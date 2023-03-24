package com.hxx.sbConsole.model.validate;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-23 12:37:30
 **/
@lombok.Data
public class UserModel implements Serializable {
    private static final long serialVersionUID = -8540518866223923355L;

    //    @Min(value = 1)//, groups = Update.class
    @Min(value = 1, message = "必须大于等于1")
    @Max(value = 5, message = "必须小于等于5")
    @Range(min = 1, max = 5, message = "必须大于等于1且小于等于5")
    private int id;

    @NotBlank(message = "不能为空")
//    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机号码格式错误")
    @Length(min = 3, max = 6, message = "长度必须在3-6之间")// 被注释的字符串的大小必须在指定的范围内
    private String code;

    @NotNull(message = "不能为空")
    private LocalDateTime birthday;

    @Range(min = 0, max = 100, message = "必须大于等于0且小于等于100")
    private float score;

    @AssertTrue //被注释的元素必须为true
//    @AssertFalse //被注释的元素必须为false
    private boolean enabled;

}
