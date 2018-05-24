package site.binghai.crm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import site.binghai.crm.entity.Admin;
import site.binghai.crm.service.AdminService;

/**
 * Created by Administrator on 2018/4/22.
 *
 * @ artOA
 */
@Controller
@RequestMapping("/")
public class LoginController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private AdminService adminService;

    @ResponseBody
    @RequestMapping("login")
    public Object adminLogin(Admin admin) {
        if (admin != null) {
            logger.warn("登录信息校验:{}", admin);
            admin = adminService.adminLogin(admin);
            if (admin != null) {
                getServletRequest().getSession().setAttribute("admin", admin);
                return success();
            }
        } else {
            logger.error("入参有误:{}", admin);
        }
        return fail("认证失败");
    }

    @RequestMapping("logout")
    public String logout(){
        getServletRequest().getSession().invalidate();
        return "redirect:/";
    }
}
