package com.kantboot.file.module.repository;

import com.kantboot.file.module.entity.KfmFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CesFileRepository extends
        CrudRepository<KfmFile, Long>,
        Repository<KfmFile, Long> {

    List<KfmFile> findByFileParentBodyNameAndFileParentBodyFieldAndName(
            @Param("FileParentBodyName") String fileParentBodyName,
            @Param("FileParentBodyField") String fileParentBodyField,
            @Param("name") String name);

}
