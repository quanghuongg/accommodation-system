package com.accommodation.system.uitls;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.IOException;
import java.io.StringWriter;

public class TemplateUtil {
    static MustacheFactory mf = new DefaultMustacheFactory();

    public static String compile(String template, Object data) throws IOException {
        Mustache m = mf.compile(template);
        StringWriter writer = new StringWriter();
        m.execute(writer, data).flush();
        return writer.toString();
    }
}
