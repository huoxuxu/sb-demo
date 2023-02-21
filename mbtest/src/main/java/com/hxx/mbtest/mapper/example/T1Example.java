package com.hxx.mbtest.mapper.example;

import com.hxx.mbtest.common.SelectExample;
import com.hxx.mbtest.common.WhereCase;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-07-01 9:37:56
 **/
@Data
public class T1Example extends SelectExample {
    private List<Integer> ids;
    private String code;
    private String name;
    private Integer enabled;
    private LocalDateTime birthdayStart;
    private LocalDateTime birthdayEnd;

    public T1Example addIds() {
        whereCases.add(WhereCase.createListValue("id in", ids));
        return this;
    }

    public T1Example add11() {
        whereCases.add(WhereCase.createNoValue("1 =1"));
        return this;
    }

    public T1Example addCode() {
        whereCases.add(WhereCase.createSingleValue("code =", code));
        return this;
    }

    public T1Example addName() {
        whereCases.add(WhereCase.createSingleValue("name =", name));
        return this;
    }

    public T1Example addEnabled() {
        whereCases.add(WhereCase.createSingleValue("enabled =", enabled));
        return this;
    }

    public T1Example addBirthday() {
        whereCases.add(WhereCase.createBetween("birthday BETWEEN ", birthdayStart, birthdayEnd));
        return this;
    }

}
