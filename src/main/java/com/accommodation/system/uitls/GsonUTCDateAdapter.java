package com.accommodation.system.uitls;

import com.google.gson.*;

import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GsonUTCDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    private final DateFormat dateFormat;

    public GsonUTCDateAdapter() {
        //This is the format I need
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        //This is the key line which converts the date to UTC which cannot be accessed with the default serializer
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public synchronized JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(dateFormat.format(date));
    }

    @Override
    public synchronized Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        try {
            String asString = jsonElement.getAsString();
            if (asString.isEmpty()) {
                return null;
            }
            return DatatypeConverter.parseDateTime(jsonElement.getAsString()).getTime();
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    public static GsonUTCDateAdapter getInstance() {
        return GsonUTCDateAdapterHolder.INSTANCE;
    }

    private static class GsonUTCDateAdapterHolder {

        private static final GsonUTCDateAdapter INSTANCE = new GsonUTCDateAdapter();
    }
}
