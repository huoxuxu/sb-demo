package com.hxx.sbcommon.common.demo;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-25 16:12:54
 **/
public class PackageScanUtil {
    /**
     * 类名后缀
     */
    private static final String class_suffix = ".class";

    /**
     * 根据类接口查到所有的class
     *
     * @param clazz 接口文件
     * @return class
     */
    public static <T> List<Class<T>> getAllClassByInterface(Class<T> clazz) {
        List<Class<T>> list = new ArrayList<>();
        try {
            List<Class> allClass = getAllClass(clazz.getPackage().getName());
            // 循环判断路径下的所有类是否实现了指定的接口 并且排除接口类自己

            for (int i = 0; i < allClass.size(); i++) {
                if (clazz.isAssignableFrom(allClass.get(i))) {
                    if (!clazz.equals(allClass.get(i))) {
                        // 自身并不加进去
                        list.add(allClass.get(i));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * 根据类接口查到所有的class
     *
     * @param clazz 接口文件
     * @return class
     */
    public static List<Class> getAllClassByAnnotation(Class clazz) {
        try {
            List<Class> allClass = getAllClass(clazz.getPackage().getName());
            return allClass.stream().filter((a) -> {
                return a.isAnnotationPresent(clazz);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据类接口查到所有的class(指定包名)
     *
     * @param clazz 接口文件
     * @return class
     */
    public static List<Class> getAllClassByAnnotation(Class clazz, String packageName) {
        try {
            List<Class> allClass = getAllClass(packageName);
            return allClass.stream().filter((a) -> {
                return a.isAnnotationPresent(clazz);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从一个指定路径下查找所有的类
     *
     * @param packagename
     */
    private static List<Class> getAllClass(String packagename) {
        List<String> classNameList = getClassPathsByPackage(packagename);
        List<Class> list = classNameList.stream().map((b) -> {
            try {
                return Class.forName(b);
            } catch (Throwable e) {
                return null;
            }
        }).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        return list;
    }

    /**
     * 获取某包下所有类
     *
     * @param packageName 包名
     * @return 类的完整名称
     */
    public static List<String> getClassPathsByPackage(String packageName) {
        List<String> fileNames = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        URL url = loader.getResource(packagePath);
        if (url != null) {
            String type = url.getProtocol();
            if (type.equals("file")) {
                String fileSearchPath = url.getPath();
                fileSearchPath = fileSearchPath.substring(0, fileSearchPath.indexOf("/classes"));
                fileNames = getClassPathsByFile(fileSearchPath);
            } else if (type.equals("jar")) {
                try {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    JarFile jarFile = jarURLConnection.getJarFile();
                    fileNames = getClassPathsByJar(jarFile);
                } catch (IOException e) {
                    throw new RuntimeException("open Package URL failed：" + e.getMessage());
                }
            } else {
                throw new RuntimeException("file system not support! cannot load MsgProcessor！");
            }
        }
        return fileNames;
    }

    /**
     * 从项目文件获取某包下所有类
     *
     * @param filePath 文件路径
     * @return 类的完整名称
     */
    private static List<String> getClassPathsByFile(String filePath) {
        List<String> classPaths = new ArrayList<String>();
        try {
            Files.walkFileTree(Paths.get(new File(filePath).getAbsolutePath()), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String filePath = file.toFile().getPath();
                    if (filePath.endsWith(class_suffix)) {
                        filePath = filePath.substring(filePath.indexOf(File.separator + "classes") + 9, filePath.lastIndexOf("."));
                        filePath = filePath.replace(File.separator, ".").replace("/", ".").replace("\\", ".");
                        classPaths.add(filePath);
                    }
                    return super.visitFile(file, attrs);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("walk files error!", e);
        }
        return classPaths;
    }

    /**
     * 从jar获取某包下所有类
     *
     * @return 类的完整名称
     */
    private static List<String> getClassPathsByJar(JarFile jarFile) {
        List<String> myClassName = new ArrayList<String>();
        try {
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(class_suffix)) {
                    entryName = entryName.replace(File.separator, ".").replace("/", ".").replace("\\", ".").substring(0, entryName.lastIndexOf("."));
                    myClassName.add(entryName);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("发生异常:" + e.getMessage());
        }
        return myClassName;
    }
}
