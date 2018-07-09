package repository;

import api.TccTransactionStatus;
import api.TccTransactionXid;
import bean.Transaction;
import exception.TccTransactionIOException;
import org.springframework.util.StringUtils;
import serializer.JdkSerializer;
import serializer.ObjectSerializer;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * JDBC事务存储仓库
 */
public class JdbcTransactionRepository implements TransactionRepository {

    //TCC会部署于多台系统，但有有可能数据库是同一个，数据库需要分离
    private String domain;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    private String suffix;

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    ObjectSerializer serializer = new JdkSerializer<Transaction>();

    protected Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new TccTransactionIOException(e);
        }
    }

    protected String getTableName(){

        return StringUtils.isEmpty(suffix)? "TCC_TRANSACTION": "TCC_TRANSACTION"+suffix;
    }

    protected void releaseConnection(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            throw new TccTransactionIOException(e);
        }
    }

    private void closeStatement(Statement stmt) {
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (Exception ex) {
            throw new TccTransactionIOException(ex);
        }
    }

    @Override
    public int create(Transaction transaction) {

        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = this.getConnection();

            StringBuilder builder = new StringBuilder();
            builder.append("INSERT INTO " + getTableName() +
                    "(GLOBAL_TX_ID,BRANCH_QUALIFIER,TRANSACTION_TYPE,CONTENT,STATUS,RETRIED_COUNT,CREATE_TIME,LAST_UPDATE_TIME,VERSION");
            builder.append(!StringUtils.isEmpty(domain) ? ",DOMAIN ) VALUES (?,?,?,?,?,?,?,?,?,?)" : ") VALUES (?,?,?,?,?,?,?,?,?)");

            stmt = connection.prepareStatement(builder.toString());

            stmt.setBytes(1, transaction.getXid().getGlobalTransactionId());
            stmt.setBytes(2, transaction.getXid().getBranchQualifier());
            stmt.setInt(3, transaction.getTransactionType().getId());
            stmt.setBytes(4, serializer.serialize(transaction));
            stmt.setInt(5, transaction.getStatus().getId());
            stmt.setInt(6, transaction.getRetriedCount());
            stmt.setTimestamp(7, new java.sql.Timestamp(transaction.getCreateTime().getTime()));
            stmt.setTimestamp(8, new java.sql.Timestamp(transaction.getLastUpdateTime().getTime()));
            stmt.setLong(9, transaction.getVersion());

            if (!StringUtils.isEmpty(domain)) {
                stmt.setString(10, domain);
            }

            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new TccTransactionIOException(e);
        } finally {
            closeStatement(stmt);
            this.releaseConnection(connection);
        }
    }

    @Override
    public int update(Transaction transaction) {

        Connection connection = null;
        PreparedStatement stmt = null;

        Date lastUpdateTime = transaction.getLastUpdateTime();
        long currentVersion = transaction.getVersion();

        transaction.setLastUpdateTime(new Date());
        transaction.setVersion(transaction.getVersion() + 1);

        try {

            connection = this.getConnection();

            StringBuilder builder = new StringBuilder();
            builder.append("UPDATE " + getTableName() + " SET " +
                    "CONTENT = ?,STATUS = ?,LAST_UPDATE_TIME = ?, RETRIED_COUNT = ?,VERSION = VERSION+1 WHERE GLOBAL_TX_ID = ? AND BRANCH_QUALIFIER = ? AND VERSION = ?");

            builder.append(StringUtils.isEmpty(domain) ? "" : " AND DOMAIN = ?");

            stmt = connection.prepareStatement(builder.toString());

            stmt.setBytes(1, serializer.serialize(transaction));
            stmt.setInt(2, transaction.getStatus().getId());
            stmt.setTimestamp(3, new Timestamp(transaction.getLastUpdateTime().getTime()));

            stmt.setInt(4, transaction.getRetriedCount());
            stmt.setBytes(5, transaction.getXid().getGlobalTransactionId());
            stmt.setBytes(6, transaction.getXid().getBranchQualifier());
            stmt.setLong(7, currentVersion);

            if (!StringUtils.isEmpty(domain)) {
                stmt.setString(8, domain);
            }

            int result = stmt.executeUpdate();

            return result;
        }catch (Throwable e){

            transaction.setLastUpdateTime(lastUpdateTime);
            transaction.setVersion(currentVersion);
            throw new TccTransactionIOException(e);
        }finally {

            closeStatement(stmt);
            this.releaseConnection(connection);
        }

    }

    @Override
    public int delete(Transaction transaction) {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = this.getConnection();

            StringBuilder builder = new StringBuilder();
            builder.append("DELETE FROM " + getTableName() +
                    " WHERE GLOBAL_TX_ID = ? AND BRANCH_QUALIFIER = ?");

            builder.append(StringUtils.isEmpty(domain) ? "": " AND DOMAIN = ?");

            stmt = connection.prepareStatement(builder.toString());

            stmt.setBytes(1, transaction.getXid().getGlobalTransactionId());
            stmt.setBytes(2, transaction.getXid().getBranchQualifier());

            if (!StringUtils.isEmpty(domain)) {
                stmt.setString(3, domain);
            }

            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new TccTransactionIOException(e);
        } finally {
            closeStatement(stmt);
            this.releaseConnection(connection);
        }
    }

    @Override
    public Transaction findByXid(TccTransactionXid xid) {
        return null;
    }

    @Override
    public List<Transaction> findAllUnfinished(Date date) {

        List<Transaction> transactions = new ArrayList<Transaction>();

        Connection connection = null;
        PreparedStatement stmt = null;

        connection = this.getConnection();

        StringBuilder builder = new StringBuilder();

        builder.append("SELECT GLOBAL_TX_ID, BRANCH_QUALIFIER, CONTENT,STATUS,TRANSACTION_TYPE,CREATE_TIME,LAST_UPDATE_TIME,RETRIED_COUNT,VERSION");
        builder.append(!StringUtils.isEmpty(domain) ? ",DOMAIN" : "");
        builder.append("  FROM " + getTableName() + " WHERE LAST_UPDATE_TIME < ?");
        builder.append(!StringUtils.isEmpty(domain) ? " AND DOMAIN = ?" : "");

        try {

            stmt = connection.prepareStatement(builder.toString());
            stmt.setTimestamp(1, new Timestamp(date.getTime()));

            if (!StringUtils.isEmpty(domain)) {
                stmt.setString(2, domain);
            }

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                byte[] transactionBytes = resultSet.getBytes(3);
                Transaction transaction = (Transaction) serializer.deserialize(transactionBytes);
                transaction.setStatus(TccTransactionStatus.valueOf(resultSet.getInt(4)));
                transaction.setLastUpdateTime(resultSet.getDate(7));
                transaction.setVersion(resultSet.getLong(9));
                transaction.resetRetriedCount(resultSet.getInt(8));
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public String toString() {
        return "JdbcTransactionRepository{" +
                "domain='" + domain + '\'' +
                ", suffix='" + suffix + '\'' +
                ", dataSource=" + dataSource +
                ", serializer=" + serializer +
                '}';
    }
}
