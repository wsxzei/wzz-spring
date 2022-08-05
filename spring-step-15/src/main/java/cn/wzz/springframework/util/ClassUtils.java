package cn.wzz.springframework.util;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ClassUtils {

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
        }
        return cl;
    }

    /**
     * Check whether the specified class is a CGLIB-generated class.
     * @param clazz the class to check
     */
    public static boolean isCglibProxyClass(Class<?> clazz) {
        return (clazz != null && isCglibProxyClassName(clazz.getName()));
    }

    /**
     * Check whether the specified class name is a CGLIB-generated class.
     * @param className the class name to check
     */
    public static boolean isCglibProxyClassName(String className) {
        return (className != null && className.contains("$$"));
    }

    public static Set<Class<?>> scanPackageByAnnotation(String basePackage, Class<?> annotationClass) throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("");
        String basePath = resource.getPath() + basePackage.replace('.', File.separatorChar);
        File file = new File(basePath);

        return doScanPackageByAnnotation(file, annotationClass);
    }

    private static Set<Class<?>> doScanPackageByAnnotation(File file, Class<?> annotationClass) throws ClassNotFoundException {
        Set<Class<?>> result = new HashSet<>();
        File[] subFiles = file.listFiles();

        for(File subFile: subFiles){
            if(subFile.isDirectory()){
                result.addAll(doScanPackageByAnnotation(subFile, annotationClass));
            }else{
                String filePath = subFile.getAbsolutePath();
                int startIdx = filePath.lastIndexOf("classes\\");
                int endIdx = filePath.lastIndexOf(".class");
                if(startIdx != -1 && endIdx != -1 && startIdx < endIdx){
                    String className = filePath.substring(startIdx + 8, endIdx).replace(File.separatorChar, '.');
                    Class<?> clazz = Class.forName(className);

                    if(clazz.isAnnotationPresent((Class<? extends Annotation>) annotationClass))
                        result.add(clazz);
                }
            }
        }
        return result;
    }
}
