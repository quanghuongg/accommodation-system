package com.api.user.uitls;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ServiceUtils {
    public static String generateRandomString() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

    public static boolean isValidMail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static boolean isNotEmpty(Object object) {
        return ObjectUtils.isNotEmpty(object);
    }

    public static boolean isEmpty(Object object) {
        return ObjectUtils.isEmpty(object);
    }

    public static boolean isValidPhone(String phone) {
        Pattern pattern = Pattern.compile("(\\+?84|0)((3([2-9]))|(5([689]))|(7([0|6-9]))|(8([1-9]))|(9([0-9])))([0-9]{7})");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static final String getStringStream(ServletInputStream stream, int length) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(stream, "UTF-8");
        for (; ; ) {
            int index = in.read(buffer, 0, length);
            if (index < 0) break;
            out.append(buffer, 0, index);
        }
        return out.toString();
    }

    public static String encodePassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

        return bCryptPasswordEncoder.encode(password);
    }

    public static List<String> parseStringToList(Map messages, String key) {
        String parser = String.valueOf(messages.get(key));

        return Arrays.asList(parser
                .replace("[", "")
                .replace("]", "")
                .split("\\s*, \\s*"));
    }

    public static String changeFormatDate(String date) {

        Long time = Long.parseLong(String.valueOf(date));

        Date dateOrigin = new Date(time);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        return simpleDateFormat.format(dateOrigin);
    }

    //Set time to format dd/MM/yyyy 00:00:00
    public static Date setTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        return calendar.getTime();
    }

    public static List paging(List T, int page, int pageSize) {
        if (pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }

        int fromIndex = (page - 1) * pageSize;
        if (T == null || T.size() < fromIndex) {
            return T;
        }

        // toIndex exclusive
        return T.subList(fromIndex, Math.min(fromIndex + pageSize, T.size()));
    }

    public static UserRecord getUserRecordByFirebaseIdToken(String firebaseIdToken) throws FirebaseAuthException {
        FirebaseToken decodedToken;

        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseIdToken);
        } catch (Exception e) {
            log.error("Fail verifyIdToken");
            e.printStackTrace();
            return null;
        }

        String uid = decodedToken.getUid();

        log.info("User id {}", uid);

        UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);

        log.info("Firebase user record {}", userRecord);

        return userRecord;
    }
}
