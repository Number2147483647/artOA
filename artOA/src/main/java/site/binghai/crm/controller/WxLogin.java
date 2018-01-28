package site.binghai.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.binghai.crm.entity.User;
import site.binghai.crm.service.UserService;
import site.binghai.crm.utils.MD5;

/**
 * Created by binghai on 2018/1/27.
 *
 * @ artOA
 */
@RequestMapping("wx")
@Controller
public class WxLogin extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("login")
    public String login(@RequestParam String openid, @RequestParam String validate, ModelMap map) {
        if (!MD5.encryption(openid + "binghai").equals(validate)) {
            return "redirect:http://weixin.qdxiaogutou.com/login.php?backUrl=http://art.nanayun.cn:8080/wx/login";
        }

        User user = userService.findByOpenId(openid);
        if (user == null) {
            map.put("openid", openid);
            return "wxBind";
        }

        getServletRequest().getSession().setAttribute("user", user);
        Object back = getServletRequest().getSession().getAttribute("back");
        return back != null ? "redirect:" + back.toString() : "/user/myInfo";
    }

    @RequestMapping(value = "wxBind", method = RequestMethod.POST)
    @ResponseBody
    public Object wxBind(@RequestParam String name, @RequestParam String phone, @RequestParam String openid) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(phone) || StringUtils.isEmpty(openid)) {
            return fail("输入不正确!");
        }
        if(userService.findByOpenId(openid) != null){
            return "redirect:/user/myInfo";
        }
        User user = userService.findByNameAndPhone(name,phone);

        if(user == null){
            return fail("绑定失败，请咨询管理员!");
        }
        user.setWxBind(true);
        user.setOpenId(openid);
        userService.save(user);

        return success();
    }
}
