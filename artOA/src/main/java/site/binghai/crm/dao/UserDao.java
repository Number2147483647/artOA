package site.binghai.crm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import site.binghai.crm.entity.User;

/**
 * Created by binghai on 2018/1/22.
 *
 * @ artOA
 */
public interface UserDao extends JpaRepository<User,Integer> {
}
