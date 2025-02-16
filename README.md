# NeurIPS Paper Scraper & Annotator

This repository contains tools for scraping and annotating research papers from the NeurIPS website. It includes scripts for:
1. Scraping metadata and PDFs of research papers.
2. Annotating downloaded PDFs with custom labels or comments.

---

## Table of Contents
- [Requirements](#requirements)
- [Installation](#installation)
- [How to Run](#how-to-run)
  - [Running the Python Script](#running-the-python-script)
  - [Running the Java Script](#running-the-java-script)
  - [Running the Annotator](#running-the-annotator)
- [Output Files](#output-files)
- [Responsible Scraping](#responsible-scraping)
- [Blog Post](#blog-post)

---

## Requirements

### Python Dependencies
Ensure you have Python installed and the following libraries:

pip install requests beautifulsoup4 tqdm PyPDF2
Java Dependencies
Ensure you have Java installed and include the following libraries:

**JSoup (for HTML parsing)**
Apache HttpClient (for handling HTTP requests)
You can download the JAR files for JSoup and HttpClient or use Maven dependencies.

**Installation**
Clone the repository and navigate to the project folder:

git clone https://github.com/yourusername/neurips-scraper.git
cd neurips-scraper
**How to Run**
Running the Python Script
Run the following command to scrape papers and download PDFs:
python scraper.py
**This will:**

✅ Scrape NeurIPS papers from multiple years
✅ Download PDFs into year-based folders
✅ Save metadata to neurips_papers.csv
Running the Java Script
Compile and run the Java program:

javac -cp .;jsoup.jar;httpclient.jar NeurIPSWebScraper.java  
java -cp .;jsoup.jar;httpclient.jar NeurIPSWebScraper
**This will:**

✅ Scrape NeurIPS papers from multiple years
✅ Download PDFs into year-based folders
✅ Save metadata to output.json
Running the Annotator
The annotator allows you to add annotations (e.g., comments, labels) to downloaded NeurIPS papers.

✅ Read the annotations.csv file
✅ Apply annotations to PDFs in the specified folder
✅ Save annotated PDFs in a separate folder
Output Files
neurips_papers.csv (Python): Contains extracted paper metadata in CSV format.
Downloaded PDFs: Saved in separate folders by year.
output.json (Java): Contains extracted metadata in JSON format.
Annotated PDFs: Saved in an annotated_pdfs folder.
Responsible Scraping
🚀 Follow these best practices:

✅ Respect robots.txt – Ensure scraping is allowed.
✅ Use timeouts & delays – Avoid overwhelming servers.
✅ Avoid unnecessary requests – Download only what you need.
Blog Post
Read my blog about the scraping and annotating process on Medium:
Web Scraping for Academic Research: Challenges, Insights, and Best Practices

**Future Enhancements**
Add support for multi-threaded annotation to improve performance.
Extend the scraper to include other conferences like CVPR or ICML.
Enhance annotation options (e.g., highlighting specific text or pages in PDFs).

Let me know if you need any additional updates or customizations!
