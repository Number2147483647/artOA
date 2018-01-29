package site.binghai.crm.entity;

import lombok.Data;
import site.binghai.crm.utils.MD5;

/**
 * Created by binghai on 2018/1/24.
 *
 * @ artOA
 */
@Data
public class Schema {
    private String name;
    private String code;
    private Object value;

    public Schema(String name) {
        this.name = name;
        this.code = MD5.shortMd5(name);
    }

    public Schema() {
    }
}
