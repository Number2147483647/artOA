package site.binghai.crm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import site.binghai.crm.entity.Admin;

import java.util.List;

/**
 * Created by binghai on 2018/1/22.
 *
 * @ artOA
 */
public interface AdminDao extends JpaRepository<Admin,Integer>{
    List<Admin> findByPhoneAndPassword(String phone,String pass);
}
