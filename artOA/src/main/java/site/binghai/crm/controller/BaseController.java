package site.binghai.crm.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import site.binghai.crm.entity.Admin;
import site.binghai.crm.entity.Fields;
import site.binghai.crm.entity.Schema;
import site.binghai.crm.service.AdminService;
import site.binghai.crm.service.FieldService;
import site.binghai.crm.utils.MD5;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by binghai on 2018/1/22.
 *
 * @ artOA
 */
public abstract class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FieldService fieldService;

    /**
     * 从thread local获取网络上下文
     */
    public HttpServletRequest getServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes;
        if (requestAttributes instanceof ServletRequestAttributes) {
            servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    public Admin getAdmin(){
        return (Admin) getServletRequest().getSession().getAttribute("admin");
    }

    public JSONObject fail(String err) {
        JSONObject object = new JSONObject();
        object.put("status", "fail");
        object.put("msg", err);
        return object;
    }

    public Object jsoupFail(String err, String callback) {
        if(StringUtils.isEmpty(callback)){
            return fail(err);
        }
        return callback + "(" + fail(err).toJSONString() + ")";
    }

    public JSONObject success() {
        JSONObject object = new JSONObject();
        object.put("status", "ok");
        return object;
    }

    public JSONObject success(String msg) {
        JSONObject object = new JSONObject();
        object.put("status", "ok");
        object.put("msg", msg);
        return object;
    }

    public Object jsoupSuccess(Object data, String msg, String callBack) {
        if(StringUtils.isEmpty(callBack)){
            return success(data, msg);
        }
        callBack = callBack == null ? "" : callBack;
        return callBack + "(" + success(data, msg).toJSONString() + ")";
    }

    public JSONObject success(Object data, String msg) {
        JSONObject object = new JSONObject();
        object.put("status", "ok");
        object.put("data", data);
        object.put("msg", msg);
        return object;
    }

    public List<Schema> getSchemas() {
        List<String> schema = new ArrayList<>();
        schema.add("姓名");
        schema.add("手机号");
        schema.addAll(fieldService.findAll().stream().map(v -> v.getName()).collect(Collectors.toList()));
        schema.add("微信绑定");
        schema.add("二维码");
        return schema.stream().map(v -> new Schema(v)).collect(Collectors.toList());
    }

    public String getField(String key, List<Fields> fields) {
        for (Fields f : fields) {
            if (key.equals(MD5.shortMd5(f.getName()))) {
                return f.getName();
            }
        }

        return "其他信息";
    }

    public String getSchema(String key, List<Schema> schemas) {
        for (int i = 0; i < schemas.size(); i++) {
            if (MD5.shortMd5(schemas.get(i).getName()).equals(key)) {
                return schemas.get(i).getName();
            }
        }
        return "其他信息";
    }
}
