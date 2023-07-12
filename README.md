# Парсер Habr

Этот проект представляет собой программу на языке Java, которая парсит данные с веб-сайта Habr. Она извлекает информацию о статьях, такую как заголовок, автор, время чтения, просмотры, темы и метки. Полученные данные сохраняются в CSV-файле с названием "Articles.csv".

## Начало работы

Чтобы запустить программу, выполните следующие шаги:

1. Клонируйте репозиторий на свой компьютер.
2. Откройте проект в выбранной среде разработки Java.
3. Убедитесь, что в вашем проекте добавлены следующие зависимости:
   - Jsoup (версия 1.16.1)
   - OpenCSV (версия 5.7.1)
4. Соберите и запустите класс `Main`.

## Использование

При запуске программы вам будет предложено ввести количество записей, которые вы хотите получить. Обратите внимание, что парсер извлекает статьи пакетами по 20 штук. Поэтому, если вы вводите число, которое не делится на 20, программа извлечет наиболее близкое кратное 20 число записей.

Извлеченные данные о статьях будут сохранены в файле "Articles.csv", расположенном в директории проекта.

## Структура проекта

Проект состоит из следующих файлов:

- `Main.java`: Это главный класс, который содержит точку входа в программу. Он запрашивает у пользователя количество записей, запускает процесс парсинга и записывает данные в CSV-файл.
- `Article.java`: В этом классе определена структура объекта статьи. Он имеет поля для заголовка, автора, времени чтения, просмотров, тем и меток.

## Зависимости

Для этого проекта используются две внешние библиотеки:

- Jsoup: Она используется для парсинга HTML-контента с веб-сайта Habr. Используется версия 1.16.1.
- OpenCSV: Она используется для записи полученных данных в CSV-файл. Используется версия 5.7.1.

Убедитесь, что вы добавили эти зависимости в свой проект перед запуском программы.
