package site.binghai.crm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import site.binghai.crm.entity.Admin;
import site.binghai.crm.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2018/4/25.
 *
 * @ artOA
 */
public interface AdminDao extends JpaRepository<Admin,Integer>{
    List<Admin> findByPhoneAndPassword(String phone,String pass);
    List<Admin> findByPhone(String p);
}
