package com.gonghf.compression.file.rar;

import com.gonghf.compression.commom.Log;

import java.io.IOException;

public class RarRun {
    public static void main(String[] args) {
        Log.initLog();
        String dir = "F:\\2k名单id.rar";
        String out = "F:/test/tar";
        try {
            RarUtil.unRar(dir,out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
