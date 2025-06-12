package org.gsh.genidxpage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.gsh.genidxpage.entity.ArchiveStatus;

import java.util.List;

@Mapper
public interface ArchiveStatusMapper {

    Long insert(ArchiveStatus report);

    ArchiveStatus selectByYearMonth(@Param("year") String year, @Param("month") String month);

    void update(ArchiveStatus report);

    List<ArchiveStatus> selectByPageExists(Boolean pageExists);

    List<ArchiveStatus> selectAllFailed();
}
