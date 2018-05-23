package site.binghai.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.binghai.crm.entity.Admin;
import site.binghai.crm.entity.Fields;
import site.binghai.crm.service.FieldService;
import site.binghai.crm.utils.TimeFormatter;

/**
 * Created by binghai on 2018/4/22.
 *
 * @ artOA
 */
@Controller
@RequestMapping("admin")
public class FieldContoller extends BaseController {
    @Autowired
    private FieldService fieldService;

    @RequestMapping("fields")
    public String fileds(ModelMap map) {
        map.put("fields", fieldService.findAll());
        return "fields";
    }

    @RequestMapping(value = "addField", method = RequestMethod.POST)
    @ResponseBody
    public Object addField(@RequestParam String addField,@RequestParam boolean extendField,@RequestParam boolean notVisible4User) {
        if (StringUtils.isEmpty(addField)) {
            return fail("输入有误!");
        }
        if ("姓名".equals(addField) || "手机号".equals(addField) || fieldService.exist(addField)) {
            return fail("该字段已存在!");
        }
        Fields fields = new Fields();
        fields.setCreated(TimeFormatter.format(System.currentTimeMillis()));
        fields.setDeleted(false);
        fields.setName(addField);
        fields.setExtendField(extendField);
        fields.setNotVisible4User(notVisible4User);
        Admin admin = (Admin) getServletRequest().getSession().getAttribute("admin");
        fields.setOwner(admin.getUsername());

        fieldService.save(fields);
        return success();
    }

    @RequestMapping("deleteField")
    @ResponseBody
    public Object deleteField(@RequestParam Integer id){
        fieldService.delete(getAdmin(),id);
        return success();
    }
}
