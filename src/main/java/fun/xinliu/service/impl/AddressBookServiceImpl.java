package fun.xinliu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.xinliu.entity.AddressBook;
import fun.xinliu.mapper.AddressBookMapper;
import fun.xinliu.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
