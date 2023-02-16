package com.tommychan.takeout.dto;

import com.tommychan.takeout.entity.User;
import lombok.Data;

@Data
public class UserDto extends User {

    private String code;

}
