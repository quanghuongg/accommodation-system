package com.accommodation.system.uitls;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class GsonUtils {
    public static final Gson GSON_NORMAL = new Gson();
    public static final Gson GSON_UTC_DATE = new GsonBuilder()
            .registerTypeAdapter(Date.class, GsonUTCDateAdapter.getInstance())
            .create();
    public static final Gson GSON_UTCDATE_NORMNUMBER = new GsonBuilder()
            .registerTypeAdapter(Date.class, GsonUTCDateAdapter.getInstance())
//            .registerTypeAdapter(Double.class, NumberSerializers.DoubleSerializer.getInstance())
            .create();
    public static final Gson GSON_UTCDATE_NORNUMBER_UNDERSCORE = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(Date.class, GsonUTCDateAdapter.getInstance())
//            .registerTypeAdapter(Double.class, NumberSerializers.DoubleSerializer.getInstance())
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

//    public static <T> ArrayList<T> fromJson(String json, Class<T> classOfT) {
//        TypeToken<List<Notification>> token = new TypeToken<List<Notification>>() {
//        };
//        return GSON_UTCDATE_NORNUMBER_UNDERSCORE.fromJson(json, token.getType());
//    }

}
