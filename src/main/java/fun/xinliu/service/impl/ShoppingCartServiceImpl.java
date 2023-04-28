package fun.xinliu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.xinliu.entity.ShoppingCart;
import fun.xinliu.mapper.ShoppingCartMapper;
import fun.xinliu.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
