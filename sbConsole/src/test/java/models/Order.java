package models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-09-28 13:02:30
 **/
@Data
public class Order {
    private String str1;
    private String str2;
    private String goodsName;

    public Order(String str1, String str2, String goodsName) {
        this.goodsName = goodsName;
        this.str1 = str1;
        this.str2 = str2;
    }
}
