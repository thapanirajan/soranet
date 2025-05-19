package com.soranet.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
// import java.util.UUID; // If you want universally unique filenames

import jakarta.servlet.http.Part;

public class ImageUtil {

    public static final String WEB_RELATIVE_UPLOAD_DIR_ROOT = "resources/uploads";

    public static final String GENERIC_DEFAULT_IMAGE_FILENAME = "default_pp.jpg"; 

    public String getImageNameFromPart(Part part) {
        if (part == null) return GENERIC_DEFAULT_IMAGE_FILENAME;
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp == null) {
            return GENERIC_DEFAULT_IMAGE_FILENAME;
        }

        String[] items = contentDisp.split(";");
        String submittedFileName = null;

        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                submittedFileName = s.substring(s.indexOf("=") + 1).trim().replace("\"", "");
                submittedFileName = Paths.get(submittedFileName).getFileName().toString();
                break;
            }
        }

        if (submittedFileName == null || submittedFileName.isEmpty() || submittedFileName.equals("null") || submittedFileName.equalsIgnoreCase("undefined")) {
            return GENERIC_DEFAULT_IMAGE_FILENAME; // Or return null to indicate no file selected
        }

        String sanitizedFileName = submittedFileName.replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9._-]", "");
        if (sanitizedFileName.isEmpty() || sanitizedFileName.matches("^_+\\..*") || sanitizedFileName.equals("_")) {
            // String ext = getFileExtension(submittedFileName);
            // sanitizedFileName = "uploaded_image" + (ext.isEmpty() ? ".png" : ext);
            // For simplicity, if sanitization fails badly, fall back or use UUID
             return "processed_" + System.currentTimeMillis() + getFileExtension(submittedFileName);
        }
        // To make it more unique if not deleting old one immediately and rely on name
        // String uniquePrefix = UUID.randomUUID().toString().substring(0, 8);
        // return uniquePrefix + "_" + sanitizedFileName;
        return sanitizedFileName;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) return "";
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot == -1 || lastIndexOfDot == fileName.length() - 1) {
            return ""; // No extension or ends with a dot
        }
        return fileName.substring(lastIndexOfDot);
    }

    public String uploadImageToWorkspace(Part part, String absoluteWorkspaceWebappPath, String relativeSaveFolderInsideUploads) {
        if (part == null || part.getSize() == 0) {
            return null; // Indicate no file was processed for upload
        }

        String imageName = getImageNameFromPart(part);
        // If getImageNameFromPart decided it's a default because no file was truly submitted
        if (imageName.equals(GENERIC_DEFAULT_IMAGE_FILENAME) && (part.getSubmittedFileName() == null || part.getSubmittedFileName().isEmpty() || part.getSubmittedFileName().equalsIgnoreCase("undefined"))) {
            return null; // No actual file was selected by the user
        }

        try {
            if (!relativeSaveFolderInsideUploads.startsWith("/")) {
                relativeSaveFolderInsideUploads = "/" + relativeSaveFolderInsideUploads;
            }
            if (relativeSaveFolderInsideUploads.endsWith("/")) {
                relativeSaveFolderInsideUploads = relativeSaveFolderInsideUploads.substring(0, relativeSaveFolderInsideUploads.length() - 1);
            }

            String diskSaveDirectoryPath = absoluteWorkspaceWebappPath +
                                           File.separator + WEB_RELATIVE_UPLOAD_DIR_ROOT.replace('/', File.separatorChar) +
                                           relativeSaveFolderInsideUploads.replace('/', File.separatorChar);

            File saveDir = new File(diskSaveDirectoryPath);
            if (!saveDir.exists()) {
                if (!saveDir.mkdirs()) {
                    System.err.println("ImageUtil: Failed to create directory: " + diskSaveDirectoryPath);
                    return null;
                }
            }

            String fullDiskFilePath = diskSaveDirectoryPath + File.separator + imageName;
            part.write(fullDiskFilePath);
            System.out.println("ImageUtil: Image uploaded to (workspace): " + fullDiskFilePath);

            String webRelativePathForDB = WEB_RELATIVE_UPLOAD_DIR_ROOT +
                                          relativeSaveFolderInsideUploads +
                                          "/" + imageName;
            System.out.println("ImageUtil: Relative path for DB: " + webRelativePathForDB);
            return webRelativePathForDB;

        } catch (IOException e) {
            System.err.println("ImageUtil: Error uploading image to workspace: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}