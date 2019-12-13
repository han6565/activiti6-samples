package com.imooc.activiti.mapper;

import java.util.List;
import java.util.Map;
import  org.apache.ibatis.annotations.Select;

public interface MyCustomMapper {
    @Select("select * from ACT_RU_TASK")
    public List<Map<String,Object>> findAll();

}
