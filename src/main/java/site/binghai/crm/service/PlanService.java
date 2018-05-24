package site.binghai.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import site.binghai.crm.dao.PlanDao;
import site.binghai.crm.entity.Plan;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/4/25.
 *
 * @ artOA
 */
@Service
public class PlanService {

    @Autowired
    private PlanDetailService planDetailService;
    @Autowired
    private PlanDao planDao;

    @Transactional
    public void save(Plan plan) {
        planDao.save(plan);
    }

    public List<Plan> all() {
        return planDao.findAll().stream()
                .filter(v -> !v.isDeleted())
                .sorted((a, b) -> b.getId() - a.getId())
                .peek(v-> v.setNowSize(getNowSize(v)))
                .collect(Collectors.toList());
    }

    private int getNowSize(Plan plan) {
        return planDetailService.findByPlanId(plan.getId()).size();
    }

    public Plan findById(Integer id) {
        Plan plan = planDao.findOne(id);
        if(null != plan){
            plan.setNowSize(getNowSize(plan));
        }
        return plan;
    }

    public Plan findByQrCode(String qrCode) {
        List<Plan> t = planDao.findByQrPass(qrCode);
        return CollectionUtils.isEmpty(t) ? null : t.get(0);
    }
}
