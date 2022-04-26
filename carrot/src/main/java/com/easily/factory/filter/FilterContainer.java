package com.easily.factory.filter;

import com.easily.core.http.BaseRequest;
import com.easily.core.http.BaseResponse;
import com.easily.system.util.HttpUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FilterContainer {
    private List<FilterBody> beforeMethods = new ArrayList<>();
    private List<FilterBody> afterMethods = new ArrayList<>();

    public BaseResponse beforeController(BaseRequest request){
        return forList(beforeMethods, request, null);
    }

    public void addBeforeMethod(FilterBody beforeMethod) {
        this.beforeMethods.add(beforeMethod);
    }

    public void afterController(BaseRequest request, BaseResponse response){
        forList(afterMethods, request, response);
    }

    public void addAfterMethod(FilterBody afterMethod) {
        this.afterMethods.add(afterMethod);
    }

    public void order(){
        // 预备执行（为了初始化顺序）
        forList(beforeMethods, HttpUtils.getTestRequest(), HttpUtils.getTestResponse());
        forList(afterMethods, HttpUtils.getTestRequest(), HttpUtils.getTestResponse());

        // 排序
        if(beforeMethods.size()>0) {
            // 清理后根据优先级排序
            beforeMethods = beforeMethods.stream()
                    .sorted(
                            Comparator.comparing(filterBody -> ((FilterBody) filterBody).getPriority())
                                    .reversed())
                    .collect(Collectors.toList());

        }

        if(afterMethods.size()>0) {
            afterMethods = afterMethods.stream()
                    .sorted(
                            Comparator.comparing(filterBody -> ((FilterBody) filterBody).getPriority())
                                    .reversed())
                    .collect(Collectors.toList());
        }
    }

    private static BaseResponse forList(List<FilterBody> list, BaseRequest request, BaseResponse response){

        for(FilterBody filterBody :list){
            // 初始化 filterBody
            filterBody.setRequest(request);
            filterBody.setResponse(response);
            Method method = filterBody.getMethodBody().getMethod();
            Object object = filterBody.getMethodBody().getObject();

            try {
                Class<?>[] parameterType = method.getParameterTypes();
                int parameterCount = method.getParameterCount();

                // 判断有无参数
                if(parameterCount == 0){
                    // 无参数执行方法
                    response = (BaseResponse) method.invoke(object);
                }else{
                    // 有参数执行方法

                    // 循环入参
                    Object[] params = new Object[parameterCount];
                    for (int i = 0; i < parameterCount; i++) {
                        if(parameterType[i] == BaseRequest.class){
                            params[i] = request;
                        }else if(parameterType[i] == BaseResponse.class){
                            params[i] = response;
                        }else if(parameterType[i] == FilterBody.class){
                            params[i] = filterBody;
                        }
                    }

                    // 如果返回类型不是BaseResponse，则不改变原有的response
                    if(method.getReturnType() == BaseResponse.class){
                        response = (BaseResponse) method.invoke(object, params);
                    }else {
                        method.invoke(object, params);
                    }
                }

                // 如果当前拦截器要求立即返回
                if(response != null){
                    if(response.isReturnNow()) return response;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return response;
    }
}
