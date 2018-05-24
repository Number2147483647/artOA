package site.binghai.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.binghai.crm.dao.ErrLogDao;
import site.binghai.crm.entity.ErrLog;
import site.binghai.crm.utils.TimeFormatter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/4/27.
 *
 * @ artOA
 */
@Service
public class ErrLogService {
    @Autowired
    private ErrLogDao dao;


    public void log(Class clazz, String method, String info) {
        ErrLog errLog = new ErrLog(clazz.getSimpleName(), method, TimeFormatter.format(System.currentTimeMillis()), info);

        dao.save(errLog);
    }

    public List<ErrLog> findAll() {
        return dao.findAll().stream().sorted((a, b) -> b.getId() - a.getId()).collect(Collectors.toList());
    }
}
