package cn.yangliu.mybatis.ex;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Helper exception.
 */
@Getter
@Setter
public class HelperException extends RuntimeException {

    private String msg;

    /**
     * Instantiates a new Helper exception.
     *
     * @param msg the msg
     */
    public HelperException(String msg) {
        this.msg = msg;
    }

    /**
     * Instantiates a new Helper exception.
     *
     * @param msg the msg
     * @param e   the e
     */
    public HelperException(String msg, Exception e) {
        super(msg, e);
        this.msg = msg;
    }
}
