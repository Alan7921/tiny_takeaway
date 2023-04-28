package fun.xinliu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.xinliu.dto.SetmealDto;
import fun.xinliu.entity.Dish;
import fun.xinliu.entity.Setmeal;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {
    public void saveSetmealWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);
}
