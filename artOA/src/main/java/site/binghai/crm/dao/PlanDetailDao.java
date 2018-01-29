package site.binghai.crm.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.binghai.crm.entity.PlanDetail;

import java.util.List;

/**
 * Created by binghai on 2018/1/28.
 *
 * @ artOA
 */
public interface PlanDetailDao extends JpaRepository<PlanDetail,Integer> {

    List<PlanDetail> findByUserIdAndPlanId(int userId,int planId);
    List<PlanDetail> findByPlanId(int planId);
    List<PlanDetail> findByPlanIdOrderByIdDesc(int planId, Pageable pageable);
    List<PlanDetail> findByCreatedTimeLike(String like);
}
