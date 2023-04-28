package fun.xinliu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.xinliu.entity.Employee;
import fun.xinliu.mapper.EmployeeMapper;
import fun.xinliu.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
