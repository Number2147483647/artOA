package site.binghai.crm.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.binghai.crm.entity.*;
import site.binghai.crm.service.FieldService;
import site.binghai.crm.service.PlanDetailService;
import site.binghai.crm.service.RoomService;
import site.binghai.crm.service.UserService;
import site.binghai.crm.utils.HttpRequestUtils;
import site.binghai.crm.utils.MD5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/4/22.
 *
 * @ artOA
 */
@Controller
@RequestMapping("admin")
public class UserController extends BaseController {
    @Autowired
    private PlanDetailService planDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;

    @RequestMapping("users")
    public String users(String anySearch, ModelMap map) {
        List<User> users = StringUtils.isEmpty(anySearch) ? userService.findAll() : userService.filter(anySearch);
        List<JSONObject> infos = new ArrayList<>();
        List<Schema> schemas = getSchemas()
                .stream()
                .filter(v -> !v.isExtendField())
                .collect(Collectors.toList());
        schemas.remove(schemas.size() - 1);
        schemas.remove(schemas.size() - 1);

        for (User user : users) {
            JSONObject info = JSONObject.parseObject(user.getInfo());
            info.put("id", user.getId());
            info.put("wxBind", user.isWxBind());
            info.put("qrCode", user.getQrCode());
            info.put(MD5.shortMd5("手机号"), user.getPhone());
            info.put(MD5.shortMd5("姓名"), user.getName());

            infos.add(info);
        }
        map.put("users", infos);
        map.put("schemas", schemas);
        return "users";
    }

    private boolean schemaFilter(Schema v) {
        String[] filter = {"微信绑定", "二维码", "手机号", "姓名"};
        for (int i = 0; i < filter.length; i++) {
            if (v.getName().equals(filter[i]))
                return false;
        }
        return true;
    }

    @RequestMapping(value = "addUser", method = RequestMethod.GET)
    public String toAddUser(ModelMap map) {
        map.put("schemas", getSchemas().stream().filter(v -> {
            return !v.getName().equals("微信绑定") && !v.getName().equals("二维码");
        }).collect(Collectors.toList()));
        Map<String, String> params = HttpRequestUtils.getRequestParamMap(getServletRequest());
        if (params.get("error") != null) {
            map.put("errMsg", "保存过程中发生错误");
        }
        return "addUser";
    }

    @RequestMapping(value = {"addUser", "saveEditUser"}, method = RequestMethod.POST)
    public String addUser(Integer autoClose) {
        Map<String, String> params = HttpRequestUtils.getRequestParamMap(getServletRequest());
        List<Schema> schemas = getSchemas();
        if (params.get("pid") != null && Integer.parseInt(params.get("pid")) > 0) {
            return planDetailEditSave(params, schemas);
        } else {
            return userSaveOrEditSave(params, schemas, autoClose);
        }
    }

    private String userSaveOrEditSave(Map<String, String> params, List<Schema> schemas, Integer autoClose) {
        User user = null;
        if (params.get("userId") != null) {
            user = userService.findOne(Integer.parseInt(params.get("userId")));
        } else {
            user = new User();
            user.setQrCode(UUID.randomUUID().toString());
        }
        JSONObject values = new JSONObject();
        boolean ok = true;

        for (Schema schema : schemas) {
            if (!ok || schema.getName().equals("微信绑定") || schema.getName().equals("二维码")) {
                continue;
            }
            if (params.get(schema.getCode()) == null) {
                ok = false;
            }
            if (schema.getName().equals("姓名")) {
                user.setName(params.get(schema.getCode()));
            } else if (schema.getName().equals("手机号")) {
                user.setPhone(params.get(schema.getCode()));
            } else {
                values.put(schema.getCode(), params.get(schema.getCode()));//更新添加新的字段
            }
        }

        if (!ok) {
            if (params.get("userId") != null) {
                return "redirect:editUser?error=1&uid=" + user.getId();
            }
            return "redirect:addUser?error=1";
        }
        user.setInfo(values.toJSONString());
        userService.save(user);

        if (params.get("userId") != null) {
            if (autoClose != null) {
                return "redirect:/autoClose";
            } else {
                return "redirect:users";
            }
        }
        return "redirect:addUser";
    }

    private String planDetailEditSave(Map<String, String> params, List<Schema> schemas) {
        int planDetailId = Integer.parseInt(params.get("pid"));
        PlanDetail planDetail = planDetailService.findById(planDetailId);
        boolean ok = true;
        JSONObject values = new JSONObject();
        for (Schema schema : schemas) {
            if (!ok || schema.getName().equals("微信绑定") || schema.getName().equals("二维码")) {
                continue;
            }
            if (params.get(schema.getCode()) == null) {
                ok = false;
            }
            if (schema.getName().equals("姓名")) {
                planDetail.setUname(params.get(schema.getCode()));
            } else if (schema.getName().equals("手机号")) {
                planDetail.setUphone(params.get(schema.getCode()));
            } else {
                values.put(schema.getCode(), params.get(schema.getCode()));
            }
        }

        if (!ok) {
            return "redirect:editUser?error=1&autoClose=1&uid=" + planDetail.getUserId() + "&pid=" + planDetail.getPlanId();
        }
        planDetail.setInfo(values.toJSONString());
        planDetailService.save(planDetail);

        if(params.get("room") != null && params.get("room").length() > 0){
            Integer roomId = Integer.parseInt(params.get("room"));
            boolean res = roomService.matchPlanDetailAndRoom(planDetail, roomId, getAdmin());

            if (!res) {
                return "redirect:editUser?autoClose=1&err=2&uid=" + params.get("userId") + "&pid=" + planDetailId;
            }
        }
        return "redirect:/autoClose";
    }


    @RequestMapping("delUser")
    public String delUser(@RequestParam int uid) {
        userService.del(uid);
        return "redirect:users";
    }

    @RequestMapping("editUser")
    public String editUser(@RequestParam int uid, Integer autoClose, Integer pid, Integer err, ModelMap map) {
        List<Schema> schemas = getSchemas().stream().filter(v -> {
            return !v.getName().equals("微信绑定") && !v.getName().equals("二维码");
        }).collect(Collectors.toList());
        JSONObject info = null;
        if (pid != null) {
            PlanDetail planDetail = planDetailService.findById(pid);
            info = JSONObject.parseObject(planDetail.getInfo());
            info.put(MD5.shortMd5("姓名"), planDetail.getUname());
            info.put(MD5.shortMd5("手机号"), planDetail.getUphone());

            List<Room> rooms = roomService.getUnFullRoomList(planDetail.getPlanId());
            Room room = roomService.findRoomByPlanDetailId(planDetail);
            if(room != null){
                rooms.add(0,room);
            }
            map.put("rooms", rooms);
            map.put("errMsg", rooms.size() == 0 ? "没有可以分配的房间..." : "");
            if (err != null && err == 2) {
                map.put("errMsg", "分配房间时出现错误!可能房间分配并没有变化.");
            }
        } else {
            User user = userService.findOne(uid);
            info = JSONObject.parseObject(user.getInfo());
            info.put(MD5.shortMd5("姓名"), user.getName());
            info.put(MD5.shortMd5("手机号"), user.getPhone());
        }

        for (int i = 0; i < schemas.size(); i++) {
            schemas.get(i).setValue(info.get(schemas.get(i).getCode()));
        }

        map.put("schemas", schemas);
        map.put("userId", uid);
        map.put("autoClose", autoClose != null);
        map.put("pid", pid != null ? pid : -1);

        Map<String, String> params = HttpRequestUtils.getRequestParamMap(getServletRequest());
        if (params.get("error") != null) {
            map.put("errMsg", "保存过程中发生错误");
        }
        return pid != null ? "editPlanDetail" : "editUser";
    }
}
