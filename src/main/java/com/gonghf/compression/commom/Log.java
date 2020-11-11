package com.gonghf.compression.commom;

import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Log {
    public static void initLog() {
        String userdir = System.getProperty("user.dir") + File.separator + "config" + File.separator;
        try {
            PropertyConfigurator.configure(new FileInputStream(userdir + "log4j.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
