package com.cafe.cafe_management.utils;

import com.google.common.base.Strings;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CafeUtils {
    private CafeUtils(){}

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }

    public static String getUUID(){
        Date date = new Date();
        long time = date.getTime();
        return "BILL-"+time;
    }

    public static JsonArray getJsonArrayFromString(String data) throws JsonParseException {
        return new Gson().fromJson(data, JsonArray.class);
    }

    public static Map<String, Object> getMapFromJson(JsonElement data){
        if(!data.isJsonNull()){
            return new Gson().fromJson(data, new TypeToken<Map<String, Object>>(){}.getType());
        }
        return new HashMap<>();
    }

    public static boolean fileExists(String path){
        log.info("Inside fileExists {}", path);
        try{
            File file = new File(path);
            return file!=null && file.exists();
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return false;
    }
}
