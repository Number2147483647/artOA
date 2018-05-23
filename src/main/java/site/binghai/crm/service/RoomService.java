package site.binghai.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.binghai.crm.dao.RoomDao;
import site.binghai.crm.entity.Admin;
import site.binghai.crm.entity.PlanDetail;
import site.binghai.crm.entity.Room;
import site.binghai.crm.entity.RoomRecord;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by binghai on 2018/4/25.
 *
 * @ artOA
 */
@Service
public class RoomService {
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private RoomRecordService roomRecordService;


    public List<Room> findByPlanId(Integer planId) {
        List<Room> rooms = roomDao.findAll().stream()
                .filter(v -> !v.isDeleted())
                .filter(v -> v.getPlanId() == planId)
                .collect(Collectors.toList());

        if (rooms == null) {
            return Collections.EMPTY_LIST;
        }

        return rooms;
    }

    @Transactional
    public Room save(Room room) {
        return roomDao.save(room);
    }

    public Room findById(Integer roomId) {
        return roomDao.findOne(roomId);
    }

    public List<Room> getUnFullRoomList(Integer planId) {
        List<Room> rs = new ArrayList<>();
        roomDao.findAll()
                .stream().filter(v -> !v.isDeleted())
                .filter(v -> planId.equals(v.getPlanId()))
                .filter(v -> v.getSize() > v.getOccupied())
                .forEach(v -> {
                    for (int i = 0; i < v.getSize() - v.getOccupied(); i++) {
                        rs.add(v);
                    }
                });

        return rs;
    }

    @Transactional
    public boolean matchPlanDetailAndRoom(PlanDetail planDetail, Integer roomId, Admin admin) {
        Room room = findById(roomId);
        int flag = -1;
        if (room != null && room.getOccupied() < room.getSize()) {
            room.setOccupied(room.getOccupied() + 1);
            save(room);

            RoomRecord roomRecord = new RoomRecord(room.getName(), planDetail.getId(), roomId);
            roomRecord.setDeleted(false);
            roomRecord = roomRecordService.save(roomRecord);
            flag = roomRecord.getId();
        } else {
            return false;
        }

        List<RoomRecord> rrs = roomRecordService.findByPlanDetail(planDetail);
        if (rrs != null && rrs.size() > 1) {
            for (int i = 0; i < rrs.size(); i++) {
                if (rrs.get(i).getId() != flag) {
                    roomRecordService.delOne(rrs.get(i).getId(), admin);
                    room = findById(rrs.get(i).getRoomId());
                    room.setOccupied(room.getOccupied() - 1);
                    save(room);
                }
            }
        }

        return true;
    }

    @Transactional
    public void roomOccupiedRmOne(int roomId) {
        Room room = findById(roomId);
        room.setOccupied(room.getOccupied() - 1);
        save(room);
    }

    public Room findRoomByPlanDetailId(PlanDetail planDetail) {
        List<RoomRecord> rms = roomRecordService.findByPlanDetail(planDetail);
        if(rms != null && rms.size() == 1){
            int roomId = rms.get(0).getRoomId();
            return findById(roomId);
        }

        return null;
    }
}
