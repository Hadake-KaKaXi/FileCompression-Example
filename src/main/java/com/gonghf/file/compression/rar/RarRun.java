package com.gonghf.file.compression.rar;

import java.io.IOException;

public class RarRun {
    public static void main(String[] args) {
        String dir = "F:\\2k名单id.rar";
        String out = "F:/test/tar";
        try {
            RarUtil.unRar(dir,out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
