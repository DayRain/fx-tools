package com.dayrain.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class CmdUtils {

    public static List<String> execute(String cmd){
        List<String> list = new ArrayList<>();
        InputStream inputStream = null;
        InputStream errorStream = null;
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            inputStream = process.getInputStream();
            errorStream = process.getErrorStream();
            BufferedReader out = new BufferedReader(new InputStreamReader(inputStream,  Charset.forName("GB2312")));
            BufferedReader err = new BufferedReader(new InputStreamReader(new BufferedInputStream(errorStream),  Charset.forName("GB2312")));
            String temp = null;
            while ((temp = out.readLine()) != null) {
                temp = temp.trim();
                if("".equals(temp)) {
                    continue;
                }
                list.add(temp.trim());
            }
            process.waitFor();
            process.getInputStream().close();
            String estr = err.readLine();
            if (estr != null) {
                System.out.println("\nError Info");
                System.out.println(estr);
            }
            return list;
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(errorStream != null) {
                try {
                    errorStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static String process(String cmd) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        Process p = pb.start();
        BufferedReader out = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getInputStream()), Charset.forName("GB2312")));
        BufferedReader err = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getErrorStream())));
        String ostr;
        StringBuilder result = new StringBuilder();
        while ((ostr = out.readLine()) != null)
            result.append(ostr).append("\n");
        String estr = err.readLine();
        if (estr != null) {
            System.out.println("\nError Info");
            System.out.println(estr);
        }

        return result.toString();
    }
}
