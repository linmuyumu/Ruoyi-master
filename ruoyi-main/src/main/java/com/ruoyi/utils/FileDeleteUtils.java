package com.ruoyi.utils;

import java.io.File;

/**
 * @author LinMu
 * @description 描述：删除指定路径下的文件及文件夹
 * @date 2022年06月15日
 */
public class FileDeleteUtils {

    public static boolean deleteFileOrDirectory(String fileName) {
        // fileName是路径或者file.getPath()获取的文件路径
        File file = new File(fileName);
        if (file.exists()) {
            if (file.isFile()) {
                // 是文件，调用删除文件的方法
                return deleteFile(fileName);
            } else {
                // 是文件夹，调用删除文件夹的方法
                return deleteDirectory(fileName);
            }
        } else {
            System.out.println("文件或文件夹删除失败：" + fileName);
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     * @return 删除成功返回true,失败返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
            System.out.println("删除文件成功：" + fileName);
            return true;
        } else {
            System.out.println("删除文件失败：" + fileName);
            return false;
        }
    }

    /**
     * 删除文件夹
     * 删除文件夹需要把包含的文件及文件夹先删除，才能成功
     * @param directory 文件夹名
     * @return 删除成功返回true,失败返回false
     */
    public static boolean deleteDirectory(String directory) {
        // directory不以文件分隔符（/或\）结尾时，自动添加文件分隔符，不同系统下File.separator方法会自动添加相应的分隔符
        if (!directory.endsWith(File.separator)) {
            directory = directory + File.separator;
        }
        File directoryFile = new File(directory);
        // 判断directory对应的文件是否存在，或者是否是一个文件夹
        if (!directoryFile.exists() || !directoryFile.isDirectory()) {
            System.out.println("文件夹删除失败，文件夹不存在" + directory);
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件和文件夹
        File[] files = directoryFile.listFiles();
        // 循环删除所有的子文件及子文件夹
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } else {  // 删除子文件夹
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            System.out.println("删除失败");
            return false;
        }
        // 最后删除当前文件夹
        if (directoryFile.delete()) {
            System.out.println("删除成功：" + directory);
            return true;
        } else {
            System.out.println("删除失败：" + directory);
            return false;
        }
    }
}
