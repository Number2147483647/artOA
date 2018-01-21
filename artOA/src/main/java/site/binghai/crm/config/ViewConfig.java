package site.binghai.crm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by binghai on 2018/1/20.
 *
 * @ artOA
 */
@Configuration
public class ViewConfig extends WebMvcConfigurerAdapter {
    /**
     * 无逻辑处理，直接url -> page 的映射关系
     * */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/users.html").setViewName("users");
        registry.addViewController("/kq.html").setViewName("kq");
        registry.addViewController("/adminKq.html").setViewName("adminKq");
        registry.addViewController("/printer.html").setViewName("printer");
        registry.addViewController("/tags.html").setViewName("tags");
        registry.addViewController("/fields.html").setViewName("fields");
        registry.addViewController("/sys.html").setViewName("sys");
        super.addViewControllers(registry);
    }
}
