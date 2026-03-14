#!/bin/bash
CHROMEDRIVER_VERSION="114.0.5735.90"  # Поставьте актуальную версию
TEMP_DIR="/tmp/chromedriver"

# Создадим временную директорию
mkdir -p "${TEMP_DIR}"

# Скачиваем архив с помощью curl
curl -L -o "${TEMP_DIR}/chromedriver.zip" "https://storage.googleapis.com/chrome-for-testing-public/${CHROMEDRIVER_VERSION}/linux64/chromedriver-linux64.zip"

# Распаковываем архив
unzip -d "${TEMP_DIR}" "${TEMP_DIR}/chromedriver.zip"

# Копируем бинарник в глобальную директорию
sudo cp "${TEMP_DIR}/chromedriver" /usr/local/bin/chromedriver

# Устанавливаем права доступа
sudo chmod +x /usr/local/bin/chromedriver

# Очищаем временную директорию
rm -rf "${TEMP_DIR}"
