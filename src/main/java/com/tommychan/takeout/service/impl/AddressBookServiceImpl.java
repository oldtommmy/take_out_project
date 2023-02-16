package com.tommychan.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tommychan.takeout.entity.AddressBook;
import com.tommychan.takeout.mapper.AddressBookMapper;
import com.tommychan.takeout.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
