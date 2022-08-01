package com.kantboot.system.user.vo;


import com.kantboot.system.user.entity.SysUser;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginVO {

    private String username;

    private String token;

    private SysUser user;

}
