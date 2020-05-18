package com.accommodation.system.uitls;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigurationLoader {
    private static final String FOLDER = "conf";
    private static final String OLD_CFG = ".cfg";
    private static final String NEW_CFG = ".properties";
    private Properties properties;

    private static class SingletonHelper {
        private static final ConfigurationLoader INSTANCE = new ConfigurationLoader();
    }

    public static ConfigurationLoader getInstance() {
        return SingletonHelper.INSTANCE;
    }

    private ConfigurationLoader() {
        this.properties = new Properties();
        File folder = new File(FOLDER);
        if (null != folder && folder.isDirectory()) {
            File[] oFiles = folder.listFiles((dir, name) -> dir.getName().equals(FOLDER) && name.toLowerCase().endsWith(OLD_CFG));
            if (oFiles != null) {
                this.loadConfiguration(oFiles);
            }
            File[] nFiles = folder.listFiles((dir, name) -> dir.getName().equals(FOLDER) && name.toLowerCase().endsWith(NEW_CFG));
            if (nFiles != null) {
                this.loadConfiguration(nFiles);
            }
        }
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

