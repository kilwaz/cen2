package requests.pages;

import core.Request;
import data.imports.XMLImporter;
import error.Error;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import requests.annotations.JSP;
import requests.annotations.RequestName;
import utils.AppParams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RequestName("import")
@JSP("import.jsp")
public class Import extends Request {
    private static Logger log = Logger.getLogger(Import.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
//        log.info("Getting the import");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        log.info("Doing a file post");
        File importFile = null;

        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            log.info("File size = " + items.size());

            for (FileItem item : items) {
                if (!item.isFormField()) {
                    String fileName = FilenameUtils.getName(item.getName());
                    InputStream fileContent = item.getInputStream();

                    importFile = new File(AppParams.getUploadFileTmpLocation() + fileName);

                    try (FileOutputStream fop = new FileOutputStream(importFile)) {
                        IOUtils.copy(fileContent, fop);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    log.info("Saved file '" + fileName + "'");
                }
            }
        } catch (FileUploadException | IOException ex) {
            Error.FILE_UPLOAD_ERROR.record().create(ex);
        }

        if (importFile != null && importFile.exists()) {
            log.info("Trying to import the XML");
            XMLImporter.build().file(importFile).execute();
        }

        log.info("Out the end");
    }
}