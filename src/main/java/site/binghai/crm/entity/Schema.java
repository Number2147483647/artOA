package site.binghai.crm.entity;

import lombok.Data;
import site.binghai.crm.utils.MD5;

/**
 * Created by Administrator on 2018/4/20.
 *
 * @ artOA
 */
@Data
public class Schema {
    private String name;
    private String code;
    private Object value;
    private boolean extendField;
    private boolean notVisible4User;

    public Schema(String name) {
        this.name = name;
        this.code = MD5.shortMd5(name);
        this.extendField = false;
    }

    public Schema(String name,boolean extendField,boolean notVisible4User) {
        this.name = name;
        this.code = MD5.shortMd5(name);
        this.extendField = extendField;
        this.notVisible4User = notVisible4User;
    }

    public Schema() {
    }
}
