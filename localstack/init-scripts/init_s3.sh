#!/bin/bash
echo "--- Створення S3 бакету 'car-dealership-s3-dev' ---"

# Використовуємо awslocal (вбудовано в контейнер LocalStack)
# Примітка: назва бакета має співпадати з назвою у application-local.yml
awslocal s3 mb s3://car-dealership-s3-dev --region us-east-1

echo "--- Створення S3 бакету завершено ---"