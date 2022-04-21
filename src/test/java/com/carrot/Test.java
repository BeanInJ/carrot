package com.carrot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String methodName = "com.carrot.Test.main";

        String s = "method:com.carrot.Test.main";
        if(s.startsWith("method:")){
            s = s.replace("method:","");
        }

        Pattern pattern = Pattern.compile("");
        Matcher matcher = pattern.matcher(methodName);
        System.out.println(matcher.matches());
    }


    public List<String> getString(String s) {

        List<String> strs = new ArrayList<String>();
        Pattern p = Pattern.compile("GraphType\\s*=\\s*\".+\"\\s*");
        Matcher m = p.matcher(s);
        while(m.find()) {
            strs.add(m.group());
        }
        return strs;
    }
}
