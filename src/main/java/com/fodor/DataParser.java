package com.fodor;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class DataParser {


    public String MapToJSON(Map<Integer, Product>inventory){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(inventory);
    }

    public Map<Integer, Product> JSONToMap(String jsonData){
        Gson gson = new Gson();

        Type empMapType = new TypeToken<Map<Integer, Product>>() {}.getType();
        return gson.fromJson(jsonData, empMapType);
    }

    public void PrintMapAfter(Map<Integer, Product>data){
        for (int key : data.keySet()) {
            System.out.println(data.get(key));
        }
    }
}
