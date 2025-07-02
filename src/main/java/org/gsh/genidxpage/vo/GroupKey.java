package org.gsh.genidxpage.vo;

public class GroupKey {

    private String value;

    GroupKey(final String value) {
        this.value = value;
    }

    public static GroupKey from(String value) {
        return new GroupKey(value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
