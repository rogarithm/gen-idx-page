package org.gsh.genidxpage.service;

import org.gsh.genidxpage.dao.IndexContentMapper;
import org.gsh.genidxpage.vo.IndexContent;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IndexContentReader {

    private final IndexContentMapper mapper;

    public IndexContentReader(IndexContentMapper mapper) {
        this.mapper = mapper;
    }

    public List<String> readAllIndexContent() {
        return mapper.selectAll()
            .stream()
            .map(IndexContent::toString)
            .toList();
    }
}
