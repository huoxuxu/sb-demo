package com.hxx.sbweb.service;

import com.hxx.sbweb.domain.User;
import com.hxx.sbweb.mapper.UserXmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserXmlService {
    @Autowired
    private UserXmlMapper userxmlMapper;


    public List<User> selectAll() {
        return userxmlMapper.selectAll();
    }

    public User selectUser(String name){
        return userxmlMapper.selectUser(name);
    }

    /**
     * map参数
     * @param map
     * @return
     */
    public List<User> selectUserByMap(Map<String,Object> map)
    {
//        LimitModel model=new LimitModel();
//        Integer min = (Integer) map.get("min");
//        model.setMin(min);
//        Integer max = (Integer) map.get("max");
//        model.setMax(max);

        return userxmlMapper.selectUserByMap(map);
    }

    public List<User> selectAll2()
    {
        return userxmlMapper.selectAll2();
    }


}
