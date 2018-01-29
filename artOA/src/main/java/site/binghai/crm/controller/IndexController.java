package site.binghai.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import site.binghai.crm.entity.PlanDetail;
import site.binghai.crm.service.PlanDetailService;
import site.binghai.crm.service.SysCfgService;
import site.binghai.crm.service.UserService;

/**
 * Created by binghai on 2018/1/22.
 *
 * @ artOA
 */
@RequestMapping("admin")
@Controller
public class IndexController extends BaseController {

    @Autowired
    private PlanDetailService planDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private SysCfgService sysCfgService;

    @RequestMapping("index")
    public String index(ModelMap map){
        map.put("sysRunning",sysCfgService.howLongSystemRunning());
        map.put("manyPeople",userService.findAll().size());
        map.put("manyKq",planDetailService.todayKq().size());
        return "index";
    }
}
