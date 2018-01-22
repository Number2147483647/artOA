package site.binghai.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import site.binghai.crm.dao.AdminDao;
import site.binghai.crm.entity.Admin;

import java.util.List;

/**
 * Created by binghai on 2018/1/22.
 *
 * @ artOA
 */
@Service
public class AdminService {
    private static Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private AdminDao adminDao;

    /**
     * 验证登录
     */
    public Admin adminLogin(Admin admin) {
        if (admin != null && !StringUtils.isEmpty(admin.getUsername()) && !StringUtils.isEmpty(admin.getPassword())) {
            List<Admin> ls = adminDao.findByPhoneAndPassword(admin.getUsername(), admin.getPassword());
            if (CollectionUtils.isEmpty(ls) || ls.size() > 1) {
                logger.error("登录校验失败:{},rs = {}", admin, ls);
                return null;
            }
            return ls.get(0);
        }
        return null;
    }
}
