package site.binghai.crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import site.binghai.crm.interceptor.LoginInter;

/**
 * Created by binghai on 2018/1/22.
 *
 * @ artOA
 */
@Component
public class MvcConfig extends WebMvcConfigurerAdapter{
    @Bean
    public LoginInter loginInter(){
        return new LoginInter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInter()).addPathPatterns("/admin/**");
        super.addInterceptors(registry);
    }
}
