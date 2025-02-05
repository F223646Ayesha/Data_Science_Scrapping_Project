NeurIPS Paper Scraper
This repository contains two scripts (scraper.java and scraper.py) for scraping research papers from the NeurIPS website. The scripts extract paper metadata (title, authors, year, PDF URL) and download the PDFs into organized folders.

Table of Contents
Requirements
Installation
How to Run
Running the Python Script
Running the Java Script
Output Files
Responsible Scraping
Blog Post
Requirements
Python Dependencies
Ensure you have Python installed and the following libraries:

bash
Copy
Edit
pip install requests beautifulsoup4 tqdm
Java Dependencies
Ensure you have Java installed and include the following libraries:

JSoup (for HTML parsing)
Apache HttpClient (for handling HTTP requests)
You can download the JAR files for JSoup and HttpClient or use Maven dependencies.

Installation
Clone the repository and navigate to the project folder:

bash
Copy
Edit
git clone https://github.com/yourusername/neurips-scraper.git
cd neurips-scraper
How to Run
Running the Python Script
bash
Copy
Edit
python scraper.py
This will:
✅ Scrape NeurIPS papers from multiple years
✅ Download PDFs into year-based folders
✅ Save metadata to output.csv

Running the Java Script
Compile and run the Java program:

bash
Copy
Edit
javac -cp .;jsoup.jar;httpclient.jar scraper.java  
java -cp .;jsoup.jar;httpclient.jar scraper
This will:
✅ Scrape NeurIPS papers from multiple years
✅ Download PDFs into year-based folders
✅ Save metadata to output.json

Output Files
output.csv (Python): Contains extracted paper metadata in CSV format
output.json (Java): Contains extracted paper metadata in JSON format
Downloaded PDFs: Saved in separate folders by year
Responsible Scraping
🚀 Follow these best practices:
✅ Respect robots.txt – Ensure scraping is allowed
✅ Use timeouts & delays – Avoid overwhelming servers
✅ Avoid unnecessary requests – Download only what you need

Blog Post
Read my blog about the scraping process on Medium: 
