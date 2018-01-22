package site.binghai.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by binghai on 2018/1/22.
 *
 * @ artOA
 */
@RequestMapping("admin")
@Controller
public class IndexController extends BaseController {

    @RequestMapping("index")
    public String index(ModelMap map){

        return "index";
    }
}
