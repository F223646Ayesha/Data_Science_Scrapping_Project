from transformers import pipeline
import pandas as pd
import time

classifier = pipeline("zero-shot-classification", model="facebook/bart-large-mnli")

categories = [
    "Deep Learning",
    "Computer Vision",
    "Natural Language Processing (NLP)",
    "Reinforcement Learning",
    "Optimization"
]

def classify_paper(title, abstract):
    try:
        text = f"Title: {title}\n\nAbstract: {abstract}"
        result = classifier(text, categories)
        best_category = result["labels"][0]
        return best_category
    except Exception as e:
        print(f"Error: {e}")
        return "Error"

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

if 'Title' not in df.columns or 'Abstract' not in df.columns:
    raise ValueError("Required columns ('Title', 'Abstract') are not found after renaming. Check the CSV file structure.")

df["Category"] = None

for index, row in df.iterrows():
    title = row["Title"]
    abstract = row.get("Abstract", "")

    print(f"Annotating paper {index + 1}/{len(df)}: {title}")
    category = classify_paper(title, abstract)
    df.at[index, "Category"] = category

    time.sleep(1)

df.to_csv("annotated_papers.csv", index=False)

print("Annotation completed and saved to 'annotated_papers.csv'.")
