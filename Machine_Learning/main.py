import torch
import streamlit as st
from transformers import BertTokenizer, BertForSequenceClassification



@st.cache(allow_output_mutation=True)
def get_model():
    tokenizer = BertTokenizer.from_pretrained("indobenchmark/indobert-base-p1")
    model = BertForSequenceClassification.from_pretrained("indobenchmark/indobert-base-p1")

    return tokenizer,model


tokenizer,model = get_model()

input = st.text_area('Masukan Text')
button = st.button("Klasiifikasi")

d = {
    1: 'urgent',
    2: 'Harus Di Selesaikan',
    3: 'Biasa',
    4: 'Aspirasi',
    5: 'good aspiration'
}

if input and button:
    test = tokenizer.encode([input], return_tensors='pt')
    output = model(test)
    st.write("logits: ", output.logits)
    pred = int(torch.argmax(output.logits))+1
    print(pred)