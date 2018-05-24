package site.binghai.crm.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.binghai.crm.entity.*;
import site.binghai.crm.service.*;
import site.binghai.crm.utils.MD5;
import site.binghai.crm.utils.TimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/27.
 *
 * @ artOA
 */
@RequestMapping("user")
@Controller
public class WxController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private FieldService fieldService;
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanDetailService planDetailService;
    @Autowired
    private RoomRecordService roomRecordService;

    @RequestMapping("myInfo")
    public String myInfo(ModelMap map, Integer pid) {
        if (pid != null) {
            return planDetailInfo(map, pid);
        }
        List<String> infos = new ArrayList<>();
        User user = (User) getServletRequest().getSession().getAttribute("user");
        user = userService.findOne(user.getId());
        getServletRequest().getSession().setAttribute("user", user);

        infos.add("姓名:" + user.getName());
        infos.add("手机号:" + user.getPhone());
        JSONObject extra = JSONObject.parseObject(user.getInfo());
        getSchemas().stream().filter(v -> !v.isNotVisible4User()).forEach(v -> {
            if (extra.get(v.getCode()) != null) {
                infos.add(v.getName() + ":" + extra.get(v.getCode()));
            }
        });
//        extra.entrySet().forEach(v -> infos.add(getField(v.getKey(), fields) + ":" + v.getValue().toString()));
        map.put("infos", infos);
        map.put("qrCode", user.getQrCode());
        return "userLogin";
    }

    private String planDetailInfo(ModelMap map, Integer pid) {
        List<String> infos = new ArrayList<>();
        PlanDetail planDetail = planDetailService.findById(pid);
        if (planDetail == null) {
            return "/autoClose";
        }
        User user = (User) getServletRequest().getSession().getAttribute("user");

        infos.add("姓名:" + planDetail.getUname());
        infos.add("手机号:" + planDetail.getUphone());
        JSONObject extra = JSONObject.parseObject(planDetail.getInfo());
        getSchemas().stream().filter(v -> !v.isNotVisible4User()).forEach(v -> {
            if (extra.get(v.getCode()) != null) {
                infos.add(v.getName() + ":" + extra.get(v.getCode()));
            }
        });
//        extra.entrySet().forEach(v -> infos.add(getField(v.getKey(), fields) + ":" + v.getValue().toString()));
        infos.add("房间:" + getRoom4User(planDetail));
        map.put("infos", infos);
        map.put("qrCode", user.getQrCode());
        return "userLogin";
    }

    private String getRoom4User(PlanDetail planDetail) {
        List<RoomRecord> rs = roomRecordService.findByPlanDetail(planDetail);
        return rs == null ? "" : rs.get(0).getRoomName();
    }

    @RequestMapping("unBind")
    public String unBind() {
        User user = (User) getServletRequest().getSession().getAttribute("user");
        logger.error("用户解绑!{}", user);
        user = userService.findByOpenId(user.getOpenId());
        user.setWxBind(false);
        user.setOpenId(null);
        userService.save(user);
        getServletRequest().getSession().invalidate();
        return "redirect:/user/myInfo";
    }


    @RequestMapping("task")
    public String tasks(ModelMap map) {
        User user = (User) getServletRequest().getSession().getAttribute("user");
        List<Plan> plans = planService.all();

        plans.forEach(v -> {
            PlanDetail detail = planDetailService.findByUserIdAndPlanId(user.getId(), v.getId());
            v.setDeleted(detail == null);
        });

        map.put("tasks", plans);
        return "userTask";
    }

    @RequestMapping("kq")
    @ResponseBody
    public Object kq(@RequestParam Integer planId) {
        Plan plan = planService.findById(planId);
        User user = (User) getServletRequest().getSession().getAttribute("user");
        PlanDetail exist = planDetailService.findByUserIdAndPlanId(user.getId(), planId);
        if (exist == null && !plan.isDeleted() && plan.isRunning() && plan.isAutoKq()) {
            PlanDetail planDetail = new PlanDetail(user.getName(), user.getPhone(), user.getInfo(), planId, user.getId(), "", TimeFormatter.format(System.currentTimeMillis()), true);

            exist = planDetailService.save(planDetail);
            plan.setNowSize(plan.getNowSize() + 1);
            planService.save(plan);
        } else {
            if (exist != null) {
                return success(exist.getId(), "success");
            }
            return fail("你现在无法打卡!");
        }
        return exist == null ? fail("打卡失败!") : success(exist.getId(), "success");
    }
}
