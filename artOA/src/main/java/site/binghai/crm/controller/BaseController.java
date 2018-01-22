package site.binghai.crm.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by binghai on 2018/1/22.
 *
 * @ artOA
 */
public abstract class BaseController {
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


    public JSONObject fail(String err) {
        JSONObject object = new JSONObject();
        object.put("status", "fail");
        object.put("msg", err);
        return object;
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

    public JSONObject success(Object data) {
        JSONObject object = new JSONObject();
        object.put("status", "ok");
        object.put("data", data);
        return object;
    }

    public JSONObject success(Object data, String msg) {
        JSONObject object = new JSONObject();
        object.put("status", "ok");
        object.put("data", data);
        object.put("msg", msg);
        return object;
    }
}
