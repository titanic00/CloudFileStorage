package com.titanic00.cloudfilestorage.util;

import com.titanic00.cloudfilestorage.entity.User;

public class MinioObjectUtil {

    public static String buildObjectString(String path, String fileName, User user) {
        // avoid double slash at the beginning and at the end
        if(!path.endsWith("/") && !fileName.startsWith("/")) {
            path += "/";
        }

        // remove slash at the beginning
        if(path.startsWith("/")) {
            path = path.substring(1);
        }

        return "user-" + user.getId() + "-files/" + path + fileName;
    }
}
