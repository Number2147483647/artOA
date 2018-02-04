package site.binghai.crm.entity;

import lombok.Data;
import site.binghai.crm.utils.TimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by binghai on 2018/2/4.
 *
 * @ artOA
 */
@Entity
@Data
public class Room {
    @Id
    @GeneratedValue
    private int id;
    private int planId;
    private String planName;
    private String name;
    private int size;
    private int occupied;
    private String owner;
    private String killer;
    private String created;
    private boolean deleted;

    public Room() {
    }

    public Room(int planId,String owner, String planName, String name, int size, int occupied) {
        this.owner = owner;
        this.planId = planId;
        this.planName = planName;
        this.name = name;
        this.size = size;
        this.occupied = occupied;
        this.created = TimeFormatter.format(System.currentTimeMillis());
        this.deleted = false;
    }
}
