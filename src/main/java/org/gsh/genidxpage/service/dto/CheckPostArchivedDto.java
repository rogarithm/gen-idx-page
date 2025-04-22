package org.gsh.genidxpage.service.dto;

public class CheckPostArchivedDto {

    private final String year;
    private final String month;
    private final String url;
    private final String timestamp;

    public CheckPostArchivedDto(String year, String month) {
        this.year = year;
        this.month = month;
        this.url = "agile.egloos.com/archives/" + year + "/" + String.format("%02d",
            Integer.parseInt(month));
        this.timestamp = "20240101";
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
