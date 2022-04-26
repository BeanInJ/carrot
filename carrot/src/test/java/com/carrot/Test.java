package com.carrot;

import com.carrot.factory.label.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String reg = "package:([^\\.]+(\\.[^\\.]+)*)";

        // (package:) + (非.) + (.非.)
        String reg1 = "package:[^\\.]+(\\.[^\\.]+)*";

        // 写的目标包可能是一半
        String value = "package:com.carrot";

        // 获取的包名一定是全的
        String packageName = Test.class.getPackage().getName();
        String className = Test.class.getSimpleName();

        // 用

        String thisReg = "package:" + packageName;



        System.out.println(Test.class.getName());
        String reg2 = "package:com[\\S]+";



        Pattern pattern = Pattern.compile("package:com.ing");
        Matcher matcher = pattern.matcher("package:com.ing");
        Matcher matche1= pattern.matcher("package:ing.");
        System.out.println(matcher.matches());
        System.out.println(matche1.matches());

    }


    public static List<String> getString(String s) {

        List<String> strs = new ArrayList<String>();
        Pattern p = Pattern.compile("GraphType\\s*=\\s*\".+\"\\s*");
        Matcher m = p.matcher(s);
        while(m.find()) {
            strs.add(m.group());
        }
        return strs;
    }
}
