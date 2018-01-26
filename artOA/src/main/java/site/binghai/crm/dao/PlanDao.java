package site.binghai.crm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import site.binghai.crm.entity.Plan;

/**
 * Created by binghai on 2018/1/25.
 *
 * @ artOA
 */
public interface PlanDao extends JpaRepository<Plan, Integer> {
}
