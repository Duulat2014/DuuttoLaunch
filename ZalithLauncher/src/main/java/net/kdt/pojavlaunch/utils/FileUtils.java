package net.kdt.pojavlaunch.utils;

import android.os.Build;
import android.system.Os;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileUtils {

    /**
     * Check if a file denoted by a String path exists.
     * @param filePath the path to check
     * @return whether it exists (same as File.exists()
     */
    public static boolean exists(String filePath){
        return new File(filePath).exists();
    }

    /**
     * Get the file name from a path/URL string.
     * @param pathOrUrl the path or the URL of the file
     * @return the file's name
     */
    public static String getFileName(String pathOrUrl) {
        int lastSlashIndex = pathOrUrl.lastIndexOf('/');
        if(lastSlashIndex == -1) return null;
        return pathOrUrl.substring(lastSlashIndex);
    }

    /**
     * Remove the extension (all text after the last dot) from a path/URL string.
     * @param pathOrUrl the path or the URL of the file
     * @return the input with the extension removed
     */
    public static String removeExtension(String pathOrUrl) {
        int lastDotIndex = pathOrUrl.lastIndexOf('.');
        if(lastDotIndex == -1) return pathOrUrl;
        return pathOrUrl.substring(0, lastDotIndex);
    }

    /**
     * Ensure that a directory exists, is a directory and is writable.
     * @param targetFile the directory to check
     * @return if the check has succeeded
     */
    public static boolean ensureDirectorySilently(File targetFile) {
        if(targetFile.isFile()) return false;
        if(targetFile.exists()) return targetFile.canWrite();
        else return targetFile.mkdirs();
    }

    /**
     * Ensure that the parent directory of a file exists and is writable
     * @param targetFile the File whose parent should be checked
     * @return if the check as succeeded
     */
    public static boolean ensureParentDirectorySilently(File targetFile) {
        File parentFile = targetFile.getParentFile();
        if(parentFile == null) return false;
        return ensureDirectorySilently(parentFile);
    }

    /**
     * Same as ensureDirectorySilently(), but throws an IOException telling why the check failed.
     * @param targetFile the directory to check
     * @throws IOException when the checks fail
     */
    public static void ensureDirectory(File targetFile) throws IOException{
        if(targetFile.isFile()) throw new IOException("Target directory is a file");
        if(targetFile.exists()) {
            if(!targetFile.canWrite()) throw new IOException("Target directory is not writable");
        }else if(!targetFile.mkdirs()) throw new IOException("Unable to create target directory");
    }

    /**
     * Same as ensureParentDirectorySilently(), but throws an IOException telling why the check failed.
     * @param targetFile the File whose parent should be checked
     * @throws IOException when the checks fail
     */
    public static void ensureParentDirectory(File targetFile) throws IOException{
        File parentFile = targetFile.getParentFile();
        if(parentFile == null) throw new IOException("targetFile does not have a parent");
        ensureDirectory(parentFile);
    }

    /**
     * Безопасное создание симлинка с фоллбэком на копирование для Android 15+
     */
    public static void makeSymlinkOrCopy(File target, File link) {
        try {
            // На Android 15 (API 35) и выше симлинки заблокированы системой безопасности
            if (Build.VERSION.SDK_INT >= 35) {
                throw new UnsupportedOperationException("Symlinks restricted on Android 15+");
            }
            if (link.exists()) {
                link.delete();
            }
            // Пытаемся создать стандартную символическую ссылку
            Os.symlink(target.getAbsolutePath(), link.getAbsolutePath());
        } catch (Exception e) {
            // Если симлинк создать не удалось, выполняем физическое копирование
            try {
                if (target.exists()) {
                    if (target.isDirectory()) {
                        copyDirectory(target, link);
                    } else {
                        if (link.getParentFile() != null) {
                            link.getParentFile().mkdirs();
                        }
                        Files.copy(target.toPath(), link.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void copyDirectory(File source, File target) throws IOException {
        if (!target.exists()) {
            target.mkdirs();
        }
        String[] files = source.list();
        if (files != null) {
            for (String file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(target, file);
                if (srcFile.isDirectory()) {
                    copyDirectory(srcFile, destFile);
                } else {
                    Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
}
