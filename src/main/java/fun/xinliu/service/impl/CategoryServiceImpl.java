package fun.xinliu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.xinliu.common.CustomException;
import fun.xinliu.entity.Category;
import fun.xinliu.entity.Dish;
import fun.xinliu.entity.Setmeal;
import fun.xinliu.mapper.CategoryMapper;
import fun.xinliu.service.CategoryService;
import fun.xinliu.service.DishService;
import fun.xinliu.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public void removeCategory(Long id) {

        // 先查询Dish表中指定id的菜品数量
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Dish::getCategoryId, id);
        long dishInvolved = dishService.count(lqw);

        if(dishInvolved > 0) {
            throw new CustomException("指定id的Category有关联菜品，无法删除");
        }

        LambdaQueryWrapper<Setmeal> lqw2 = new LambdaQueryWrapper<>();
        lqw2.eq(Setmeal::getCategoryId, id);
        long setMealInvolved = setmealService.count(lqw2);

        if(setMealInvolved > 0) {
            throw new CustomException("指定id的Category有关联套餐，无法删除");
        }

        this.removeById(id);
    }
}
