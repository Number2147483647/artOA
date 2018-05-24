package site.binghai.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import site.binghai.crm.config.Config;
import site.binghai.crm.entity.Plan;
import site.binghai.crm.entity.PlanDetail;
import site.binghai.crm.entity.User;
import site.binghai.crm.service.PlanDetailService;
import site.binghai.crm.service.PlanService;
import site.binghai.crm.service.UserService;
import site.binghai.crm.utils.MD5;
import site.binghai.crm.utils.TimeFormatter;

/**
 * Created by Administrator on 2018/4/27.
 *
 * @ artOA
 */
@RequestMapping("wx")
@Controller
@CrossOrigin(origins = "*")
public class WxLogin extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanDetailService planDetailService;


    @RequestMapping("login")
    public String login(String openid, String validate, ModelMap map) {
        if (StringUtils.isEmpty(openid) || StringUtils.isEmpty(validate)) {
            return "redirect:http://weixin.qdxiaogutou.com/login.php?backUrl="+ Config.server+"/wx/login";
        }

        if (!MD5.encryption(openid + "binghai").equals(validate)) {
            return "redirect:http://weixin.qdxiaogutou.com/login.php?backUrl="+Config.server+"/wx/login";
        }

        User user = userService.findByOpenId(openid);
        if (user == null) {
            map.put("openid", openid);
            return "wxBind";
        }

        getServletRequest().getSession().setAttribute("user", user);
        Object back = getServletRequest().getSession().getAttribute("back");
        return back != null ? "redirect:" + back.toString() : "redirect:/user/myInfo";
    }

    @RequestMapping(value = "wxBind", method = RequestMethod.POST)
    @ResponseBody
    public Object wxBind(@RequestParam String name, @RequestParam String phone, @RequestParam String openid) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(phone) || StringUtils.isEmpty(openid)) {
            return fail("输入不正确!");
        }
        if (userService.findByOpenId(openid) != null) {
            return "redirect:/user/myInfo";
        }
        User user = userService.findByNameAndPhone(name, phone);

        if (user == null) {
            return fail("绑定失败，请咨询管理员!");
        }
        user.setWxBind(true);
        user.setOpenId(openid);
        userService.save(user);

        return success();
    }

    @RequestMapping("confirmPlanCode")
    @ResponseBody
    public Object confirmPlanCode(@RequestParam String qrCode, String callback) {
        Plan plan = planService.findByQrCode(qrCode);

        return plan == null ? jsoupFail("-1", callback) : jsoupSuccess(plan, "success", callback);
    }

    @RequestMapping("adminKqByScan")
    @ResponseBody
    public Object adminKqByScan(@RequestParam String userCode, @RequestParam String planCode, @RequestParam String scanOpenId, String callback) {
        Plan plan = planService.findByQrCode(planCode);
        User user = userService.findByQrCode(userCode);
        if (plan == null || user == null) {
            return jsoupFail("-1", callback);
        }

        if (planDetailService.findByUserIdAndPlanId(user.getId(), plan.getId()) == null) {
            PlanDetail planDetail = new PlanDetail(user.getName(), user.getPhone(), user.getInfo(), plan.getId(), user.getId(), scanOpenId, TimeFormatter.format(System.currentTimeMillis()), false);

            planDetailService.save(planDetail);
            plan.setNowSize(plan.getNowSize());
            planService.save(plan);
        }

        return jsoupSuccess(user, "success", callback);//安全地从其他页面获取数据
    }
}
