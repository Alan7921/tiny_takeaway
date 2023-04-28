package fun.xinliu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.xinliu.entity.OrderDetail;
import fun.xinliu.mapper.OrderDetailMapper;
import fun.xinliu.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
