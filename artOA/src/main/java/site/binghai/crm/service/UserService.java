package site.binghai.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.binghai.crm.dao.UserDao;
import site.binghai.crm.entity.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by binghai on 2018/1/22.
 *
 * @ artOA
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;


    public List<User> findAll() {
        return userDao.findAll().stream()
                .filter(v -> v.isDeleted())
                .sorted((a, b) -> b.getId() - a.getId())
                .collect(Collectors.toList());
    }
}
