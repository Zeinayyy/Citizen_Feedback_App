import firebase_admin
from firebase_admin import credentials, auth
from flask import Flask, request, jsonify
import torch
from transformers import BertTokenizer, BertForSequenceClassification

app = Flask(__name__)
cred = credentials.Certificate("./servicesAcc.json")
firebase_admin.initialize_app(cred)

tokenizer = None
model = None

d = {
    1: 'urgent',
    2: 'Harus Di Selesaikan',
    3: 'Biasa',
    4: 'Aspirasi',
    5: 'good aspiration'
}

def load_model_and_tokenizer():
    global tokenizer, model
    tokenizer = BertTokenizer.from_pretrained("indobenchmark/indobert-base-p1")
    model = BertForSequenceClassification.from_pretrained("indobenchmark/indobert-base-p1")

load_model_and_tokenizer()

@app.route('/predict', methods=['POST'])
def predict():
    # Mendapatkan token bearer dari header Authorization
    bearer_token = request.headers.get('Authorization')

    # Validasi token bearer
    if not bearer_token or ' ' not in bearer_token:
        return jsonify({'error': 'Unauthorized', 'statusCode': 401}), 401
    
    # Validate content type
    if request.content_type != 'application/json':
        return jsonify({'error': 'Invalid content type. Expected JSON.', 'statusCode': 400}), 400
    
    # Validasi text
    data = request.get_json()
    if not data or 'text' not in data:
        return jsonify({'error': 'Invalid JSON data. Missing required fields.', 'statusCode': 400}), 400

    try:
        # Mengambil text dari data
        text = data['text']

        # Verifikasi token bearer dengan Firebase Admin SDK
        token = bearer_token.split(' ')[1]
        decoded_token = auth.verify_id_token(token)

        input_text = data['text']

        test = tokenizer.encode([input_text], return_tensors='pt')
        output = model(test)

        pred = int(torch.argmax(output.logits))+1
        classification = d[pred]

        return jsonify({'data': {'classification':classification, 'logits' : output.logits.tolist()}, 'statusCode': 200}), 200
    except auth.InvalidIdTokenError as e:
        error_message = str(e)
        return jsonify({'error': error_message, 'statusCode': 401}), 401

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
