package site.binghai.crm.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Administrator on 2018/4/22.
 *
 * @ artOA
 */
@Entity
@Data
public class Fields {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String created;
    private String owner;
    private String killer;
    private String killTime;
    private boolean extendField;//拓展字段
    private boolean notVisible4User; // 对用户不可见
    private boolean isDeleted;
}
