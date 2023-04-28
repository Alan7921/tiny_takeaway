package fun.xinliu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.xinliu.common.CustomException;
import fun.xinliu.dto.SetmealDto;
import fun.xinliu.entity.Setmeal;
import fun.xinliu.entity.SetmealDish;
import fun.xinliu.mapper.SetmealMapper;
import fun.xinliu.service.SetmealDishService;
import fun.xinliu.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    SetmealDishService setmealDishService;

    @Transactional
    public void saveSetmealWithDish(SetmealDto setmealDto){
        // 保存套餐信息
        this.save(setmealDto);
        // 保存关联的SetmealDish信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        Long id = setmealDto.getId();

        // 因为setmealDishes中没有对应的setmealId信息，所以需要手动填充
        for (SetmealDish setMealDish: setmealDishes) {
            setMealDish.setSetmealId(id);
        }

        setmealDishService.saveBatch(setmealDishes);
    }

    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        // 在删除菜品数据前先校验套餐状态
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper();
        lqw.in(ids != null, Setmeal::getId, ids);
        lqw.eq(Setmeal::getStatus, 1);
        long count = this.count(lqw);
        if(count > 0) {
            throw new CustomException("处于在售状态的套餐无法删除");
        }

        // 删除菜品
        this.removeBatchByIds(ids);

        // 删除菜品关联的口味数据
        LambdaUpdateWrapper<SetmealDish> luw = new LambdaUpdateWrapper();
        luw.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(luw);
    }


}
