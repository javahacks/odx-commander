package com.javahacks.odx.utils;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.logging.LogManager;

/**
 * Initializing Java util logging is tedious ....
 */
public class JavaUtilLoggingInitializer {

    public static void init() {
        try {
            new File(System.getProperty("user.home"), ".odx-commander").mkdirs();
            LogManager.getLogManager().readConfiguration(propertiesAsStream());
        } catch (final Exception e) {
            LoggerFactory.getLogger(JavaUtilLoggingInitializer.class).error("Could not init Java util logging properly", e);
        }
    }

    private static InputStream propertiesAsStream() {
        return JavaUtilLoggingInitializer.class.getClassLoader().getResourceAsStream("logging.properties");
    }


}
