package com.kantboot.system.user.module.repository;

import com.kantboot.system.user.module.entity.SysSetting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface SysSettingRepository extends Repository<SysSetting,Long>, CrudRepository<SysSetting,Long>
{
}
