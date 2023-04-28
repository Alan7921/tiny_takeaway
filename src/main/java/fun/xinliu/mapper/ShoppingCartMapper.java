package fun.xinliu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.xinliu.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
