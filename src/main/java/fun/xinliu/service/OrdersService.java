package fun.xinliu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.xinliu.entity.Orders;

public interface OrdersService extends IService<Orders> {

    public void submit(Orders orders);
}
