package cn.yangliu.mybatis.ex;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelperException extends RuntimeException {

    private String msg;

    public HelperException(String msg) {
        this.msg = msg;
    }

    public HelperException(String msg, Exception e) {
        super(msg, e);
        this.msg = msg;
    }
}
