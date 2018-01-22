package site.binghai.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import site.binghai.crm.service.UserService;

/**
 * Created by binghai on 2018/1/22.
 *
 * @ artOA
 */
@Controller
@RequestMapping("admin")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("users")
    public String users(ModelMap map) {
        map.put("users", userService.findAll());
        return "users";
    }
}
