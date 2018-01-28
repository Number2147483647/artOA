package site.binghai.crm.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import site.binghai.crm.utils.HttpRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by binghai on 2018/1/27.
 *
 * @ artOA
 */
@Component
public class UserInter extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session != null) {
            if (session.getAttribute("user") != null) {
                return true;
            }
        }
        String backUrl = HttpRequestUtils.getRequestFullPath(request);
        session.setAttribute("back",backUrl);
        response.sendRedirect("http://weixin.qdxiaogutou.com/login.php?backUrl=http://art.nanayun.cn:8080/wx/login");
        return false;
    }
}
