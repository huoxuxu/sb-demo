package com.hxx.sbConsole.service.impl.demo.basic;

import com.hxx.sbConsole.model.TypeRelatedBO;
import com.hxx.sbConsole.model.enums.LinePatternEnum;
import com.hxx.sbConsole.model.validate.UserModel;
import com.hxx.sbcommon.common.basic.langType.LangTypeHandlerFactory;
import com.hxx.sbcommon.common.basic.validate.ValidateUtil;
import com.hxx.sbcommon.common.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-23 12:53:17
 **/
@Slf4j
public class DemoBasicService {
    public static void main(String[] args) {
        try {
            case0();
            case1();
            System.out.println("ok!");
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    static void case0() {
        System.out.println("==================================================");
        {
            UserModel u = new UserModel();
            {
                u.setId(1);
                u.setCode("P12345");
                u.setBirthday(LocalDateTime.MIN);
                u.setScore(100F);
                u.setEnabled(false);
            }
            String err = ValidateUtil.validateModel(u);
            System.out.println(err);
        }
    }

    static void case1() {
        List<String> titles = new ArrayList<>();
        {
            titles.add("id");
            titles.add("code");
            titles.add("createTime");
        }
        List<Object[]> rows = new ArrayList<>();
        {
            rows.add(Arrays.asList(1, "hh1", "2023-09-05 12:34:56").toArray());
            rows.add(Arrays.asList(2D, "hh2", "2023-09-04 12:34:56").toArray());
            rows.add(Arrays.asList("3", "hh3", "2023-09-03 12:34:56").toArray());
        }
        List<TypeRelatedBO> relateds = new ArrayList<>();
        {
            relateds.add(new TypeRelatedBO("id", "id", Long.class));
            relateds.add(new TypeRelatedBO("code", "code", String.class));
            relateds.add(new TypeRelatedBO("createTime", "createTime", LocalDateTime.class));
        }
        Map<String, List<TypeRelatedBO>> relatedMap = relateds.stream()
                .collect(Collectors.groupingBy(d -> d.getName()));

        List<Object[]> newRows = new ArrayList<>();
        for (Object[] row : rows) {
            Object[] newRowArr = new Object[row.length];
            for (int i = 0; i < titles.size(); i++) {
                String title = titles.get(i);
                Object val = row[i];

                List<TypeRelatedBO> relatedTypes = relatedMap.get(title);
                // 转换值
                Class relatedType = relatedTypes.get(0).getRelatedType();
                Object convertVal = LangTypeHandlerFactory.convert(val, relatedType);
                newRowArr[i] = convertVal;
            }
            newRows.add(newRowArr);
        }

        System.out.println(JsonUtil.toJSON(newRows));
    }

}
