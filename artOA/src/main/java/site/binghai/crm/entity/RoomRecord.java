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
@Data
@Entity
public class RoomRecord {
    @Id
    @GeneratedValue
    private int id;
    private int planDetailId;
    private int roomId;
    private String roomName;
    private boolean deleted = false;
    private String killer;
    private String created;

    public RoomRecord(String roomName, int planDetailId, int roomId) {
        this.roomName = roomName;
        this.planDetailId = planDetailId;
        this.roomId = roomId;
        this.deleted = false;
        this.created = TimeFormatter.format(System.currentTimeMillis());
    }

    public RoomRecord() {
    }
}
