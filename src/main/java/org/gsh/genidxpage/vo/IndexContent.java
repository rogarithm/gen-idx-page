package org.gsh.genidxpage.vo;

public class IndexContent {

    private String postListId;
    private String rawHtml;

    private IndexContent() {}

    IndexContent(String postListId, String rawHtml) {
        this.postListId = postListId;
        this.rawHtml = rawHtml;
    }

    public static IndexContent from(String year, String month, String rawHtml) {
        String postListId =  String.format("%s/%s", year, month);
        return new IndexContent(postListId, rawHtml);
    }

    @Override
    public String toString() {
        return String.format("%s:%s", postListId, rawHtml);
    }
}
