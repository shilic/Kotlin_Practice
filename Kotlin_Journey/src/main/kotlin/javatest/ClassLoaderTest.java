package javatest;

import com.sun.webkit.plugin.Plugin;
import jdk.jfr.internal.tool.Main;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;

public class ClassLoaderTest {
    public static void loadPlugins(File jarFile) throws Exception {
        // 1. 先通过 JarFile 扫描所有类名
        List<String> classNames = new ArrayList<>();
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName()
                            .replace('/', '.')
                            .replace(".class", "");
                    classNames.add(className);
                }
            }
        }

        // 2. 创建 URLClassLoader（注意：父加载器要能加载 Plugin 接口）
        URL jarUrl = jarFile.toURI().toURL();
        URLClassLoader loader = new URLClassLoader(
                new URL[]{jarUrl},
                Main.class.getClassLoader()  // 父加载器包含 Plugin 接口
        );

        // 3. 遍历类名，加载并检查
        for (String className : classNames) {
            try {
                Class<?> clazz = loader.loadClass(className);
                if (Plugin.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                    Plugin plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();
                    // plugin.execute();
                }
            } catch (Exception e) {
                // 处理加载失败的类（比如非插件类）
            }
        }

        // 注意：不要在插件使用期间关闭 loader，否则类会失效
        // 通常将 loader 保存在某个地方，在插件卸载时再关闭
    }
}
