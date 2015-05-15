package com.antoniotari.android;

import com.google.gson.Gson;

import com.antoniotari.android.meanutil.FileUtil;
import com.antoniotari.android.meanutil.JsonUtils;
import com.antoniotari.android.meanutil.Log;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Antonio Tari on 15/12/14.
 */
public abstract class MObject {
    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public JSONObject toJson(){
        return JsonUtils.convertToJSONObject(new Gson().toJson(this));
    }

    @Override
    public boolean equals(Object item){
        return EqualsBuilder.reflectionEquals(this, item, false);
    }

    //------------------------------------------------------------------------
    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(1979, 19841, this);
    }

    public boolean saveToDisk(Context context,String filename){
        try {
            FileUtil.getInstance().storeSerializable(context,filename+"class",this.getClass());
            return FileUtil.getInstance().writeStringFile(context,filename,toJson().toString());
        } catch (IOException e) {
            Log.error(e);
        }
        return false;
    }

    public static <T> T readFromDisk(Context context,String filename){//},Class<T> classOfT){
        try {
            Class<T> thClass=(Class<T>)FileUtil.getInstance().readSerializable(context,filename+"class");
            return new Gson().fromJson(FileUtil.getInstance().readStringFile(context, filename), thClass);
        }catch (Exception e){}
        return null;
    }

    public static void parcelListInBundle(String tag, List mediaList, Bundle args) {
        Parcelable[] parcelables=(Parcelable[]) mediaList.toArray(new Parcelable[mediaList.size()]);
        args.putSerializable(tag+"class",parcelables.getClass());
        args.putParcelableArray(tag, parcelables);
    }


    public static <T> List<T>  unparcelListFromBundle(String tag, Bundle args){//}, Class<? extends T[]> newType2) {
        Class<? extends T[]> newType= (Class<? extends T[]>) args.getSerializable(tag+"class");
        Parcelable[] parcelableArray = args.getParcelableArray(tag);
        T[] array = Arrays.copyOf(parcelableArray, parcelableArray.length, newType);
        return Arrays.asList(array);
    }

    public void writeListToParcel(Parcel dest,List list){
        dest.writeInt(list.size());
        for(int i=0;i<list.size();i++)
        {
            Bundle bun=new Bundle();
            if(list.get(i) instanceof Parcelable) {
                bun.putParcelable("Parcelable", (Parcelable)(list.get(i)));
            }
            else if(list.get(i) instanceof Serializable){
                bun.putSerializable("Serializable", (Serializable) (list.get(i)));
            }
            else if(list.get(i) instanceof String){
                bun.putString("String", (String) (list.get(i)));
            }
            else if(list.get(i) instanceof Integer){
                bun.putInt("Integer", (Integer) (list.get(i)));
            }
            dest.writeBundle(bun);
        }
    }

    public void readListFromParcel(Parcel in,List list){
        int size=in.readInt();
        for(int i=0;i<size;i++)
        {
            Bundle bun=in.readBundle();
            if(bun!=null && bun.keySet()!=null){
                if(bun.keySet().contains("Parcelable")){
                    list.add((Parcelable)(bun.getParcelable("Parcelable")));
                }
                else if(bun.keySet().contains("Serializable")){
                    list.add((Serializable)(bun.getSerializable("Serializable")));
                }
                else if(bun.keySet().contains("String")){
                    list.add((String)(bun.getString("String")));
                }
                else if(bun.keySet().contains("Integer")){
                    list.add((Integer)(bun.getInt("Integer")));
                }
            }
        }
    }
}
