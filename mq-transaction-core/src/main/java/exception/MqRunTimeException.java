package exception;

/**
 * @author wangxi
 * @date 2019-12-18 15:53
 */
public class MqRunTimeException extends RuntimeException{

    public MqRunTimeException(String msg){
        super(msg);
    }
}
