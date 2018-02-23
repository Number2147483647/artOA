package site.binghai.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import site.binghai.crm.dao.PlanDetailDao;
import site.binghai.crm.entity.PlanDetail;
import site.binghai.crm.utils.TimeFormatter;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by binghai on 2018/1/28.
 *
 * @ artOA
 */
@Service
public class PlanDetailService {
    @Autowired
    private PlanDetailDao detailDao;


    public PlanDetail findByUserIdAndPlanId(int userId, int planId) {
        List<PlanDetail> planDetails = detailDao.findByUserIdAndPlanId(userId, planId)
                .stream().filter(v -> !v.isDeleted()).collect(Collectors.toList());
        return CollectionUtils.isEmpty(planDetails) ? null : planDetails.get(0);
    }

    @Transactional
    public PlanDetail save(PlanDetail planDetail) {
        return detailDao.save(planDetail);
    }

    public List<PlanDetail> findByPlanId(int planId) {
        return detailDao.findByPlanId(planId)
                .stream().filter(v -> !v.isDeleted())
                .collect(Collectors.toList());
    }

    public PlanDetail findByPlanIdLimit1(int planId) {
        List<PlanDetail> pds = detailDao.findByPlanIdAndDeletedOrderByIdDesc(planId,false, new PageRequest(0, 1));
        return pds == null || pds.isEmpty() ? null : pds.get(0);
    }

    public List<PlanDetail> todayKq() {
        return detailDao.findByCreatedTimeStartsWith(TimeFormatter.format2yyyy_MM_dd(System.currentTimeMillis()))
                .stream().filter(v -> !v.isDeleted())
                .collect(Collectors.toList());
    }

    public PlanDetail findById(int planDetailId) {
        PlanDetail pd = detailDao.findOne(planDetailId);
        return pd.isDeleted() ? null : pd;
    }
}
