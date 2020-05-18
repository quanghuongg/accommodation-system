package com.accommodation.system.config;


import com.accommodation.system.utils2.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigurationLoader {
    private static final String FOLDER = "conf";
    private static final String OLD_CFG = ".cfg";
    private static final String NEW_CFG = ".properties";

    private Properties properties;
    private String profile;

    private static class Profiles {
        public static final String DEV = "dev";
        public static final String PROD = "prod";
        public static final String KEY = "spring.profiles.active";
        public static final String SEPARATOR = "-";
    }

    private static class SingletonHelper {
        private static final ConfigurationLoader INSTANCE = new ConfigurationLoader();
    }

    public static ConfigurationLoader getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private ConfigurationLoader() {
        this.properties = new Properties();
        this.profile = getProfile();

        File folder = new File(FOLDER);
        if (null != folder && folder.isDirectory()) {
            File[] oFiles = folder.listFiles((dir, name) -> dir.getName().equals(FOLDER) && name.toLowerCase().endsWith(Profiles.SEPARATOR + profile + OLD_CFG));
            if (Utils.isNotEmpty(oFiles)) {
                this.loadConfiguration(oFiles);
            }
            File[] nFiles = folder.listFiles((dir, name) -> dir.getName().equals(FOLDER) && name.toLowerCase().endsWith(Profiles.SEPARATOR + profile + NEW_CFG));
            if (Utils.isNotEmpty(nFiles)) {
                this.loadConfiguration(nFiles);
            }
        }
    }


    public String getProfile() {
        String profile = System.getProperty(Profiles.KEY);
        if (profile != null && Profiles.PROD.equals(profile)) {
            return Profiles.PROD;
        }
        return Profiles.DEV;
    }

    private void loadConfiguration(File[] files) {
        for (File file : files) {
            loadConfiguration(file);
        }
    }

    private void loadConfiguration(File file) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader bf = null;
        try {
            if (!file.exists()) {
                return;
            }
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            bf = new BufferedReader(isr);
            properties.load(bf);
        } catch (Exception e) {
            //todo
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (bf != null) {
                    bf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public Object get(String key) {
        return properties.get(key);
    }

    public Object get(String key, Object o) {
        return properties.getOrDefault(key, o);
    }

    public int getAsInteger(String key, int defaultNumb) {
        String trimmer = this.getTrimmed(key);
        if (trimmer != null) {
            return Integer.parseInt(properties.getProperty(key));
        } else {
            return defaultNumb;
        }
    }

    public String getAsString(String key) {
        return this.getTrimmed(key);
    }

    public String getAsString(String key, String defaultStr) {
        String value = this.getTrimmed(key);
        return value == null ? defaultStr : value;
    }

    public long getAsLong(String key, long defaultNumb) {
        String trimmer = this.getTrimmed(key);
        if (trimmer != null) {
            return Long.parseLong(properties.getProperty(key));
        } else {
            return defaultNumb;
        }
    }

    public double getAsDouble(String key, double defaultNumb) {
        String trimmer = this.getTrimmed(key);
        if (trimmer != null) {
            return Double.parseDouble(properties.getProperty(key));
        } else {
            return defaultNumb;
        }
    }

    public float getAsFloat(String key, float defaultNumb) {
        String trimmer = this.getTrimmed(key);
        return trimmer != null ? Float.parseFloat(properties.getProperty(key)) : defaultNumb;

    }

    public boolean getAsBoolean(String key, boolean defaultValue) {
        String valueString = this.getTrimmed(key);
        if (null != valueString && !valueString.isEmpty()) {
            valueString = valueString.toLowerCase();
            if ("true".equals(valueString)) {
                return true;
            } else {
                return !"false".equals(valueString) && defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public String getTrimmed(String name) {
        String value = this.properties.getProperty(name);
        return null == value ? null : value.trim();
    }
}

