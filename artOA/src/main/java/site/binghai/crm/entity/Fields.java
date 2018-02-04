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
@Entity
@Data
public class Fields {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String created;
    private String owner;
    private boolean extendField;
    private boolean isDeleted;
}
