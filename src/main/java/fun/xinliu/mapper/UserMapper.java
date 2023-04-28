package fun.xinliu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.xinliu.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
