package com.io;

import java.io.*;
import java.net.URL;

/**
 * @author yanguangx
 * @date 2019/7/24
 */
public class FileDownload {

    /** 远程地址 */
    private static final String REMOTE_URL = "https://hitout.github.io/file/img/SOA.png";

    public static void main(String[] args) {
        try(OutputStream outputStream = new FileOutputStream(new File("D:/SOA.png"));
            InputStream inputStream = new URL(REMOTE_URL).openStream()
        ) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
