package com.example.swd_project;

public class ApiHolder {
    public static String sever = "https://swdproject.herokuapp.com/api";
    //public static String sever = "https://b8d50c00.ngrok.io/api";
    public static String urlLogin = sever+"/Users/Login";
    public static String urlGoogleLogin = sever+"/Users/LoginFireBase?token=";
    public static String urlGetCourse = sever+"/Courses?currentpage=1&pagerange=10";
    public static String urlCourse = sever+"/Courses";
    public static String urlProgramCourse = sever+"/Programs";
    public static String urlProgramCourses = sever+"/ProgramCourses";
    public static String urlProgramList = sever+"/Programs?currentpage=1&pagerange=10";
}
