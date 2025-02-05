package d_s;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.CloseableHttpResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class NeurIPSWebScraper {

    private static final String BASE_URL = "https://papers.nips.cc";
    private static final String DOWNLOAD_DIR = "F:\\neurips_papers_r\\";
    private static final String CSV_FILE = DOWNLOAD_DIR + "neurips_papers.csv";
    private static final int THREAD_COUNT = 10;
    private static final int TIMEOUT = 60000;

    public static void main(String[] args) {
        try {
            Files.createDirectories(Paths.get(DOWNLOAD_DIR)); 
            initializeCSV();
        } catch (IOException e) {
            System.err.println("Failed to create download directory.");
            return;
        }
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int y = 2023; y >= 2018; y--) { 
            final int year = y;
            String yearUrl = BASE_URL + "/paper/" + year;
            String yearFolder = DOWNLOAD_DIR + year + "\\";
            
            try {
                Files.createDirectories(Paths.get(yearFolder));
                Document yearPage = Jsoup.connect(yearUrl).timeout(TIMEOUT).get();
                Elements paperLinks = yearPage.select("ul.paper-list li a[href$=Abstract-Conference.html]");
                for (Element paperLink : paperLinks) {
                    String paperUrl = BASE_URL + paperLink.attr("href");
                    executor.submit(() -> processPaper(paperUrl, yearFolder, year));
                }
            } catch (IOException e) {
                System.err.println("Failed to load: " + yearUrl);
            }
        }

        executor.shutdown();
    }
    private static void processPaper(String paperUrl, String yearFolder, int year) {
        try {
            Document paperPage = Jsoup.connect(paperUrl).timeout(TIMEOUT).get();
            Element pdfLink = paperPage.selectFirst("a[href$=Paper-Conference.pdf]");

            if (pdfLink != null) {
                String pdfUrl = BASE_URL + pdfLink.attr("href");
                String title = sanitizeFilename(paperPage.title());

                downloadPDF(pdfUrl, title, yearFolder);
                saveMetadataToCSV(title, paperUrl, pdfUrl, yearFolder, year);
            }
        } catch (IOException e) {
            System.err.println("Error processing: " + paperUrl);
        }
    }
    private static void downloadPDF(String pdfUrl, String fileName, String yearFolder) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(new HttpGet(pdfUrl));
             InputStream inputStream = response.getEntity().getContent();
             FileOutputStream outputStream = new FileOutputStream(yearFolder + fileName + ".pdf")) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("Downloaded: " + fileName);
        } catch (IOException e) {
            System.err.println("Failed to download: " + pdfUrl);
        }
    }
    private static void saveMetadataToCSV(String title, String paperUrl, String pdfUrl, String yearFolder, int year) {
        synchronized (NeurIPSWebScraper.class) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
                writer.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n", title, year, paperUrl, pdfUrl, yearFolder));
            } catch (IOException e) {
                System.err.println("Failed to write metadata to CSV.");
            }
        }
    }
    private static void initializeCSV() throws IOException {
        File file = new File(CSV_FILE);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("Title,Year,Paper URL,PDF URL,Folder Path\n");
            }
        }
    }
    private static String sanitizeFilename(String filename) {
        return filename.replaceAll("[\\\\/:*?\"<>|]", "_");
    }
}
