package fun.xinliu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fun.xinliu.common.Result;
import fun.xinliu.entity.Employee;
import fun.xinliu.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
// caution here, RestController 注解的value是用于为组件起一个逻辑名称
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * create by: Xin Liu
     * description: 这个控制器方法负责后台系统的登陆和登出功能
     * create time: 2023/4/13 2:42 PM
     *
      * @param employee 封装前端页面请求传入的JSON数据
     * @return fun.xinliu.entity.Employee
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest httpServletRequest,  @RequestBody Employee employee) {

        String username = employee.getUsername();
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,username);
        Employee emp = employeeService.getOne(lqw);

        if(emp == null) {
            return Result.error("该用户名或密码不正确");
        }

        if(!emp.getPassword().equals(password)){
            return Result.error("该用户名或密码不正确");
        }

        if(emp.getStatus() == 0){
            return Result.error("该账户已被禁用，请联系管理员");
        }

        // 记录id用于后续判断用户是否已经登陆
        httpServletRequest.getSession().setAttribute("employee", emp.getId());

        return Result.success(emp);
    }

    /**
     * create by: Xin Liu
     * description: 这个控制器方法主要负责后台管理界面的登出功能
     * create time: 2023/4/13 7:19 PM
     *
     * @param request
     * @return fun.xinliu.common.Result<java.lang.String>
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        // 清理session中保存的当前登陆的员工id
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }


    @PostMapping
    public Result<String> save(HttpServletRequest httpServletRequest, @RequestBody Employee employee) {
        log.info("新增员工： {}", employee.toString());

        Long id = Thread.currentThread().getId();
        log.info("当前线程id为：{}",id);

        // 设置一个初始密码，需要使用md5进行加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        Long empId = (Long)httpServletRequest.getSession().getAttribute("employee");
//
//        employee.setUpdateUser(empId);
//        employee.setCreateUser(empId);

        employeeService.save(employee);
        return Result.success("新增员工成功");
    }

    @GetMapping("/page")
    public Result<Page> page(Integer page, Integer pageSize, String name) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);

        // 声明Page对象
        Page pageInfo = new Page(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<Employee>();

        // 设置query的筛选条件
        lqw.like(Strings.isNotEmpty(name), Employee::getName, name);

        // 设置返回结果的排序
        lqw.orderByDesc(Employee::getUpdateTime);

        // 执行查询
        employeeService.page(pageInfo, lqw);

        return Result.success(pageInfo);
    }

    @PutMapping
    public Result<String> update(HttpServletRequest httpServletRequest, @RequestBody Employee employee) {
        log.info(employee.toString());
//        Long empId = (Long) httpServletRequest.getSession().getAttribute("employee");
//
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);

        employeeService.updateById(employee);

        return Result.success("员工信息修改成功");
    }

    @GetMapping("{id}")
    public Result<Employee> getById(@PathVariable("id") Long id){
        Employee emp = employeeService.getById(id);
        if(emp != null) {
            return Result.success(emp);
        }
        return Result.error("数据同步失败，查询不到指定用户");
    }

}
