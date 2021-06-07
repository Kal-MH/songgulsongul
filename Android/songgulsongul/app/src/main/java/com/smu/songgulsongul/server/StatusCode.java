package com.smu.songgulsongul.server;

public class  StatusCode {
    public static int RESULT_OK = 200;
    public static int RESULT_NO = 201;
    public static int RESULT_CLIENT_ERR= 400;
    public static int RESULT_SERVER_ERR = 500;

    public static String getName(int a){
        if( a == RESULT_OK)
            return "RESULT OK";
        else if ( a == RESULT_CLIENT_ERR)
            return "CLIENT ERR";
        else if( a == RESULT_NO)
            return "RESULT NO";
        else if( a== RESULT_SERVER_ERR)
            return "SERVER ERR";
        else
            return "Not Status code";

    }
}
