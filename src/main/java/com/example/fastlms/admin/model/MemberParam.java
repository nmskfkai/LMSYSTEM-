package com.example.fastlms.admin.model;

import lombok.Data;

@Data
public class MemberParam {
    long pageSize;
    long pageIndex;

    String SearchType;
    String SearchValue;

    String userId;

    public long getPageStart() {
        init();
        return (pageIndex - 1) * pageSize;
    }

    public long getPageEnd() {
        init();
        return pageSize;
    }

    public void init() {
        if (pageIndex < 1) {
            pageIndex = 1;
        }

        if (pageSize < 10) {
            pageSize = 10;
        }
    }


    public String getQueryString() {
        init();

        StringBuilder sb = new StringBuilder();

        if (SearchType != null && !SearchType.isEmpty()) {
            sb.append(String.format("searchType=%s", SearchType));
        }

        if (SearchValue != null && !SearchValue.isEmpty()) {
            if (!sb.isEmpty()) {
                sb.append("&");
            }
            sb.append(String.format("searchValue=%s", SearchValue));
        }

        return sb.toString();
    }
}
