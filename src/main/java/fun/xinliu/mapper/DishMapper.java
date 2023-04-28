package fun.xinliu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.xinliu.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
