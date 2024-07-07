package com.example.summerproject.utils;

public class FilePathConstants {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String PROJECT_NAME = PROJECT_PATH.substring(PROJECT_PATH.lastIndexOf(FILE_SEPARATOR) + 1);
    public static final String PRESENT_DIR = PROJECT_PATH.replace(PROJECT_NAME, "");

    public static final String UPLOAD_DIR = PRESENT_DIR + "ulci-document" + FILE_SEPARATOR + "ulci" + FILE_SEPARATOR;
    public static final String TEMP_PATH = PRESENT_DIR + "ulcitempdocument" + FILE_SEPARATOR + "doc" + FILE_SEPARATOR;

    public static final String SYSTEM_ATTACHMENT_PATH = UPLOAD_DIR + "system-attachment" + FILE_SEPARATOR + "doc" + FILE_SEPARATOR;

}
