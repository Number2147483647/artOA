package site.binghai.crm.service;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import site.binghai.crm.dao.UserDao;
import site.binghai.crm.entity.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/4/22.
 *
 * @ artOA
 */
@Service
public class UserService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private ErrLogService errLogService;

    @Autowired
    private UserDao userDao;


    public List<User> findAll() {
        return userDao.findAll().stream()
                .filter(v -> !v.isDeleted())
                .sorted((a, b) -> b.getId() - a.getId())
                .collect(Collectors.toList());
    }

    public List<User> filter(String f) {
        return findAll().stream()
                .filter(v ->
                        v.getName().contains(f) ||
                                v.getPhone().contains(f) ||
                                v.getInfo().contains(f)
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public void save(User user) {
        userDao.save(user);
    }

    public User findByOpenId(String openid) {
        List<User> users = userDao.findByOpenId(openid);

        users = users.stream().filter(v -> !v.isDeleted()).collect(Collectors.toList());

        if (users == null || users.size() == 0) {
            logger.error("{}没有找到对应用户，请先绑定", openid);
            return null;
        }

        if (users.size() > 1) {
            errLogService.log(this.getClass(), "findByOpenId", openid + "存在多个绑定:" + JSONObject.toJSONString(users));
            logger.error("{}存在多个绑定，错误!{}", openid, users);
            return null;
        }

        return users.get(0);
    }

    public User findByNameAndPhone(String name, String phone) {
        List<User> users = userDao.findByNameAndPhone(name, phone);

        users = users.stream().filter(v -> !v.isDeleted()).collect(Collectors.toList());

        if (users == null || users.size() == 0) {
//            errLogService.log(this.getClass(), "findByNameAndPhone", "姓名和电话绑定时没有找到对应用户" + name + "," + phone);
            logger.error("姓名和电话绑定时没有找到对应用户，请检查数据!{}{}", name, phone);
            return null;
        }

        if (users.size() > 1) {
//            errLogService.log(this.getClass(), "findByNameAndPhone", "存在多个记录无法绑定" + name + "," + phone + ":" + JSONObject.toJSONString(users));
            logger.error("{},{}存在多个记录，无法绑定!{}", name, phone, users);
            return null;
        }
        return users.get(0);
    }

    public List<User> findByUserIds(List<Integer> userIds) {
        List<User> t = userDao.findAll(userIds);
        t = t.stream().filter(v -> !v.isDeleted()).collect(Collectors.toList());
        t.sort((a, b) -> b.getId() - a.getId());
        return t;
    }

    public User findOne(int userId) {
        User user = userDao.findOne(userId);
        return user.isDeleted() ? null : user;
    }

    public User findByQrCode(String userCode) {
        List<User> t = userDao.findByQrCode(userCode);
        t = t.stream().filter(v -> !v.isDeleted()).collect(Collectors.toList());
        return CollectionUtils.isEmpty(t) ? null : t.get(0);
    }

    @Transactional
    public void del(int uid) {
        User user = findOne(uid);
        if (user != null && !user.isDeleted()) {
            user.setDeleted(true);
            save(user);
        }
    }

    public User findByName(String name) throws Exception {
        List<User> users = findAll().stream()
                .filter(v -> !v.isDeleted())
                .filter(v -> v.getName().equals(name)).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(users)) {
            throw new Exception("用户不存在!");
        }

        if (users.size() > 1) {
            throw new Exception("存在同名用户，请扫码打卡!");
        }

        return users.get(0);
    }
}
