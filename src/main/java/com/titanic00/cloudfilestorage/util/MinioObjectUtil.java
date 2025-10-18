package com.titanic00.cloudfilestorage.util;

import io.minio.Result;
import io.minio.messages.Item;

import java.io.ByteArrayOutputStream;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    public static String getFileNameFromObjectName(String objectName) {
        String filename = objectName.substring(objectName.lastIndexOf("/"));
        if (filename.startsWith("/")) {
            return filename.substring(1);
        }

        return filename;
    }

    public static String getDirNameFromObjectName(String objectName) {
        int lastButOneIdx;

        for (int i = objectName.lastIndexOf("/") - 1; ; i--) {
            if (objectName.toCharArray()[i] == '/') {
                lastButOneIdx = i;
                break;
            }
        }

        return objectName.substring(lastButOneIdx + 1, objectName.lastIndexOf("/") + 1);
    }

    public static String getPathFromObjectName(String objectName) {
        String path = objectName.substring(objectName.indexOf("/") + 1, objectName.lastIndexOf("/") + 1);
        if (path.startsWith("/")) {
            return path.substring(1);
        }

        return path;
    }

    public static byte[] toZip(Iterable<Result<Item>> items) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(baos)) {

            for (Result<Item> item : items) {
                ZipEntry entry = new ZipEntry(Paths.get(item.get().objectName()).getFileName().toString());
                zipOut.putNextEntry(entry);
            }

            zipOut.closeEntry();
        }

        return baos.toByteArray();
    }

    public static boolean isDir(String path) {
        return path.endsWith("/");
    }
}
