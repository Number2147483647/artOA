package site.binghai.crm.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by binghai on 2018/1/22.
 * 考勤细节
 *
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
    private String uname;
    private String uphone;
    private String scanOpenId; // 扫描者openId
    private String createdTime;
    private String info;
    private boolean autoKq; // 自助考勤

    public PlanDetail(String uname, String uphone, String info, int planId, int userId, String scanOpenId, String createdTime, boolean autoKq) {
        this.info = info;
        this.uphone = uphone;
        this.uname = uname;
        this.planId = planId;
        this.userId = userId;
        this.scanOpenId = scanOpenId;
        this.createdTime = createdTime;
        this.autoKq = autoKq;
    }

    public PlanDetail() {
    }
}
