package site.binghai.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import site.binghai.crm.entity.User;
import site.binghai.crm.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/4/26.
 *
 * @ artOA
 */
@RequestMapping("admin")
@Controller
public class PrinterController extends BaseController {
    @Autowired
    private UserService userService;


    @RequestMapping("print")
    public Object print(String anySearch, ModelMap map) {
        List<User> userList = StringUtils.isEmpty(anySearch) ? userService.findAll() : userService.filter(anySearch);
        map.put("users", userList);
        map.put("userIds", userList.stream().map(v -> v.getId()).collect(Collectors.toList()));
        return "printer";
    }
}
