package org.gsh.genidxpage.service.dto;

public class EmptyArchivedPageInfo extends ArchivedPageInfo {

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isUnreachable() {
        return false;
    }
}
