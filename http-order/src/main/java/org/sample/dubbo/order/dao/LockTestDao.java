package org.sample.dubbo.order.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author wangxi
 * @date 2019-12-04 22:34
 */
public interface LockTestDao {

    int updateOrderMoney(@Param("id") Integer id);

    int selectOrderMoney(@Param("id") Integer id);

}
