package com.easily.system.util;

public class HtmlHelper {
    public static String getHtml() {
        String responseFirstLine = "HTTP/1.1 200 OK\r\n";
        // HTTP响应头
        String responseHeader = "Content-Type:" + "html" + "\r\n\r\n";
        String header = responseFirstLine + responseHeader;
        //组装HTTP响应正文
        String content = "<body><h1>Hello:" + "1" + "</h1></body>";
        String title = "<head><title>HelloWorld</title></head>";
        String body = "<html>" + title + content + "</html>";
        return header + body;
    }

}
