package site.binghai.crm.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by binghai on 2018/1/22.
 * 考勤计划
 * @ artOA
 */
@Data
@Entity
public class Plan {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String owner;
    private String created;
    private String qrPass;
    private boolean running;
    private boolean isDeleted;
}
