package com.gonghf.file.compression.tar;

import java.io.File;

public class TarRun {
    public static void main(String[] args) {
        String dir = "F:/迅雷下载/moban4277/moban4277/examples/demo2";
        String out = "F:/test";
        try {
            //压缩
            String compression = TarUtil.compression(dir);
            System.out.println("compression:" + compression);
            //解压缩
            File[] files = TarUtil.decompression(compression, out);
            for(File file:files){
                System.out.println(file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
