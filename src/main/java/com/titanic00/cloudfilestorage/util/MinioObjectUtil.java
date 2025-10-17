package com.titanic00.cloudfilestorage.util;

public class MinioObjectUtil {

    public static String buildObjectName(String path, String fileName) {
        // avoid double slash at the beginning and at the end
        if (!path.endsWith("/") && !fileName.startsWith("/")) {
            path += "/";
        }

        // remove slash at the beginning
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        return path + fileName;
    }
}
