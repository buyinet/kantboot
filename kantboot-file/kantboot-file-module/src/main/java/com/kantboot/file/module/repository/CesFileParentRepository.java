package com.kantboot.file.module.repository;

import com.kantboot.file.module.entity.KfmFileParent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface CesFileParentRepository
        extends
        CrudRepository<KfmFileParent, Long>,
        Repository<KfmFileParent, Long> {

    KfmFileParent findByBodyNameAndBodyField(
            @Param("bodyName") String bodyName,
            @Param("bodyField") String bodyField);

}
