package fun.xinliu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.xinliu.dto.DishDto;
import fun.xinliu.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    // 新增菜品的同时插入菜品对应的口味数据，需要操作两张表
    public void saveDishWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

    public void deleteWithFlavor(List<Long> ids);
}
