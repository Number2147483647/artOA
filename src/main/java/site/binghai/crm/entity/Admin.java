package site.binghai.crm.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by binghai on 2018/4/22.
 *
 * @ artOA
 */
@Data//自动产生get set方法
@Entity//映射数据库产生实体类的表
public class Admin {//管理员
    @Id//设置id
    @GeneratedValue//id自增
    private int id;
    private String username;
    private String phone;
    private String password;
    private String owner;
    private String killer; //删除人
    private String createdTime;
    private boolean isDeleted;

    public Admin(String username, String phone, String password, String owner, String createdTime, boolean isDeleted) {
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.owner = owner;
        this.createdTime = createdTime;
        this.isDeleted = isDeleted;
    }

    public Admin() {
    }
}
