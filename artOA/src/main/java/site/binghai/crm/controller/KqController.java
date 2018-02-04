package site.binghai.crm.controller;

import com.alibaba.fastjson.JSONArray;
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
import site.binghai.crm.service.*;
import site.binghai.crm.utils.MD5;
import site.binghai.crm.utils.TimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by binghai on 2018/1/25.
 *
 * @ artOA
 */
@RequestMapping("admin")
@Controller
public class KqController extends BaseController {
    @Autowired
    private PlanService planService;
    @Autowired
    private UserService userService;
    @Autowired
    private FieldService fieldService;
    @Autowired
    private PlanDetailService planDetailService;
    @Autowired
    private SysCfgService sysCfgService;
    @Autowired
    private WxLogin wxLoginController;

    @RequestMapping("kq")
    public String index(ModelMap map) {
        map.put("all", planService.all());
        return "kq";
    }

    @RequestMapping(value = "addKq", method = RequestMethod.GET)
    public String toAddKQ(ModelMap map) {
        map.put("TjName", TimeFormatter.format2yyyy_MM_dd(System.currentTimeMillis()) + "日考勤计划");
        map.put("AllSize", userService.findAll().size());
        return "addPlan";
    }


    @RequestMapping(value = "addPlan", method = RequestMethod.POST)
    public String addPlan(@RequestParam String planName, @RequestParam String text, @RequestParam Integer preSize) {
        Admin admin = (Admin) getServletRequest().getSession().getAttribute("admin");
        Plan plan = new Plan();
        plan.setAllSize(preSize);
        plan.setNowSize(0);
        plan.setText(text);
        plan.setCreated(TimeFormatter.format(System.currentTimeMillis()));
        plan.setDeleted(false);
        plan.setName(planName);
        plan.setOwner(admin.getUsername());
        plan.setQrPass(UUID.randomUUID().toString());
        plan.setRunning(true);
        plan.setAutoKq(false);
        planService.save(plan);
        return "redirect:kq";
    }

    @RequestMapping("stopKq")
    public String stopKq(@RequestParam Integer id) {
        Plan plan = planService.findById(id);
        if (plan != null) {
            plan.setRunning(false);
            planService.save(plan);
        }
        return "redirect:kq";
    }

    @RequestMapping("delKq")
    public String delKq(@RequestParam Integer id) {
        Plan plan = planService.findById(id);
        if (plan != null) {
            plan.setRunning(false);
            plan.setDeleted(true);
            planService.save(plan);
        }
        return "redirect:kq";
    }

    @RequestMapping("kqSwitch")
    public String kqSwitch(@RequestParam Integer id) {
        Plan plan = planService.findById(id);
        if (plan != null) {
            plan.setAutoKq(!plan.isAutoKq());
            planService.save(plan);
        }
        return "redirect:kq";
    }

    @RequestMapping("adminKq")
    public String adminKq(@RequestParam Integer id, ModelMap map) {
        Plan plan = planService.findById(id);
        if (plan == null) {
            return "redirect:kq";
        }
        map.put("plan", plan);
        List<Schema> schemas = getSchemas();
        schemas.add(0, new Schema("打卡时间"));
        map.put("schemas", schemas);
        map.put("adminDebug", sysCfgService.adminDebugOpen());
        return "adminKq";
    }

    @RequestMapping("adminDebug")
    @ResponseBody
    public Object adminDebug(@RequestParam String name, @RequestParam String planCode) {
        if (StringUtils.isEmpty(name)) {
            return fail("请输入名字!");
        }
        User user = null;
        try {
            user = userService.findByName(name);
        } catch (Exception e) {
            return fail(e.getMessage());
        }

        Admin admin = (Admin) getServletRequest().getSession().getAttribute("admin");
        Object resp = wxLoginController.adminKqByScan(user.getQrCode(), planCode, admin.getUsername() + ":" + admin.getPhone(), null);
        JSONObject json = JSONObject.parseObject(resp.toString());
        if (!json.getString("status").equals("ok")) {
            return fail("打卡失败，原因未知，请采用扫码打卡!");
        }
        return success("处理成功");
    }


    @RequestMapping("lastScan")
    @ResponseBody
    public Object lastScan(@RequestParam int planId) {
        PlanDetail pd = planDetailService.findByPlanIdLimit1(planId);
        if (pd == null) {
            return fail("empty");
        }

        User user = userService.findOne(pd.getUserId());
        if (user == null) {
            return fail("没有任何人打卡!");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<h3>%s</h3>", user.getName()));
        sb.append(String.format("<p>手机号:%s</p>", user.getPhone()));
        List<Schema> schemas = getSchemas();
        JSONObject info = JSONObject.parseObject(user.getInfo());
        info.entrySet().forEach(v -> sb.append(String.format("<p>%s:%s</p>", getSchema(v.getKey(), schemas), v.getValue().toString())));

        List<PlanDetail> planDetails = planDetailService.findByPlanId(planId);
        planDetails.sort((a, b) -> b.getId() - a.getId());

        JSONObject data = new JSONObject();
        data.put("time", pd.getCreatedTime());

        JSONArray bodyInfo = new JSONArray();

        planDetails.forEach(v -> {
            User ru = userService.findOne(v.getUserId());
            bodyInfo.add(matchFields(v, schemas));
        });

        data.put("data", bodyInfo);
        return success(data, sb.toString());
    }

    private JSONObject matchFields(PlanDetail planDetail, List<Schema> schemas) {
        JSONObject object = new JSONObject();
        object.put("userId", planDetail.getUserId());
        object.put("pid", planDetail.getId());
        List<String> lines = new ArrayList<>();
        JSONObject info = JSONObject.parseObject(planDetail.getInfo());
        for (int i = 0; i < schemas.size(); i++) {
            if (schemas.get(i).getCode().equals(MD5.shortMd5("姓名"))) {
                lines.add(planDetail.getUname());
            } else if (schemas.get(i).getCode().equals(MD5.shortMd5("手机号"))) {
                lines.add(planDetail.getUphone());
            } else {
                String v = info.getString(schemas.get(i).getCode());
                lines.add(v == null ? "" : v);
            }
        }
        lines.add(0, planDetail.getCreatedTime());
        object.put("lines", lines);
        return object;
    }

    public List<Schema> getSchemas() {
        List<String> schema = new ArrayList<>();
        schema.add("姓名");
        schema.add("手机号");
        schema.addAll(fieldService.findAll().stream().map(v -> v.getName()).collect(Collectors.toList()));
        return schema.stream().map(v -> new Schema(v)).collect(Collectors.toList());
    }
}
