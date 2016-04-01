package util;

import java.io.*;
import java.util.Properties;

public class UserProperties {

    private static final String FILENAME = "users.properties";

    public static String COLUMN_1 = "column1";
    public static String COLUMN_2 = "column2";
    public static String COLUMN_3 = "column3";
    public static String COLUMN_4 = "column4";
    public static String COLUMN_5 = "column5";
    public static String COLUMN_6 = "column6";
    public static String PREVIOUS_FILES = "previous files";
    private static Properties properties;

    public static Object getProperty(String name) {
        if (properties == null)
            readProperties();

        return properties.get(name);
    }

    public static void putProperty(String property, String newValue) {
        properties.put(property, newValue);
    }

    private static void readProperties() {
        properties = new Properties();
        try {
            File file = new File(FILENAME);
            if (!file.exists()) {
                file.createNewFile();
                addDefaultValues();
            }
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addDefaultValues() {
        properties.put(COLUMN_1, "FileName");
        properties.put(COLUMN_2, "Path");
        properties.put(COLUMN_3, "Executable");
        properties.put(COLUMN_4, "Readable");
        properties.put(COLUMN_5, "Writable");
        properties.put(COLUMN_6, "Hidden");
        properties.put(PREVIOUS_FILES, "");
    }

    public static void saveProperties() {
        try {
            File f = new File(FILENAME);
            OutputStream out = new FileOutputStream(f);
            properties.store(out, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
