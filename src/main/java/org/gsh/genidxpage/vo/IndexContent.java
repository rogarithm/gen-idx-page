package org.gsh.genidxpage.vo;

public class IndexContent {

    private String year;
    private String month;
    private String rawHtml;

    private IndexContent() {}

    IndexContent(String year, String month, String rawHtml) {
        this.year = year;
        this.month = month;
        this.rawHtml = rawHtml;
    }

    public static IndexContent from(String year, String month, String rawHtml) {
        return new IndexContent(year, month, rawHtml);
    }

    @Override
    public String toString() {
        return String.format("%s/%s:%s", year, month, rawHtml);
    }
}
