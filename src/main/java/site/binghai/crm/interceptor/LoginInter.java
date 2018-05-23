package site.binghai.crm.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by binghai on 2018/4/22.
 *
 * @ artOA
 */
public class LoginInter extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session != null) {
            if (session.getAttribute("admin") != null) {
                return true;
            }
        }
        response.sendRedirect("/");
        return false;
    }
}
