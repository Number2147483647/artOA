package site.binghai.crm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import site.binghai.crm.entity.Plan;

import java.util.List;

/**
 * Created by binghai on 2018/4/25.
 *
 * @ artOA
 */
public interface PlanDao extends JpaRepository<Plan, Integer> {
    List<Plan> findByQrPass(String pass);
}
