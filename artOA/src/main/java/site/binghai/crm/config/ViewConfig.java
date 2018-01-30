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
        registry.addViewController("/").setViewName("login");
//        registry.addViewController("/admin/index.html").setViewName("index");
//        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/admin/users.html").setViewName("users");
        registry.addViewController("/admin/kq.html").setViewName("kq");
        registry.addViewController("/admin/adminKq.html").setViewName("adminKq");
        registry.addViewController("/admin/printer.html").setViewName("printer");
        registry.addViewController("/admin/tags.html").setViewName("tags");
        registry.addViewController("/admin/fields.html").setViewName("fields");
        registry.addViewController("/admin/sys.html").setViewName("sys");
        registry.addViewController("/autoClose").setViewName("autoClose");
        super.addViewControllers(registry);
    }
}
