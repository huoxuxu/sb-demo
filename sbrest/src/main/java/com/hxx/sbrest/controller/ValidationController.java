package com.hxx.sbrest.controller;

import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import com.hxx.sbrest.model.vo.UserVo;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-29 14:57:52
 **/
@RestController
@RequestMapping("/valid")
public class ValidationController {
    /**
     *
     * @param vo
     * @param result
     * @return
     */
    @GetMapping("/got")
    public String got(@Validated UserVo vo, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<String> errs = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).collect(Collectors.toList());

            for (String s : errs) {
                stringBuilder.append(s);
            }
            return stringBuilder.toString();
        }

        String s = JsonUtil.toJSON(vo);
        return s;
    }

    /**
     * /valid/vot
     * @param vo
     * @return
     */
    @RequestMapping("/vot")
    public String vot(@Validated @RequestBody UserVo vo, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<String> errs = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).collect(Collectors.toList());

            for (String s : errs) {
                stringBuilder.append(s);
            }
            return stringBuilder.toString();
        }

        String s = JsonUtil.toJSON(vo);
        return s;
    }


}
