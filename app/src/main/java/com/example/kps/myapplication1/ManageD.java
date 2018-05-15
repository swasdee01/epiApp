package com.example.kps.myapplication1;

public class ManageD {

    static String[] mArr = {"มกราคม","กุมภาพันธ์","มีนาคม","เมษายน","พฤษภาคม","มิถุนายน","กรกฎาคม","สิงหาคม","กันยายน","ตุลาคม","พฤศจิกายน","ธันวาคม"};

    public static String thaiDate (int y, int m, int d)    {
        return String.format("@s @s @s",String.valueOf(d), mArr[m], String.valueOf(y+543));
    }

}
