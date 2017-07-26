package com.terry.daxiang.jiazhang.custom;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义解析json格式
 *
 * Created by fulei on 16/11/8.
 */

public class JsonParser {

    /**
     * 根据bean来解析
     * @param jsonObject json
     * @param t bean对象的class
     * @param _t 初始化class
     * @return
     */
    public static <T> T get(JSONObject jsonObject, Class<?> t , T _t) {
        String set = "set";
        try {
            // 是否是内部类
            if (t.isMemberClass()) {
                int i=t.getModifiers();
                String strModifier = Modifier.toString(i);
                if(strModifier.contains("static")) {//静态内部类
                    _t = (T) t.newInstance();
                }else {
                    Class<?> cc = Class.forName(t.getName());
                    Constructor<?>[] cs = cc.getDeclaredConstructors();
                    _t = (T)cs[0].newInstance(_t);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // 获取类的内部类
        Class<?>[] t_class = t.getClasses();
        // 获取类的属性
        Field[] fields = t.getDeclaredFields();
        // 获取jsonObject数据的key
        JSONArray arrays = jsonObject.names();

        /**
         * 根据bean类属性进行解析jsonObject
         */
        for (Field field : fields) {
            for (int i = 0; i < arrays.length(); i++) {
                // 把类属性的第一个字母变大写
                String methodName = field.getName().substring(0, 1).toUpperCase()+ field.getName().substring(1);
                // Log.e("", methodName + "--" + field.getType() + " ");
                try {
                    /**
                     * 给类属性赋予相应 jsonObject key的值 String 类型
                     */
                    if (field.getName().equals(arrays.getString(i))) {
                        if (String.class.equals(field.getType())) {
                            if (jsonObject.has(arrays.getString(i))) {
                                // 给类属性相应set方法赋予jsonObject
                                // key的值=====》getString()
                                try {
                                    Method method = t.getMethod(set+ methodName, String.class);
                                    try {
                                        // 调用类的set方法
                                        method.invoke(_t, jsonObject.optString(arrays.getString(i)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        /**
                         * 给类属性赋予相应 jsonObject key的值 int 类型
                         */
                        if (int.class.equals(field.getType())) {
                            if (jsonObject.has(arrays.getString(i))) {
                                try {
                                    // 给类属性相应set方法赋予jsonObject
                                    // key的值=====》getInt()
                                    Method method = t.getMethod(set+ methodName, int.class);
                                    try {
                                        // 调用类的set方法
                                        method.invoke(_t, jsonObject.optInt(arrays.getString(i)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        /**
                         * 给类属性赋予相应 jsonObject key的值 Integer 类型
                         */
                        if (Integer.class.equals(field.getType())) {
                            if (jsonObject.has(arrays.getString(i))) {
                                try {
                                    // 给类属性相应set方法赋予jsonObject
                                    // key的值=====》getInt()
                                    Method method = t.getMethod(set+ methodName, Integer.class);
                                    try {
                                        // 调用类的set方法
                                        method.invoke(_t, jsonObject.optInt(arrays.getString(i)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        /**
                         * 解析内部类JSONObject
                         */
                        for (Class<?> tClass : t_class) {
                            if (tClass.equals(field.getType())) {

                                // 判断是否有该Key的JSONObject
                                if (jsonObject.has(arrays.getString(i))) {
                                    // key的值=====》getJSONObject()
                                    try {
                                        Method method = t.getMethod(set
                                                + methodName, tClass);
                                        Log.e("", "HcJson -->JSONObject: "+arrays.getString(i));
                                        // 重复调用get进行解析内部类属性
                                        Object arrayClass = get(jsonObject.getJSONObject(arrays.optString(i)), tClass , _t);
                                        try {
                                            // 调用类的set方法
                                            method.invoke(_t, arrayClass);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        /**
                         * 给类属性赋予相应 jsonObject key的值 ArrayList 类型
                         */
                        if (ArrayList.class.equals(field.getType())) {
                            if (jsonObject.has(arrays.getString(i))) {
                                try {
                                    // 给类属性相应set方法赋予jsonObject
                                    // key的值=====》getJSONArray()
                                    Method method = t.getMethod(set+ methodName, ArrayList.class);
                                    JSONArray array = jsonObject.getJSONArray(arrays.getString(i));
                                    ArrayList<Object> list = null;
                                    // is JSONArray
                                    if(t_class.length <=0){
                                        //这个是数组 比如字符串数组["" ,"" , ""]
                                        list = new ArrayList<Object>();
                                        for (int arrayNum = 0; arrayNum < array.length(); arrayNum++) {
                                            list.add(array.get(arrayNum));
                                        }
                                    }else{
                                        //这个是对象List
                                        for (Class<?> tClass : t_class) {
                                            if (methodName.equals(tClass
                                                    .getSimpleName())) {
                                                list = new ArrayList<Object>();
                                                for (int arrayNum = 0; arrayNum < array.length(); arrayNum++) {
                                                    // is Member Class
                                                    // 重复调用get进行解析内部类属性
                                                    Object arrayClass = get(array.getJSONObject(arrayNum),tClass ,_t);
                                                    list.add(arrayClass);
                                                }
                                            }
                                        }
                                    }
                                    try {
                                        // 调用类的set方法
                                        method.invoke(_t, list);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        /**
                         * 给类属性赋予相应 jsonObject key的值 ArrayList 类型
                         */
                        if (List.class.equals(field.getType())) {
                            if (jsonObject.has(arrays.getString(i))) {
                                try {
                                    // 给类属性相应set方法赋予jsonObject
                                    // key的值=====》getJSONArray()
                                    Method method = t.getMethod(set+ methodName, List.class);
                                    JSONArray array = jsonObject.getJSONArray(arrays.getString(i));
                                    List<Object> list = null;
                                    // is JSONArray
                                    if(t_class.length <=0){
                                        //这个是数组 比如字符串数组["" ,"" , ""]
                                        list = new ArrayList<Object>();
                                        for (int arrayNum = 0; arrayNum < array.length(); arrayNum++) {
                                            list.add(array.get(arrayNum));
                                        }
                                    }else{
                                        //这个是对象List
                                        for (Class<?> tClass : t_class) {
                                            if (methodName.equals(tClass
                                                    .getSimpleName())) {
                                                list = new ArrayList<Object>();
                                                for (int arrayNum = 0; arrayNum < array.length(); arrayNum++) {
                                                    // is Member Class
                                                    // 重复调用get进行解析内部类属性
                                                    Object arrayClass = get(array.getJSONObject(arrayNum),tClass ,_t);
                                                    list.add(arrayClass);
                                                }
                                            }
                                        }
                                    }
                                    try {
                                        // 调用类的set方法
                                        method.invoke(_t, list);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        /**
                         * 给类属性赋予相应 jsonObject key的值 Long 类型
                         */
                        if (Long.class.equals(field.getType())) {
                            if (jsonObject.has(arrays.getString(i))) {
                                try {
                                    // 给类属性相应set方法赋予jsonObject
                                    // key的值=====》getLong()
                                    Method method = t.getMethod(set+ methodName, Long.class);
                                    try {
                                        // 调用类的set方法
                                        method.invoke(_t, jsonObject.optLong(arrays.getString(i)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        /**
                         * 给类属性赋予相应 jsonObject key的值 long 类型
                         */
                        if (long.class.equals(field.getType())) {
                            if (jsonObject.has(arrays.getString(i))) {
                                try {
                                    // 给类属性相应set方法赋予jsonObject
                                    // key的值=====》getLong()
                                    Method method = t.getMethod(set
                                            + methodName, long.class);
                                    try {
                                        // 调用类的set方法
                                        method.invoke(_t, jsonObject
                                                .optLong(arrays.getString(i)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        /**
                         * 给类属性赋予相应 jsonObject key的值 Boolean 类型
                         */
                        if (Boolean.class.equals(field.getType())) {
                            if (jsonObject.has(arrays.getString(i))) {
                                try {
                                    // 给类属性相应set方法赋予jsonObject
                                    // key的值=====》getBoolean()
                                    Method method = t.getMethod(set
                                            + methodName, Boolean.class);
                                    try {
                                        // 调用类的set方法
                                        method.invoke(_t,jsonObject.optBoolean(arrays.getString(i)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        /**
                         * 给类属性赋予相应 jsonObject key的值 boolean 类型
                         */
                        if (boolean.class.equals(field.getType())) {
                            // 给类属性相应set方法赋予jsonObject key的值=====》getBoolean()
                            if (jsonObject.has(arrays.getString(i))) {
                                try {
                                    Method method = t.getMethod(set
                                            + methodName, boolean.class);
                                    try {
                                        // 调用类的set方法
                                        method.invoke(_t,jsonObject.optBoolean(arrays.getString(i)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (null == _t) {
            Log.e("", "没有可执行解析类属性和其方法");
        }
        return _t;
    }

}
