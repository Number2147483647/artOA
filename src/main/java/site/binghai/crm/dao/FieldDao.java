package site.binghai.crm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import site.binghai.crm.entity.Fields;

import java.util.List;

/**
 * Created by Administrator on 2018/4/22.
 *
 * @ artOA
 */
public interface FieldDao extends JpaRepository<Fields,Integer> {

}
