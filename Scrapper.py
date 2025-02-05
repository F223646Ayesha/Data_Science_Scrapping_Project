import os
import re
import csv
import requests
import threading
from bs4 import BeautifulSoup
from concurrent.futures import ThreadPoolExecutor
from tqdm import tqdm
BASE_URL = "https://papers.nips.cc"
DOWNLOAD_DIR = r"F:\neurips_papers_python"
METADATA_FILE = os.path.join(DOWNLOAD_DIR, "neurips_papers.csv")
THREAD_COUNT = 10 
TIMEOUT = 60

os.makedirs(DOWNLOAD_DIR, exist_ok=True)

csv_lock = threading.Lock()

def sanitize_filename(filename):
    """Remove invalid characters from filenames for Windows"""
    return re.sub(r'[<>:"/\\|?*]', '_', filename)

def get_paper_links(year):
    """Scrape paper links for a given year."""
    year_url = f"{BASE_URL}/paper/{year}"
    try:
        response = requests.get(year_url, timeout=TIMEOUT)
        response.raise_for_status()
        soup = BeautifulSoup(response.text, "html.parser")
        return [(BASE_URL + a["href"], year) for a in soup.select("ul.paper-list li a[href$='Abstract-Conference.html']")]
    except requests.RequestException:
        print(f"Failed to load {year_url}")
        return []

def extract_paper_metadata(paper_url, year):
    """Extract paper metadata and download PDF into the year folder."""
    try:
        response = requests.get(paper_url, timeout=TIMEOUT)
        response.raise_for_status()
        soup = BeautifulSoup(response.text, "html.parser")

        title = soup.title.text.strip()
        authors = ", ".join(a.text.strip() for a in soup.select(".authors a"))

        year_folder = os.path.join(DOWNLOAD_DIR, str(year))
        os.makedirs(year_folder, exist_ok=True)

        pdf_link = soup.select_one("a[href$='Paper-Conference.pdf']")
        if pdf_link:
            pdf_url = BASE_URL + pdf_link["href"]
            filename = sanitize_filename(title) + ".pdf"
            file_path = os.path.join(year_folder, filename) 

            pdf_response = requests.get(pdf_url, timeout=TIMEOUT, stream=True)
            pdf_response.raise_for_status()
            with open(file_path, "wb") as f:
                for chunk in pdf_response.iter_content(chunk_size=8192):
                    f.write(chunk)
            
            print(f"Downloaded: {file_path}")

            with csv_lock:
                with open(METADATA_FILE, "a", newline="", encoding="utf-8") as csv_file:
                    writer = csv.writer(csv_file)
                    writer.writerow([title, authors, year, pdf_url, file_path])

            return [title, authors, year, pdf_url, file_path]
        else:
            print(f"No PDF found for: {paper_url}")
            return None

    except requests.RequestException:
        print(f"Failed to process: {paper_url}")
        return None

def main():
    """Main function to scrape, download, and save metadata."""
    paper_urls = []
    for year in range(2023, 2018, -1):
        paper_urls.extend(get_paper_links(year))

    print(f"Found {len(paper_urls)} papers to download.")

    with open(METADATA_FILE, "w", newline="", encoding="utf-8") as csv_file:
        writer = csv.writer(csv_file)
        writer.writerow(["Title", "Authors", "Year", "PDF URL", "File Path"])

    with ThreadPoolExecutor(max_workers=THREAD_COUNT) as executor:
        list(tqdm(executor.map(lambda args: extract_paper_metadata(*args), paper_urls), total=len(paper_urls)))

    print(f"Metadata saved to: {METADATA_FILE}")

if __name__ == "__main__":
    main()
