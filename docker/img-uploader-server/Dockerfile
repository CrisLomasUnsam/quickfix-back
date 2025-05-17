FROM python:3.11-slim

WORKDIR /app

RUN pip install flask

COPY uploader.py .

CMD ["python", "uploader.py"]