package com.aaarfyh.ranfa.module.repository;

import com.aaarfyh.ranfa.module.entity.BusRanfaWorkVideo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;


public interface BusRanfaWorkVideoRepository
        extends CrudRepository<BusRanfaWorkVideo,Long>, Repository<BusRanfaWorkVideo,Long> {

    BusRanfaWorkVideo findByFileIdOfVideo(@Param("fileIdOfVideo")Long fileIdOfVideo);
}
