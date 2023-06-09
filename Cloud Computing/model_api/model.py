import numpy as np
import torch
import evaluate
import pandas as pd
import joblib
from transformers import TrainingArguments
from transformers import BertTokenizer, BertForSequenceClassification
from sklearn.model_selection import train_test_split
from transformers import Trainer, metrics

df = pd.read_excel("test2.xlsx")

tokenizer = BertTokenizer.from_pretrained("indobenchmark/indobert-base-p1")
model = BertForSequenceClassification.from_pretrained("indobenchmark/indobert-base-p1")

tokens = tokenizer.encode('Sampah ada di mana mana', return_tensors='pt')

result = model(tokens)

logits = result.logits
prediction = int(torch.argmax(logits)) + 1

joblib.dump(result, "BERT.joblib")
joblib.dump(tokens, "Tokenizer.joblib")

def sentiment_score(clean_text):
    tokens = tokenizer.encode(clean_text, return_tensors='pt')
    result = model(tokens)
    return int(torch.argmax(result.logits)) + 1

df['sentiment'] = df['clean_text'].apply(lambda x: sentiment_score(str(x)))

feature = df['preprocessed_text']
label = df['sentiment']

X_train, X_test, y_train, y_test = train_test_split(feature, label, random_state=100, test_size=0.2)

train_args = TrainingArguments(output_dir="test")

def compute_metrics(eval_pred):
    logits, label = eval_pred
    predictions = np.argmax(logits, axis=1)
    return metrics.compute(predictions=predictions, references=label)

train_args = TrainingArguments(output_dir="test", evaluation_strategy="epoch", optim="adamw_torch")

trainer = Trainer(
    model=model,
    args=train_args,
    train_dataset=X_train,
    eval_dataset=X_test,
    compute_metrics=compute_metrics
)

trainer.train()