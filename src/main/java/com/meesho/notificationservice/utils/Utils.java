package com.meesho.notificationservice.utils;

public class Utils {

    public static boolean isValidPhoneNumber( String phoneNumber){
        if(phoneNumber.length()!=13){
            return  false;
        }else if(phoneNumber.charAt(0) != '+'){
            return false;
        }
        return true;
    }
}
