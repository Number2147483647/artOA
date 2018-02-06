package site.binghai.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.binghai.crm.dao.FieldDao;
import site.binghai.crm.entity.Admin;
import site.binghai.crm.entity.Fields;
import site.binghai.crm.utils.TimeFormatter;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by binghai on 2018/1/22.
 *
 * @ artOA
 */
@Service
public class FieldService {
    @Autowired
    private FieldDao fieldDao;

    public List<Fields> findAll() {
        return fieldDao.findAll().stream().filter(v -> !v.isDeleted()).collect(Collectors.toList());
//        return fieldDao.findAll().stream().sorted((a, b) -> b.getId() - a.getId()).collect(Collectors.toList());
    }

    public boolean exist(String addField) {
        List<Fields> all = fieldDao.findAll();
        for (Fields f : all) {
            if (f.getName().equals(addField.trim())) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void delete(Admin admin, int id) {
        Fields field = fieldDao.findOne(id);
        if (field != null) {
            field.setDeleted(true);
            field.setKiller(admin.getUsername());
            field.setKillTime(TimeFormatter.format(System.currentTimeMillis()));
            fieldDao.save(field);
        }
    }

    public void save(Fields fields) {
        fieldDao.save(fields);
    }
}
