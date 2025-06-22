package org.gsh.genidxpage.service.dto;

public class CheckPostArchivedDto implements CheckPostArchived {

    private final String year;
    private final String month;
    private final String url;
    private final String timestamp;

    public CheckPostArchivedDto(String year, String month) {
        this.year = year;
        this.month = String.format("%02d", Integer.parseInt(month));
        this.url = "https://agile.egloos.com/archives/" + year + "/" + String.format("%02d",
            Integer.parseInt(month));
        this.timestamp = "20230401";
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getUrl() {
        return url;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
