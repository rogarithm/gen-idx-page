package org.gsh.genidxpage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.gsh.genidxpage.entity.ArchivedPageUrlReport;

@Mapper
public interface WebArchiveReportMapper {

    Long insertReport(ArchivedPageUrlReport report);
}
