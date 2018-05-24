package site.binghai.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import site.binghai.crm.entity.Plan;
import site.binghai.crm.entity.Room;
import site.binghai.crm.service.PlanService;
import site.binghai.crm.service.RoomRecordService;
import site.binghai.crm.service.RoomService;

import java.util.List;

/**
 * Created by Administrator on 2018/4/24.
 *
 * @ artOA
 */
@RequestMapping("admin")
@Controller
public class roomManageController extends BaseController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private PlanService planService;
    @Autowired
    private RoomRecordService roomRecordService;

    @RequestMapping("roomManage")
    public String roomManage(@RequestParam Integer planId, ModelMap map) {
        List<Room> roomList = roomService.findByPlanId(planId);
        map.put("roomList", roomList);
        map.put("plan", planService.findById(planId));

        return "roomManage";
    }

    @RequestMapping(value = "addRoom", method = RequestMethod.POST)
    public String addRoom(@RequestParam String roomName, @RequestParam Integer roomSize, @RequestParam Integer planId, ModelMap map) {
        if(StringUtils.isEmpty(roomName) || roomSize == null || roomSize <= 0 || planId <= 0){
            return "redirect:roomManage?planId=" + planId;
        }
        Plan plan = planService.findById(planId);

        Room room = new Room(planId, getAdmin().getUsername(), plan.getName(), roomName, roomSize, 0);
        roomService.save(room);

        return "redirect:roomManage?planId=" + planId;
    }


    @RequestMapping("delRoom")
    public String delRoom(@RequestParam Integer roomId) {
        Room room = roomService.findById(roomId);
        if (room != null) {
            roomRecordService.delRoomRecordByRoomId(roomId, getAdmin().getUsername());
            room.setDeleted(true);
            room.setKiller(getAdmin().getUsername());
            roomService.save(room);
        }

        return "redirect:roomManage?planId=" + room.getPlanId();
    }
}
