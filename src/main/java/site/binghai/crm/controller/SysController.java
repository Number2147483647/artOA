package site.binghai.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.binghai.crm.entity.Admin;
import site.binghai.crm.service.AdminService;
import site.binghai.crm.utils.TimeFormatter;

/**
 * Created by binghai on 2018/4/29.
 *
 * @ artOA
 */
@Controller
@RequestMapping("admin")
public class SysController extends BaseController{
    @Autowired
    private AdminService adminService;

    @RequestMapping("sys")
    public String sys(Integer err ,ModelMap map){
        map.put("ads",adminService.findAll());
        map.put("err",err == null ? "":"手机号已经存在，不能重复添加!");
        return "sys";
    }

    @RequestMapping("addAdmin")
    public String addAdmin(@RequestParam String name,@RequestParam String phone,@RequestParam String pass){
        Admin admin = adminService.findByPhone(phone);
        if(admin != null){
            return "redirect:sys?err=1";
        }
        Admin creator = (Admin) getServletRequest().getSession().getAttribute("admin");
        Admin add = new Admin(name,phone,pass,creator.getUsername(), TimeFormatter.format(System.currentTimeMillis()),false);

        adminService.save(add);
        return "redirect:sys";
    }

    @RequestMapping("delAdmin")
    public String delAdmin(@RequestParam Integer id){
        Admin del = adminService.findById(id);
        if(del == null || del.isDeleted()){
            return "redirect:sys";
        }

        Admin killer = (Admin) getServletRequest().getSession().getAttribute("admin");
        del.setKiller(killer.getUsername());
        del.setDeleted(true);

        adminService.save(del);
        return "redirect:sys";
    }
}
