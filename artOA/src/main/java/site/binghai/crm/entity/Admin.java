package site.binghai.crm.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by binghai on 2018/1/22.
 *
 * @ artOA
 */
@Data
@Entity
public class Admin {
    @Id
    @GeneratedValue
    private int id;
    private String username;
    private String phone;
    private String password;
    private String owner;
    private String createdTime;
    private boolean isDeleted;

}
