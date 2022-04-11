package com.vegetables.system.logging;

import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.logging.Formatter;

import java.util.logging.Handler;

import java.util.logging.LogRecord;

/**
 * 自定义日志格式
 */
public class CarrotLogFormatter extends Formatter {

    @Override
    public String format(LogRecord arg0) {

        StringBuilder builder = new StringBuilder();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String dateStr = sdf.format(now);

        StringBuilder className = new StringBuilder(arg0.getSourceClassName() + "." + arg0.getSourceMethodName());
        int length = className.length();
        if(length>25){
            className = new StringBuilder("..." + className.substring(length - 25, length));
        }else{
            int num = 28-length;
            for(int i=0;i<num;i++){
                className.append(" ");
            }
        }

        // 日期
        builder.append(dateStr);
        //拼接日志级别
        builder.append(" [").append(arg0.getLevel()).append("] ");
        //拼接方法名
        builder.append(className).append(" : ");
        //拼接msg
        builder.append(arg0.getMessage());
        //换行
        builder.append("\r\n");
        return builder.toString();

    }

    @Override
    public String getHead(Handler h) {
        return "Carrot Log Began:\r\n";
    }

    @Override
    public String getTail(Handler h) {
        return "Carrot Log End.\r\n";
    }

}