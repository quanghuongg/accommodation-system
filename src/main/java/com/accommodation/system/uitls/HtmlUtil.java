package com.accommodation.system.uitls;

public class HtmlUtil {
    public static String createReportMailTemplate(String templatePath, Object object) throws Exception {
        return TemplateUtil.compile(templatePath, object);
    }
}
