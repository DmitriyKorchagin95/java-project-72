### Hexlet tests and linter status:
[![Actions Status](https://github.com/DmitriyKorchagin95/java-project-78/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/DmitriyKorchagin95/java-project-78/actions) [![build](https://github.com/DmitriyKorchagin95/java-project-72/actions/workflows/build.yml/badge.svg)](https://github.com/DmitriyKorchagin95/java-project-72/actions/workflows/build.yml)
___
### Sonarqube status:
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=DmitriyKorchagin95_java-project-72&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=DmitriyKorchagin95_java-project-72)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=DmitriyKorchagin95_java-project-72&metric=bugs)](https://sonarcloud.io/summary/new_code?id=DmitriyKorchagin95_java-project-72)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=DmitriyKorchagin95_java-project-72&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=DmitriyKorchagin95_java-project-72)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=DmitriyKorchagin95_java-project-72&metric=coverage)](https://sonarcloud.io/summary/new_code?id=DmitriyKorchagin95_java-project-72)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=DmitriyKorchagin95_java-project-72&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=DmitriyKorchagin95_java-project-72)
___
## Анализатор страниц
Это веб-приложение для анализа сайтов.
Оно позволяет добавлять URL-адреса, проверять их доступность и извлекать SEO-метаданные: `title`, `h1` и `description`.

## Функционал
- Добавление URL-адресов
- Нормализация URL (исключение дубликатов)
- Проверка доступности сайта (HTTP статус)
- Парсинг HTML страницы
- Извлечение SEO-метаданных
- Результаты проверки
    - `status`
    - `title`
    - `h1`
    - `description`
    - `дата последней проверки`
---
## Локальная сборка и запуск:
```bash
git clone https://github.com/DmitriyKorchagin95/java-project-72
cd java-project-72
make run-dist
```
___
# Демо
### [Application on Render](https://java-project-72-ywlv.onrender.com/)
