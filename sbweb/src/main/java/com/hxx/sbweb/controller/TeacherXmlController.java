package com.hxx.sbweb.controller;

import com.hxx.sbcommon.model.Result;
import com.hxx.sbweb.domain.Teacher;
import com.hxx.sbweb.domain.User;
import com.hxx.sbweb.service.TeacherXmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teacherxml")
public class TeacherXmlController {
    @Autowired
    private TeacherXmlService service;

    /**
     * http://localhost:8082/teacherxml/get?id=1
     * @return
     */
    @RequestMapping("/get")
    public Result<Teacher> getbyID(Integer id) {
        Teacher teacher = service.getbyID(id);
        return Result.success(teacher);
    }


}
