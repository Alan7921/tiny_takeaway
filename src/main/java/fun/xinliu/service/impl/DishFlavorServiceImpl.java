package fun.xinliu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.xinliu.entity.DishFlavor;
import fun.xinliu.mapper.DishFlavorMapper;
import fun.xinliu.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
