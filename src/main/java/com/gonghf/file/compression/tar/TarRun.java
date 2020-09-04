package com.gonghf.file.compression.tar;

import java.io.File;

public class TarRun {
    public static void main(String[] args) {
        String dir = "F:/迅雷下载/moban4277/moban4277/examples/demo2";
        String out = "F:/test";
        //String dir = args[0];
        try {
            //压缩
            String compression = TarUtil.compression(dir);
            //解压缩
            File[] files = TarUtil.decompression(compression, out);
            System.out.println("--------------------------------");
            for(File file:files){
                System.out.println(file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
