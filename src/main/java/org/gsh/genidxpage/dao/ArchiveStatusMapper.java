package org.gsh.genidxpage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.gsh.genidxpage.entity.ArchiveStatus;

import java.util.List;

@Mapper
public interface ArchiveStatusMapper {

    Long insertReport(ArchiveStatus report);

    ArchiveStatus selectReportByYearMonth(@Param("year") String year, @Param("month") String month);

    void updateReport(ArchiveStatus report);

    List<ArchiveStatus> selectByPageExists(Boolean pageExists);
}
