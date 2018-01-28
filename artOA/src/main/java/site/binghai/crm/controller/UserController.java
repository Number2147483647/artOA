package site.binghai.crm.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import site.binghai.crm.entity.Schema;
import site.binghai.crm.entity.User;
import site.binghai.crm.service.FieldService;
import site.binghai.crm.service.UserService;
import site.binghai.crm.utils.HttpRequestUtils;
import site.binghai.crm.utils.MD5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by binghai on 2018/1/22.
 *
 * @ artOA
 */
@Controller
@RequestMapping("admin")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @RequestMapping("users")
    public String users(String anySearch, ModelMap map) {
        List<User> users = StringUtils.isEmpty(anySearch) ? userService.findAll() : userService.filter(anySearch);
        List<JSONObject> infos = new ArrayList<>();
        List<Schema> schemas = getSchemas();
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

    @RequestMapping(value = "addUser", method = RequestMethod.POST)
    public String addUser() {
        Map<String, String> params = HttpRequestUtils.getRequestParamMap(getServletRequest());
        List<Schema> schemas = getSchemas();
        boolean ok = true;
        User user = new User();
        JSONObject values = new JSONObject();
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
                values.put(schema.getCode(), params.get(schema.getCode()));
            }
        }

        if (!ok) {
            return "redirect:addUser?error=1";
        }

        user.setInfo(values.toJSONString());
        user.setQrCode(UUID.randomUUID().toString());
        user.setWxBind(false);
        userService.save(user);
        return "redirect:addUser";
    }
}
