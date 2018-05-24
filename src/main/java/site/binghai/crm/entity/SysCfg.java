package site.binghai.crm.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Administrator on 2018/4/20.
 *
 * @ artOA
 */
@Data
@Entity
public class SysCfg {
    @Id
    @GeneratedValue
    private int id;
    private boolean adminDebug; // 管理代打卡开关
    private long startUpTime; //系统启动时间
}
