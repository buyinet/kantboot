package com.aaarfyh.ranfa.module.repository;

import com.aaarfyh.ranfa.module.entity.BusRanfaTechnique;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;


public interface BusRanfaTechniqueRepository
        extends CrudRepository<BusRanfaTechnique,Long>, Repository<BusRanfaTechnique,Long> {

}
