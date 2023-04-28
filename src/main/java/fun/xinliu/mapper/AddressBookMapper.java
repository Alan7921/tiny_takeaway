package fun.xinliu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.xinliu.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
