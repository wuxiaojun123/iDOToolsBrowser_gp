package com.example;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.text.html.HTML;

public class MyClass extends ParentBean {

    public static void main(String[] args) {

        try {
//            Class<ParentBean> aClass = (Class<ParentBean>) Class.forName("com.example.ParentBean");
            ParentBean mbean = new ParentBean();
            Field field = ParentBean.class.getDeclaredField("offset");
            field.setAccessible(true);
            field.setInt(mbean, 0);

            //获取
            System.out.println("值是：" + field.getInt(mbean));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
