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

    protected void addBeforeMethod(FilterBody beforeMethod) {
        this.beforeMethods.add(beforeMethod);
    }

    protected void addAfterMethod(FilterBody afterMethod) {
        this.afterMethods.add(afterMethod);
    }

    /**
     * 经过前置过滤器
     */
    public BaseResponse beforeController(BaseRequest request) {
        return forList(beforeMethods, request, null);
    }

    /**
     * 经过后置过略器
     */
    public void afterController(BaseRequest request, BaseResponse response) {
        forList(afterMethods, request, response);
    }

    /**
     * filterPool解析完成后，进行排序
     */
    public void order() {
        // 预备执行（为了初始化顺序）
        forList(beforeMethods, HttpUtils.getTestRequest(), HttpUtils.getTestResponse());
        forList(afterMethods, HttpUtils.getTestRequest(), HttpUtils.getTestResponse());

        // 排序
        if (this.beforeMethods.size() > 0) {
            this.beforeMethods = this.beforeMethods.stream().sorted(Comparator.comparing((filterBody) ->
                    ((FilterBody) filterBody).getPriority()
            ).reversed()).collect(Collectors.toList());
        }

        if (this.afterMethods.size() > 0) {
            this.afterMethods = this.afterMethods.stream().sorted(Comparator.comparing((filterBody) ->
                    ((FilterBody) filterBody).getPriority()
            ).reversed()).collect(Collectors.toList());
        }
    }

    /**
     * 循环执行过略器
     */
    private static BaseResponse forList(List<FilterBody> list, BaseRequest request, BaseResponse response) {

        for (FilterBody filterBody : list) {
            // 初始化 filterBody
            filterBody.setRequest(request);
            filterBody.setResponse(response);
            Method method = filterBody.getMethodBody().getMethod();
            Object object = filterBody.getMethodBody().getObject();

            try {
                // 获取参数
                Object[] params = createFilterMethodParams(method, request, response, filterBody);

                // 如果返回类型不是BaseResponse，则不改变原有的response
                if (method.getReturnType() == BaseResponse.class) {
                    response = (BaseResponse) method.invoke(object, params);
                } else {
                    method.invoke(object, params);
                }

                // 如果当前拦截器要求立即返回
                if (response != null && response.isReturnNow()) return response;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    /**
     * 过略器方法入参
     */
    private static Object[] createFilterMethodParams(Method method, BaseRequest request, BaseResponse response, FilterBody filterBody) {
        Class<?>[] parameterType = method.getParameterTypes();
        int parameterCount = method.getParameterCount();

        Object[] params = new Object[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            if (parameterType[i] == BaseRequest.class) {
                params[i] = request;
            } else if (parameterType[i] == BaseResponse.class) {
                params[i] = response;
            } else if (parameterType[i] == FilterBody.class) {
                params[i] = filterBody;
            } else {
                params[i] = null;
            }
        }
        return params;
    }
}
