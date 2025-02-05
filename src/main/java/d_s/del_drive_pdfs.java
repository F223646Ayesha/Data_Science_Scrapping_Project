package d_s;

import java.io.File;

public class del_drive_pdfs {

    private static final String DOWNLOAD_DIR = "neurips_papers/";

    public static void main(String[] args) {
        // After processing and downloading, you can call this function to delete files
        deleteFiles(DOWNLOAD_DIR);
        System.out.println("Files in '" + DOWNLOAD_DIR + "' have been deleted.");
    }

    // Method to delete all files in the specified directory
    private static void deleteFiles(String dirPath) {
        File folder = new File(dirPath);
        if (folder.exists() && folder.isDirectory()) {
            // Get all the files in the directory
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    // Delete each file
                    if (file.isFile()) {
                        if (file.delete()) {
                            System.out.println("Deleted file: " + file.getName());
                        } else {
                            System.out.println("Failed to delete file: " + file.getName());
                        }
                    }
                }
            }
        } else {
            System.out.println("The specified directory does not exist.");
        }
    }
}
