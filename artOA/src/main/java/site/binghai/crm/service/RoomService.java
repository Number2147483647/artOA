package site.binghai.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.binghai.crm.dao.RoomDao;
import site.binghai.crm.entity.Room;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by binghai on 2018/2/4.
 *
 * @ artOA
 */
@Service
public class RoomService {
    @Autowired
    private RoomDao roomDao;


    public List<Room> findByPlanId(Integer planId) {
        List<Room> rooms = roomDao.findAll().stream()
                .filter(v -> !v.isDeleted())
                .filter(v -> v.getPlanId() == planId)
                .collect(Collectors.toList());

        if(rooms == null){
            return Collections.EMPTY_LIST;
        }

        return rooms;
    }

    @Transactional
    public void save(Room room) {
        roomDao.save(room);
    }

    public Room findById(Integer roomId) {
        return roomDao.findOne(roomId);
    }
}
