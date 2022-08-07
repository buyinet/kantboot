package com.kantboot.system.user.module.vo;


import com.kantboot.system.user.module.entity.SysUser;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginVO {

    private String username;

    private String token;

    private SysUser user;

}
