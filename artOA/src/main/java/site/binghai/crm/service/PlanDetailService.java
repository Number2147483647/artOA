package site.binghai.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import site.binghai.crm.dao.PlanDetailDao;
import site.binghai.crm.entity.PlanDetail;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by binghai on 2018/1/28.
 *
 * @ artOA
 */
@Service
public class PlanDetailService {
    @Autowired
    private PlanDetailDao detailDao;


    public PlanDetail findByUserIdAndPlanId(int userId,int planId){
        List<PlanDetail> planDetails = detailDao.findByUserIdAndPlanId(userId,planId);
        return CollectionUtils.isEmpty(planDetails) ? null : planDetails.get(0);
    }

    @Transactional
    public void save(PlanDetail planDetail) {
        detailDao.save(planDetail);
    }
}
