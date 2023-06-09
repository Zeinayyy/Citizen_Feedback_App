FROM python:latest

# Mengatur direktori kerja
WORKDIR /app

# Menyalin file requirements.txt ke dalam image
COPY requirements.txt .

# Menginstal dependensi yang diperlukan
RUN pip install --no-cache-dir -r requirements.txt

# Menyalin kode aplikasi ke dalam image
COPY . .

# Menjalankan aplikasi Flask saat container berjalan
CMD ["python", "main.py"]
