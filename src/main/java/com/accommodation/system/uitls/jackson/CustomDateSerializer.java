package com.accommodation.system.uitls.jackson;

import com.accommodation.system.define.Constant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.SDF_FORMAT);
        jsonGenerator.writeString(dateFormat.format(date));
    }
}
