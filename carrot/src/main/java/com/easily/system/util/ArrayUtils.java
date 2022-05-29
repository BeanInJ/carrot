package com.easily.system.util;

import java.util.HashMap;
import java.util.Map;

public class ArrayUtils {
    /**
     * 判断目标数组 targetArray 是否在 模板中 templateArray
     * @param templateArray 模板数组
     * @param targetArray 目标数组
     * @param startIndex 目标数组在目标中的位置
     * @return true 在，false 不在
     */
    public static boolean isInTemplateArray(byte[] templateArray,byte[] targetArray, int startIndex){
        for(int i=startIndex, n=0; n < targetArray.length; i++, n++){
            if(templateArray[i] != targetArray[n]) return false;
        }
        return true;
    }

    /**
     * 判断两个数组是否相等
     * @param array1 数组1
     * @param array2 数组2
     * @return 是的相等
     */
    public static boolean isSameArray(byte[] array1, byte[] array2) {
        if (array1.length != array2.length) return false;
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) return false;
        }
        return true;
    }

    /**
     * 获取数组 startIndex 到 endIndex 之间的数组
     */
    public static byte[] getArray(byte[] array, int startIndex, int endIndex) {
        if (startIndex < 0 || startIndex > endIndex) return null;

        if(endIndex > array.length) endIndex = array.length;

        byte[] result = new byte[endIndex - startIndex];
        for (int i = startIndex, n = 0; i < endIndex; i++, n++) {
            result[n] = array[i];
        }
        return result;
    }

    public static String getString(byte[] array, int startIndex, int endIndex) {
        byte[] array1 = getArray(array, startIndex, endIndex);
        return array1 == null ? null : new String(array1);
    }

    /**
     * XXX=xxx&XXX=xxx 这种结构变成 map
     */
    public static Map<String,String> equAndToMap(byte[] array,int index){
        Map<String,String> map = new HashMap<>();
        int startIndex = 0;
        int endIndex = -1;
        for (int j=index; j<array.length;j++){
            // XXX=xxx&XXX=xxx

            int equIndex = 0;
            String key = null;
            String value = null;
            if (array[j] == (byte) '=') {
                equIndex = j;
                key = ArrayUtils.getString(array, startIndex, equIndex);
            }

            if (array[j] == (byte) '&' && equIndex != 0){
                endIndex = j;
                value = ArrayUtils.getString(array,equIndex+1 ,endIndex);
                map.put(key,value);
            }

        }

        return map;
    }
}
