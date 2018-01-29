package site.binghai.crm.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by binghai on 2018/1/29.
 *
 * @ artOA
 */
@Data
@Entity
public class SysCfg {
    @Id
    @GeneratedValue
    private int id;
    private long startUpTime; //系统启动时间
}
