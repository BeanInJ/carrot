package com.vegetables.entity;

import java.util.Map;

public interface Http {
    String[] getFirstLine();
    Map<String,String> getHeader();
    String getBody();

    void setFirstLine(String[] firstLine);
    void setHeader(Map<String,String> header);
    void setBody(String body);
}
