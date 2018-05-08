package core.builders.resources;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import utils.SDEUtils;

import java.io.*;

public class ResourceFileMapper {
    private static Logger log = Logger.getLogger(ResourceFileMapper.class);

    private static final String RESOURCE_DIR = "/var/www/kilwaz/resources/";
    private static final String RESOURCE_DIR_JS = "/var/www/kilwaz/resources/js/";
    private static final String RESOURCE_DIR_CSS = "/var/www/kilwaz/resources/css/";
    private static final String RESOURCE_DIR_WEB_FONTS = "/var/www/kilwaz/resources/webfonts/";

    public static void buildResources() {
        clearResourceDirectory();
        // Auto find files in js folder?
        buildJS();
        buildCSS();
        buildWebFonts();
        runLessCss();
        removeLessFiles();
    }

    private static void removeLessFiles() {
        File folder = new File(RESOURCE_DIR_CSS);
        File fList[] = folder.listFiles();
        // Searches .less
        for (int i = 0; i < fList.length; i++) {
            File pes = fList[i];
            if (pes.getName().endsWith(".less")) {
                boolean success = fList[i].delete();
            }
        }
    }

    private static void clearResourceDirectory() {
        log.info("Cleaning resource directory (" + RESOURCE_DIR + ")");
        File resourceDirectory = new File(RESOURCE_DIR);
        File resourceDirectoryJS = new File(RESOURCE_DIR + "js/");
        File resourceDirectoryCSS = new File(RESOURCE_DIR + "css/");
        File resourceDirectoryWebFonts = new File(RESOURCE_DIR + "webfonts/");
        try {
            FileUtils.cleanDirectory(resourceDirectory);
            FileUtils.forceMkdir(resourceDirectoryJS);
            FileUtils.forceMkdir(resourceDirectoryCSS);
            FileUtils.forceMkdir(resourceDirectoryWebFonts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runLessCss() {
        try {
            log.info("Running Less CSS");
            String s;
            Process p = Runtime.getRuntime().exec("/home/kilwaz/cen/node_modules/less/bin/lessc /var/www/kilwaz/resources/css/styles.less /var/www/kilwaz/resources/css/styles.css");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("Less CSS: " + s);
            p.waitFor();
            System.out.println("exit: " + p.exitValue());
            p.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void buildWebFonts() {
        for (File file : SDEUtils.getResourcesAsFiles("web/static/webfonts/")) {
            build("/" + file.getParent() + "/", file.getName(), RESOURCE_DIR_WEB_FONTS);
        }
    }

    private static void buildJS() {
        for (File file : SDEUtils.getResourcesAsFiles("web/static/js/")) {
            build("/" + file.getParent() + "/", file.getName(), RESOURCE_DIR_JS);
        }
    }

    private static void buildCSS() {
        for (File file : SDEUtils.getResourcesAsFiles("web/static/css/")) {
            build("/" + file.getParent() + "/", file.getName(), RESOURCE_DIR_CSS);
        }
    }

    private static void build(String directoryLocation, String resourceName, String outputPath) {
        InputStream stream = null;
        OutputStream resStreamOut = null;

        String constructedResourceName = directoryLocation + resourceName;

        try {
            stream = ResourceFileMapper.class.getResourceAsStream(constructedResourceName);

            if (stream == null) {
                log.info("Cannot get resource \"" + constructedResourceName + "\" from ear file.");
                return;
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            resStreamOut = new FileOutputStream(outputPath + resourceName);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            log.info("Copied " + directoryLocation + resourceName + " to resource directory (" + outputPath + ")");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (resStreamOut != null) {
                    resStreamOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
