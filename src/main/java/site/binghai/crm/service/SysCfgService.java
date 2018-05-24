package site.binghai.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.binghai.crm.dao.SysCfgDao;
import site.binghai.crm.entity.SysCfg;

import java.util.List;

/**
 * Created by Administrator on 2018/4/29.
 *
 * @ artOA
 */
@Service
public class SysCfgService {
    @Autowired
    private SysCfgDao dao;

    public long howLongSystemRunning(){
        List<SysCfg> cfgs = dao.findAll();
        SysCfg cfg = cfgs.get(0);

        long howlong = System.currentTimeMillis() - cfg.getStartUpTime();
        long days = howlong/86400000;
        return days;
    }

    public boolean adminDebugOpen(){
        List<SysCfg> cfgs = dao.findAll();
        SysCfg cfg = cfgs.get(0);
        return cfg.isAdminDebug();
    }
}
