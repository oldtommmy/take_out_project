package com.tommychan.takeout.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tommychan.takeout.common.BaseContext;
import com.tommychan.takeout.common.R;
import com.tommychan.takeout.entity.Employee;
import com.tommychan.takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,employee.getUsername());
        Employee one = employeeService.getOne(wrapper);
        if (one == null) {
            return R.error("不存在此用户，登录失败");
        }

        if (!one.getPassword().equals(password)){
            return R.error("密码错误，登录失败");
        }

        if (one.getStatus() == 0) {
            return R.error("账号已被禁用，请联系管理员");
        }
        BaseContext.setCurrentId(one.getId());
        request.getSession().setAttribute("employee",one.getId());
        return R.success(one);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("employee");
        return R.success("注销成功");
    }

    @PostMapping
    public R<String> save(@RequestBody Employee employee) {
        log.info("新增员工，员工信息：{}", employee);

        //设置初始密码 123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        //获得当前登录的管理员的 id
//        Long id = (Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(id);
//        employee.setUpdateUser(id);

        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page:{} pageSize:{} name:{}", page, pageSize, name);

        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序规则
        queryWrapper.orderByDesc(Employee::getCreateTime);

        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据 id 修改员工信息
     * @param employee employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新的成员信息：{}", employee);
        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        System.out.println(employee);
        employeeService.updateById(employee);
        return R.success("更新成功");
    }

    /**
     * 根据 id 查询员工信息
     * @param id id
     * @return R
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("id:{}", id);
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }

}
