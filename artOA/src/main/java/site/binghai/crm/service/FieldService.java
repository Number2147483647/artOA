package site.binghai.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.binghai.crm.dao.FieldDao;
import site.binghai.crm.entity.Fields;

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
        return fieldDao.findAll().stream().sorted((a, b) -> b.getId() - a.getId()).collect(Collectors.toList());
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

    public void save(Fields fields) {
        fieldDao.save(fields);
    }
}
