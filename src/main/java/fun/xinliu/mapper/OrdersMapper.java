package fun.xinliu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.xinliu.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
