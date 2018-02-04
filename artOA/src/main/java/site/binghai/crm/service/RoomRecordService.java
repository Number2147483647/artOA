package site.binghai.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import site.binghai.crm.dao.RoomRecordDao;
import site.binghai.crm.entity.RoomRecord;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by binghai on 2018/2/4.
 *
 * @ artOA
 */
@Service
public class RoomRecordService {

    @Autowired
    private RoomRecordDao dao;


    public List<RoomRecord> findByRoomId(Integer roomId) {
        return dao.findAll().stream()
                .filter(v -> !v.isDeleted())
                .filter(v -> v.getRoomId() == roomId)
                .collect(Collectors.toList());
    }


    @Transactional
    public void delRoomRecordByRoomId(int roomId, String killer) {
        List<RoomRecord> rs = findByRoomId(roomId);

        if (!CollectionUtils.isEmpty(rs)) {
            for (RoomRecord roomRecord : rs) {
                roomRecord.setKiller(killer);
                roomRecord.setDeleted(true);
                dao.save(roomRecord);
            }
        }
    }
}
