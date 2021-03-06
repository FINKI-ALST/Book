package mk.ukim.finki.wp.exam.util;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CodeExtractor {

    public static void submitSourcesAndLogs() throws JsonProcessingException {
        File root = new File(".");
        System.out.println(root.getAbsolutePath());

        File basePackage = new File(root, "src/main/java/mk/ukim/finki/wp/exam/example/");
        System.out.println(basePackage.getAbsolutePath());
        List<File> javaFiles = findJavaFiles(basePackage, ".java");
        Map<String, String> content = readFilesContent(javaFiles);

        File resources = new File(root, "src/main/resources");
        List<File> properties = findJavaFiles(resources, ".properties");
        List<File> templates = findJavaFiles(resources, ".html");
        templates.addAll(properties);
        Map<String, String> htmlAndTemplates = readFilesContent(templates);

        content.putAll(htmlAndTemplates);

        SubmissionHelper.submitSource(content);
    }

    public static List<File> findJavaFiles(File root, String extension) {
        List<File> javaFiles = new ArrayList<>();
        File[] files = root.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                javaFiles.addAll(findJavaFiles(f, extension));
            } else if (f.getName().endsWith(extension)) {
                javaFiles.add(f);
            }
        }
        return javaFiles;
    }

    public static Map<String, String> readFilesContent(List<File> javaFiles) {
        Map<String, String> fileContent = new TreeMap<>();
        for (File f : javaFiles) {
            try {
                String content = Files.readString(f.toPath());
                fileContent.put(f.getAbsolutePath(), content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileContent;
    }
}
