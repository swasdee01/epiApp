package com.example.kps.myapplication1;

public class MyGlobal {

    private static MyGlobal instance;

    private String myurl, cid, pw, fullName, hcode;

    private MyGlobal()  {}

    public static synchronized  MyGlobal getInstance()  {
        if(instance==null)  {
            instance=new MyGlobal();
        }
        return instance;
    }

    public void setUrl(String d)    {
        this.myurl=d;
    }

    public String getUrl()    {
        return this.myurl;
    }

    public void setCid(String d)    {
        this.cid=d;
    }

    public String getCid()    {
        return this.cid;
    }

    public void setPw(String d)    {
        this.pw=d;
    }

    public String getPw()    {
        return this.pw;
    }

    public void setFullName(String d)    {
        this.fullName=d;
    }

    public String getFullName()    {
        return this.fullName;
    }

    public void setHcode(String d)    {
        this.hcode=d;
    }

    public String gethcode()    {
        return this.hcode;
    }


}
