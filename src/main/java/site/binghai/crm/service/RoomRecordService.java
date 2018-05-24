package site.binghai.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import site.binghai.crm.dao.RoomRecordDao;
import site.binghai.crm.entity.Admin;
import site.binghai.crm.entity.PlanDetail;
import site.binghai.crm.entity.RoomRecord;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/4/25.
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

    @Transactional
    public void delOne(int id, Admin admin) {
        RoomRecord roomRecord = dao.findOne(id);
        if (roomRecord != null) {
            roomRecord.setKiller(admin.getUsername());
            roomRecord.setDeleted(true);
            dao.save(roomRecord);
        }
    }

    public List<RoomRecord> findByPlanDetail(PlanDetail planDetail) {
        List<RoomRecord> ls = dao.findByPlanDetailId(planDetail.getId())
                .stream().filter(v -> !v.isDeleted())
                .collect(Collectors.toList());

        if (ls == null || ls.size() == 0) {
            return null;
        }

        return ls;
    }

    @Transactional
    public RoomRecord save(RoomRecord roomRecord) {
        return dao.save(roomRecord);
    }

    @Transactional
    public List<RoomRecord> delByPlanDetail(PlanDetail planDetail, Admin admin) {
        List<RoomRecord> ls = findByPlanDetail(planDetail);
        if(CollectionUtils.isEmpty(ls)){
            return new ArrayList<>();
        }
        for (int i = 0; i < ls.size(); i++) {
            ls.get(0).setDeleted(true);
            ls.get(0).setKiller(admin.getUsername());
            save(ls.get(0));
        }
        return ls;
    }
}
