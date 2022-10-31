package com.ibrahimtahasanli.customview.controller

import org.json.JSONArray

object DataObject {
    private var objects: JSONArray? = null;

    fun GetObjects() : JSONArray?{
        return objects;
    }

    fun SetObjects(obj: JSONArray){
        objects = obj;
    }
}