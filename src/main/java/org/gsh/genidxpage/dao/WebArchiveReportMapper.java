package org.gsh.genidxpage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.gsh.genidxpage.entity.ArchivedPageUrlReport;

@Mapper
public interface WebArchiveReportMapper {

    Long insertReport(ArchivedPageUrlReport report);

    ArchivedPageUrlReport selectReportByYearMonth(@Param("year") String year, @Param("month") String month);
}
