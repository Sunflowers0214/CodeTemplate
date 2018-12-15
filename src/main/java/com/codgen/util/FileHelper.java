package com.codgen.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class FileHelper {

    public Properties getProperties(String path) {
        InputStream in = null;
        Properties p = new Properties();

        try {
            in = ClassLoaderUtil.getStream(path);
            p.load(in);
            return p;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p;
    }

    public void writeToFile(String filePath, String fileName, String content) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = new FileOutputStream(filePath + File.separator + fileName);
            osw = new OutputStreamWriter(fos, "UTF-8");
            String newStr = new String(content.getBytes(), "UTF-8");
            osw.write(newStr);
            osw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fos)
                fos.close();
            if (null != osw)
                osw.close();
        }
    }

}
