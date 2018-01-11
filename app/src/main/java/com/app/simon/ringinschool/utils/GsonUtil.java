package com.app.simon.ringinschool.utils;

import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Gson 工具类
 * Created by xw on 2016/8/16
 */
public class GsonUtil {
    public static final String TAG = GsonUtil.class.getSimpleName();

    private static final Gson gson;

    static {
        gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            /**
             * 设置要过滤的属性
             */
            @Override
            public boolean shouldSkipField(FieldAttributes attr) {
                // 设置对应类中要过虑的属性
                //                if (attr.getDeclaringClass() == UserInfo.class) {
                //                    return "password".equals(attr.getName());
                //                } else if (attr.getDeclaringClass() == Permission.class) {
                //                    return "roles".equals(attr.getName());
                ////				} else if (attr.getDeclaringClass() == Role.class) {
                ////					return "permissions".equals(attr.getName());
                //                } else if (attr.getDeclaringClass() == WorkGroup.class) {
                //                    return "parentWorkGroup".equals(attr.getName());
                //                } else if (attr.getDeclaringClass() == Report.class) {
                //                    return "bill".equals(attr.getName());
                //                }
                return false;
            }

            /**
             * 设置要过滤的类
             */
            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                // 这里，如果返回true就表示此类要过滤，否则就输出
                return false;
            }
        }).create();
    }

    /**
     * 转换为Json
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * 转换为Object
     *
     * @param json
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> T toObj(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, json + " 解析json失败");
        }
        return null;
    }

    /**
     * 转换为Object
     *
     * @param json
     * @param typeOfT
     * @param <T>
     * @return
     */
    public static <T> T toObj(String json, Type typeOfT) {
        try {
            return gson.fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, json + " 解析json失败");
        }
        return null;
    }
}
