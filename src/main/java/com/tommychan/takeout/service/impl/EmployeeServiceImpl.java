package com.tommychan.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tommychan.takeout.entity.Employee;
import com.tommychan.takeout.mapper.EmployeeMapper;
import com.tommychan.takeout.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
