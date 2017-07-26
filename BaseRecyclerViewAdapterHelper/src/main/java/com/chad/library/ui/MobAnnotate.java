package com.chad.library.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import java.lang.reflect.Field;

/**
 * 以注解方式绑定控件
 * Created by fulei on 16/10/24.
 */
public class MobAnnotate {

    public static void init(Activity activity){
        init(activity, activity.getWindow().getDecorView());
    }

    public static void init(Fragment fragment){
        //init(fragment, fragment.getActivity().getWindow().getDecorView());
        init(fragment,fragment.getView());
        //init(fragment.getActivity());
    }

    public static void init(View view){
        Context context = view.getContext();
        if(context instanceof Activity){
            init((Activity)context);
        }else{
            throw new RuntimeException("view must into Activity");
        }
    }

    private static void init(Object obj,View v){
        // 通过反射获取到全部属性，反射的字段可能是一个类（静态）字段或实例字段
        Field[] fields = obj.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                // 返回BindView类型的注解内容
                BindView bindView = field.getAnnotation(BindView.class);
                if (bindView != null) {
                    int viewId = bindView.id();
                    //Log.e("", field.getName()+":"+viewId);
                    boolean clickLis = bindView.click();
                    try {
                        field.setAccessible(true);
                        if (clickLis) {
                            v.findViewById(viewId).setOnClickListener((View.OnClickListener) obj);
                        }
                        // 将currentClass的field赋值为sourceView.findViewById(viewId)
                        field.set(obj, v.findViewById(viewId));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
