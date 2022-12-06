package com.hxx.sbConsole.service.impl.fastjson;

import com.hxx.sbcommon.common.json.FastJsonReaderQuick;
import com.hxx.sbcommon.common.json.JsonUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-03 17:11:00
 **/
@Service
public class FastjsonServiceImpl {

    public static void demo() {
        try {
            String path = "d:/demo.json";
            long start = System.currentTimeMillis();
            parse3(path);
            System.out.println("耗时：" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            System.out.println(e + "");
        }
    }

    static void parse1(String path) {
        try {
            String jsonArrStr = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
            List<DemoCls> ls = JsonUtil.parseArray(jsonArrStr, DemoCls.class);
            DemoCls fir = ls.get(0);
            System.out.println(JsonUtil.toJSON(fir));
            System.out.println("共：" + ls.size());
        } catch (Exception e) {
            System.out.println(e + "");
        }
    }

    // 取第一行JSONObject
    static void parse2(String path) {
        try {
            FileReader fileReader = FastJsonReaderQuick.getFileReader(path);
            try (FastJsonReaderQuick jsu = new FastJsonReaderQuick(fileReader)) {
                List<DemoCls> ls = new ArrayList<>();

                jsu.parsePOJOArr((ind, m) -> {
                    if (ind > 0) {
                        return null;
                    }

                    DemoCls d = new DemoCls();
                    for (Map.Entry<String, Object> entry : m.entrySet()) {
                        String k = entry.getKey();
                        Object v = entry.getValue();

                        switch (k) {
                            case "id":
                                if (v instanceof Integer) {
                                    d.setId((Integer) v);
                                }
                                break;
                            case "code":
                                if (v instanceof String) {
                                    d.setCode((String) v);
                                }
                                break;
                            case "name":
                                if (v instanceof String) {
                                    d.setName((String) v);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    return d;
                });

                DemoCls fir = ls.get(0);
                System.out.println(JsonUtil.toJSON(fir));

                int total = ls.size();
                System.out.println("共：" + total);
            }
        } catch (Exception e) {
            System.out.println(e + "");
        }
    }

    // 分批取
    static void parse3(String path) {
        try {
            FileReader fileReader = FastJsonReaderQuick.getFileReader(path);
            try (FastJsonReaderQuick jsu = new FastJsonReaderQuick(fileReader)) {
                // 分页数据
                List<DemoCls> data = new ArrayList<>(2000);

                AtomicInteger total = new AtomicInteger();
                jsu.parsePOJOArr((ind, modelMap) -> {
                    total.getAndIncrement();
                    if (ind > 0 && ind % 2000 == 0) {
                        // TODO：处理 data 中的数据
                        System.out.println("开始处理data数据：" + data.size());
                        data.clear();
                    }

                    DemoCls d = convert2(modelMap);
                    data.add(d);
                    return d;
                });

                if (!CollectionUtils.isEmpty(data)) {
                    // TODO：处理 data 中的数据
                    System.out.println("开始处理data数据：" + data.size());
                    data.clear();
                }

                System.out.println("共：" + total);
            }
        } catch (Exception e) {
            System.out.println(e + "");
        }
    }

    private static DemoCls convert2(Map<String, Object> modelMap) {
        DemoCls d = new DemoCls();
        for (Map.Entry<String, Object> entry : modelMap.entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();

            switch (k) {
                case "id":
                    if (v instanceof Integer) {
                        d.setId((Integer) v);
                    }
                    break;
                case "code":
                    if (v instanceof String) {
                        d.setCode((String) v);
                    }
                    break;
                case "name":
                    if (v instanceof String) {
                        d.setName((String) v);
                    }
                    break;
                default:
                    break;
            }
        }
        return d;
    }

    public static void main(String[] args) {
        FastjsonServiceImpl.demo();
        System.out.println("ok1");
        Scanner input = new Scanner(System.in);
        input.next();
        System.out.println("ok");
    }

    @lombok.Data
    public static class DemoCls {
        private int id;
        private String code;
        private String name;
    }
}
