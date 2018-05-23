package site.binghai.crm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import site.binghai.crm.entity.User;

import java.util.List;

/**
 * Created by binghai on 2018/4/22.
 *
 * @ artOA
 */
public interface UserDao extends JpaRepository<User,Integer> {
    List<User> findByOpenId(String openId);//
    List<User> findByNameAndPhone(String name,String phone);
    List<User> findByQrCode(String qrCode);
}
