package fun.xinliu.controller;

import fun.xinliu.common.Result;
import fun.xinliu.entity.Orders;
import fun.xinliu.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @Transactional
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders){
        log.info("下单...数据为, {}", orders);
        ordersService.submit(orders);
        return Result.success("下单成功");
    }


}
