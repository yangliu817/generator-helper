package cn.yangliu.mybatis.bean;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 作者 杨柳
 * 时间 2017-10-26 17:41
 * 描述
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_link_info")
@NoArgsConstructor
public class LinkInfo implements Serializable {

    public LinkInfo(Long id) {
        this.id = id;
    }

    @TableId
    private Long id;

    private String name;

    private String user;

    private String password;

    private String host;

    private String port;

    private String databaseType;

}
