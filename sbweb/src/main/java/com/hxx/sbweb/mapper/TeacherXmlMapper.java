package com.hxx.sbweb.mapper;

import com.hxx.sbweb.domain.Teacher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeacherXmlMapper {
    //一对多
    Teacher getbyID(int id);

}
