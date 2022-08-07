package com.aaarfyh.ranfa.module.repository;

import com.aaarfyh.ranfa.module.entity.BusRanfaWork;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;


public interface BusRanfaWorkRepository
        extends CrudRepository<BusRanfaWork,Long>, Repository<BusRanfaWork,Long> {

}
