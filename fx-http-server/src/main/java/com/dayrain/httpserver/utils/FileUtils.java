package com.dayrain.httpserver.utils;

import com.dayrain.httpserver.component.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    public static void saveConfig(Configuration configuration) {

        saveConfig(configuration, getConfigFile());
    }

    public static void saveConfig(Configuration configuration, File file) {

        BufferedWriter bufferedWriter = null;
        try {
            String config = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(configuration);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            bufferedWriter.write(config);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Configuration load() {

        return load(getConfigFile());
    }

    public static Configuration load(File file) {
        BufferedReader bufferedReader = null;
        try {
            StringBuilder configStr = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String buf;
            while ((buf = bufferedReader.readLine()) != null) {
                configStr.append(buf);
            }
            return "".equals(configStr.toString()) ? new Configuration(1400, 800, 8, 8) : new ObjectMapper().readValue(configStr.toString(), Configuration.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static String getFromInputStream(InputStream inputStream) {
        try {
            byte[] buf = new byte[4096];
            int len;
            StringBuilder stringBuilder = new StringBuilder();
            while ((len = inputStream.read(buf)) != -1) {
                stringBuilder.append(new String(buf, 0, len));
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static File getConfigFile() {
        String property = System.getProperty("user.dir");
        File file = new File(property + File.separator + "config" + File.separator + "config.json");
        if (!file.exists()) {

            File dir = new File(property + File.separator + "config");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Configuration configuration = new Configuration(1200, 800, 8, 8);
            saveConfig(configuration, file);
        }
        return file;
    }


}
