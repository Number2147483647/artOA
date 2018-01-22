package site.binghai.crm.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by binghai on 2018/1/22.
 *  考勤细节
 * @ artOA
 */
@Data
@Entity
public class PlanDetail {
    @Id
    @GeneratedValue
    private int id;
    private int planId;
    private int userId;
    private String scanOpenId; // 扫描者openId
    private String createdTime;

}
