package com.tommychan.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tommychan.takeout.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
