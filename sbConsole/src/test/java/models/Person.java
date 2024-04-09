package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-06-08 9:21:37
 **/
@Data
public class Person {
    private long id;
    private String name;
    private String sex;
    private int age;
    private BigDecimal score;
    private boolean alive;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date birthday;

}
