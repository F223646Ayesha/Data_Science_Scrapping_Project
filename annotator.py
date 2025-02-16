from transformers import pipeline
import pandas as pd
import time

# Load the zero-shot classification model
classifier = pipeline("zero-shot-classification", model="facebook/bart-large-mnli")

# Define categories for classification
categories = [
    "Deep Learning",
    "Computer Vision",
    "Natural Language Processing (NLP)",
    "Reinforcement Learning",
    "Optimization"
]

# Function to classify a paper based on its title and abstract
def classify_paper(title, abstract):
    try:
        text = f"Title: {title}\n\nAbstract: {abstract}"
        result = classifier(text, categories)
        return result["labels"][0]  # Return the top predicted category
    except Exception as e:
        print(f"Error: {e}")
        return "Error"

# Load the dataset
file_path = "neurips_papers.csv"
df = pd.read_csv(file_path)

print("Column names:", df.columns)

df.rename(
    columns={
        df.columns[0]: 'Title',
        df.columns[1]: 'Abstract',
        df.columns[2]: 'PDF Link'
    },
    inplace=True
)

# Ensure required columns exist
if 'Title' not in df.columns or 'Abstract' not in df.columns:
    raise ValueError("Required columns ('Title', 'Abstract') are missing. Check the CSV file structure.")


df["Category"] = None

# Process each paper and classify it
for index, row in df.iterrows():
    title = row["Title"]
    abstract = row.get("Abstract", "")

    print(f"Annotating paper {index + 1}/{len(df)}: {title}")
    category = classify_paper(title, abstract)
    df.at[index, "Category"] = category

    # Add a delay to avoid rate limits or excessive resource usage
    time.sleep(1)

df.to_csv("annotated_papers.csv", index=False)

print("Annotation completed and saved to 'annotated_papers.csv'.")
