package fun.xinliu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.xinliu.common.CustomException;
import fun.xinliu.common.Result;
import fun.xinliu.dto.DishDto;
import fun.xinliu.entity.Dish;
import fun.xinliu.entity.DishFlavor;
import fun.xinliu.mapper.DishMapper;
import fun.xinliu.service.DishFlavorService;
import fun.xinliu.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    @Transactional
    public void saveDishWithFlavor(DishDto dishDto) {
        // 因为dishDto继承了Dish类，所以可以直接存入表中
        this.save(dishDto);

        // 下一步是将DishFlavor存入对应的表中，但是Dto中的flavors缺少DishId数据，所以取出来，然后依次存入
        Long id = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

        for (DishFlavor flavor: flavors) {
            flavor.setDishId(id);
        }
        // 最后批量存入DishFlavor数据
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {

        // 查询菜品基本信息
        Dish dish = this.getById(id);

        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId, dish.getId());

        List<DishFlavor> list = dishFlavorService.list(lqw);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        dishDto.setFlavors(list);

        return dishDto;
    }
    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        // 更新dish表基本信息
        this.updateById(dishDto);

        // 清理当前菜品对应的口味数据，——dish_flavor表的删除操作
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(lqw);

        // 从当前dishDto中获得flavor数据，insert到dish_flavor表汇总
        List<DishFlavor> flavors = dishDto.getFlavors();

        for (DishFlavor flavor: flavors) {
            flavor.setDishId(dishDto.getId());
        }

        dishFlavorService.saveBatch(flavors);


    }

    @Transactional
    @Override
    public void deleteWithFlavor(List<Long> ids) {
        // 在删除菜品数据前先校验菜品状态
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper();
        lqw.in(ids != null, Dish::getId, ids);
        lqw.eq(Dish::getStatus, 1);
        long count = this.count(lqw);
        if(count > 0) {
            throw new CustomException("处于在售状态的菜品无法删除");
        }

        // 删除菜品
        this.removeBatchByIds(ids);

        // 删除菜品关联的口味数据
        LambdaUpdateWrapper<DishFlavor> luw = new LambdaUpdateWrapper();
        luw.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(luw);
    }
}
