package com.hxx.sbweb.service;

import com.hxx.sbweb.domain.Teacher;
import com.hxx.sbweb.mapper.TeacherXmlMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherXmlService {
    @Autowired
    private TeacherXmlMapper xmlMapper;

    public Teacher getbyID(int id)
    {
        return xmlMapper.getbyID(id);
    }

}
