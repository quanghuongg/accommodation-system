package com.accommodation.system.utils2;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
    public static String getStringStream(InputStream is, int length) throws IOException {
        DataInputStream in = new DataInputStream(is);
        final int SIZE = 8092;
        ByteArrayOutputStream result;
        if (length > 0) {
            result = new ByteArrayOutputStream(length);
        } else {
            result = new ByteArrayOutputStream();
        }
        byte[] buff = new byte[SIZE];
        int nRead = 0;
//        int nReadTotal = 0;
        while ((nRead = in.read(buff, 0, SIZE)) != -1) {
            result.write(buff, 0, nRead);
//            nReadTotal += nRead;
        }
        return new String(result.toByteArray(), 0, length, "UTF-8");
    }
}
