package com.ljc.librarybackend.utils;

import lombok.Data;

@Data
public class ResultModel<T> {
    private int code;
    private String message="";
    private T content;
    public ResultModel(){
    }
    public ResultModel(int code, String message){
        this.code = code;
        this.message = message;
    }
    public ResultModel(int code, String message, T content){
        this.code = code;
        this.message = message;
        this.content = content;
    }
    public static <T> ResultModel<T> success(String message,T content){
        return new ResultModel<T>(0,message,content);
    }
    public static <T> ResultModel<T> success(T content){
        return new ResultModel<T>(0,"success",content);
    }
    public static <T> ResultModel<T> success(String message){
        return new ResultModel<T>(0,message);
    }
    public static <T> ResultModel<T> error(int code,String message){
        return new ResultModel<T>(code,message);
    }
    public static <T> ResultModel<T> error(String message){
        return new ResultModel<T>(-1,message);
    }
    public static <T> ResultModel<T> error(){
        return new ResultModel<T>(-1,"error");
    }
}
