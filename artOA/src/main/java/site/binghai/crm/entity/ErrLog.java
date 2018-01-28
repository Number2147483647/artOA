package site.binghai.crm.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by binghai on 2018/1/27.
 *
 * @ artOA
 */
@Entity
@Data
public class ErrLog {
    @Id
    @GeneratedValue
    private int id;
    private String clazz;
    private String method;
    private String time;
    private String info;

    public ErrLog() {
    }

    public ErrLog(String clazz, String method, String time, String info) {
        this.clazz = clazz;
        this.method = method;
        this.time = time;
        this.info = info;
    }
}
