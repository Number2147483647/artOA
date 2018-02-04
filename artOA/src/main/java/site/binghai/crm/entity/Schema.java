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
    private boolean extendField;

    public Schema(String name) {
        this.name = name;
        this.code = MD5.shortMd5(name);
        this.extendField = false;
    }

    public Schema(String name,boolean extendField) {
        this.name = name;
        this.code = MD5.shortMd5(name);
        this.extendField = extendField;
    }

    public Schema() {
    }
}
