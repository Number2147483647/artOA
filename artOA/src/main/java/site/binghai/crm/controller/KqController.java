package site.binghai.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import site.binghai.crm.entity.Admin;
import site.binghai.crm.entity.Plan;
import site.binghai.crm.service.PlanService;
import site.binghai.crm.service.UserService;
import site.binghai.crm.utils.TimeFormatter;

import java.util.UUID;

/**
 * Created by binghai on 2018/1/25.
 *
 * @ artOA
 */
@RequestMapping("admin")
@Controller
public class KqController extends BaseController {
    @Autowired
    private PlanService planService;
    @Autowired
    private UserService userService;

    @RequestMapping("kq")
    public String index(ModelMap map){
        map.put("all",planService.all());
        return "kq";
    }

    @RequestMapping(value = "addKq", method = RequestMethod.GET)
    public String toAddKQ(ModelMap map) {
        map.put("TjName", TimeFormatter.format2yyyy_MM_dd(System.currentTimeMillis()) + "日考勤计划");
        map.put("AllSize", userService.findAll().size());
        return "addPlan";
    }


    @RequestMapping(value = "addPlan", method = RequestMethod.POST)
    public String addPlan(@RequestParam String planName, @RequestParam Integer preSize) {
        Admin admin = (Admin) getServletRequest().getSession().getAttribute("admin");
        Plan plan = new Plan();
        plan.setAllSize(preSize);
        plan.setNowSize(0);
        plan.setCreated(TimeFormatter.format(System.currentTimeMillis()));
        plan.setDeleted(false);
        plan.setName(planName);
        plan.setOwner(admin.getUsername());
        plan.setQrPass(UUID.randomUUID().toString());
        plan.setRunning(true);
        plan.setAutoKq(false);
        planService.save(plan);
        return "redirect:kq";
    }

    @RequestMapping("stopKq")
    public String stopKq(@RequestParam Integer id){
        Plan plan = planService.findById(id);
        if(plan != null){
            plan.setRunning(false);
            planService.save(plan);
        }
        return "redirect:kq";
    }

    @RequestMapping("kqSwitch")
    public String kqSwitch(@RequestParam Integer id){
        Plan plan = planService.findById(id);
        if(plan != null){
            plan.setAutoKq(!plan.isAutoKq());
            planService.save(plan);
        }
        return "redirect:kq";
    }

    @RequestMapping("adminKq")
    public String adminKq(@RequestParam Integer id,ModelMap map){
        Plan plan = planService.findById(id);
        if(plan == null){
            return "redirect:kq";
        }
        map.put("plan",plan);
        return "adminKq";
    }

}
