package org.gsh.genidxpage.service.dto;

public class FindBlogPostDto {

    private final String year;
    private final String month;

    public FindBlogPostDto(String year, String month) {
        this.year = year;
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }
}
