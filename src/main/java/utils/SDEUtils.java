package utils;

import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class SDEUtils {
    private static Logger log = Logger.getLogger(SDEUtils.class);

    private static Set<String> getResources(String path) {
        return new Reflections(path, new ResourcesScanner()).getResources(Pattern.compile(".*"));
    }

    private static String getResource(String directory, String fileName) {
        File initialFile = new File(directory + fileName);

        Set<String> paths = getResources(directory);

        for (String foundPath : paths) {
            File foundFile = new File(foundPath);

            if (foundFile.getName().equals(initialFile.getName())) {
                return foundFile.getParent() + "/" + foundFile.getName();
            }
        }

        return "";
    }

    public static File getResourceAsFile(String directory, String fileName) {
        String filePath = getResource(directory, fileName);
        return new File(filePath);
    }

    public static Set<File> getResourcesAsFiles(String path) {
        Set<String> paths = getResources(path);

        Set<File> files = new HashSet<>();

        for (String filePath : paths) {
            files.add(new File(filePath));
        }

        return files;
    }
}
