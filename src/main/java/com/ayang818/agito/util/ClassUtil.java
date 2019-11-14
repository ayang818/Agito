package com.ayang818.agito.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @ClassName ClassUtil
 * @Dessription TODO
 * @Author 杨丰畅
 * @Date 2019/10/22 19:09
 **/
public class ClassUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String className, boolean isInitial) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitial, getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error(className + " class not found");
            throw new RuntimeException(e);
        }
        return cls;
    }

    public static Set<Class<?>> getClassSet(String packageName) {
        HashSet<Class<?>> classSet = new HashSet<>();
        Enumeration<URL> root;
        try {
            root = getClassLoader().getResources(packageName.replace(".", "/"));
            while (root.hasMoreElements()) {
                URL node = root.nextElement();
                if (node != null) {
                    String protocol = node.getProtocol();
                    if ("files".equals(protocol)) {
                        String packagePath = node.getPath().replaceAll("%20", " ");
                        addClasses(classSet, packagePath, packageName);
                    } else if("jar".equals(protocol)) {
                        JarURLConnection jarURLConnection = (JarURLConnection) node.openConnection();
                        if (jarURLConnection != null) {
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if (jarFile != null) {
                                Enumeration<JarEntry> jarEntries = jarFile.entries();
                                if (jarEntries.hasMoreElements()) {
                                    JarEntry jarEntry = jarEntries.nextElement();
                                    String className = jarEntry.getName();
                                    if (className.endsWith(".class")) {
                                        className.substring(0, className.indexOf(".")).replace("/", ".");
                                        addClass(classSet, className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("get classSet failed", e);
        }
        return classSet;
    }

    private static void addClass(Set<Class<?>> classSet, String className) {
        Class<?> cls = loadClass(className, false);
        LOGGER.info("add "+className+" to classSet");
        classSet.add(cls);
    }

    private static void addClasses(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
        for (File file : files) {
            String filename = file.getName();
            if (file.isFile()) {
                String className = filename.substring(0, filename.lastIndexOf("."));
                if (!className.isEmpty()) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet, className);
            } else if (file.isDirectory()) {
                String newPackagePath = packagePath+"/"+filename;
                if (!packageName.isEmpty()) {
                    filename = packageName+"."+filename;
                }
                addClasses(classSet, newPackagePath, filename);
            }
        }
    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> aClass = loadClass(className, false);
        classSet.add(aClass);
    }
}
