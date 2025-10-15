package com.titanic00.cloudfilestorage.util;

public class MinioObjectUtil {

    public static String buildObjectString(String path, String fileName, String rootFolderName) {
        // avoid double slash at the beginning and at the end
        if (!path.endsWith("/") && !fileName.startsWith("/")) {
            path += "/";
        }

        // remove slash at the beginning
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        System.out.println(rootFolderName);

        return rootFolderName + path + fileName;
    }
}
