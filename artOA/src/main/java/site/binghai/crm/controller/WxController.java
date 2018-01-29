package site.binghai.crm.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.binghai.crm.entity.Fields;
import site.binghai.crm.entity.Plan;
import site.binghai.crm.entity.PlanDetail;
import site.binghai.crm.entity.User;
import site.binghai.crm.service.FieldService;
import site.binghai.crm.service.PlanDetailService;
import site.binghai.crm.service.PlanService;
import site.binghai.crm.service.UserService;
import site.binghai.crm.utils.MD5;
import site.binghai.crm.utils.TimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binghai on 2018/1/27.
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

    @RequestMapping("myInfo")
    public String myInfo(ModelMap map) {
        List<Fields> fields = fieldService.findAll();

        fieldService.findAll();
        List<String> infos = new ArrayList<>();
        User user = (User) getServletRequest().getSession().getAttribute("user");
        infos.add("姓名:" + user.getName());
        infos.add("手机号:" + user.getPhone());
        JSONObject extra = JSONObject.parseObject(user.getInfo());
        extra.entrySet().forEach(v -> infos.add(getField(v.getKey(), fields) + ":" + v.getValue().toString()));
        map.put("infos", infos);
        map.put("qrCode", user.getQrCode());
        return "userLogin";
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
        if (planDetailService.findByUserIdAndPlanId(user.getId(), planId) == null) {
            PlanDetail planDetail = new PlanDetail(user.getName(), planId, user.getId(), "", TimeFormatter.format(System.currentTimeMillis()), true);

            planDetailService.save(planDetail);
            plan.setNowSize(plan.getNowSize());
            planService.save(plan);
        } else {
            return fail("你已经打卡了!");
        }
        return success();
    }
}
