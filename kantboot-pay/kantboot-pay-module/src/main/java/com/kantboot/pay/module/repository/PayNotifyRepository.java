package com.kantboot.pay.module.repository;

import com.kantboot.pay.module.entity.PayNotify;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface PayNotifyRepository extends Repository<PayNotify,Long>, CrudRepository<PayNotify,Long> {

}
