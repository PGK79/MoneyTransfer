# Курсовой проект «Сервис перевода денег»

Сервис перевода денег — REST приложение предоставляющее интерфейс для перевода денег с одной карты на другую по заранее описанной спецификации  

## Описание проекта
Приложение предоставляет REST-интерфейс для интеграции с веб-приложением (FRONT), с возможностью использовать его функционала, без необходимости доработки. 

Дополнительно реализована возможность логирования проведенных операций. Все изменения записываются в файл — лог переводов c указанием:
* даты;
* времени;
* карты, с которой было списание;
* карты зачисления;
* суммы;
* комиссии;
* результата операции, если был.

### Информация для запуска и оценки проекта
* Порт: 5500
######
* Docker образ клиентская часть: **transferfrontend:1.0**
######
* Docker образ серверная часть:  **transferbackend:1.0**
######
* Создание и запуск Docker контейнера:  
**docker run -itd --name back -p 5500:5500 transferbackend:1.0**
######
* Пример запроса (платеж пройдет):
  * Карта откуда перевод № 1111111111111111 ММ/ГГ 10/24 СМС 123
  * Карта куда переводить № 2222222222222222
  * Сумма 100
  * Нажать Отправить
  ######
* Пример запроса (платеж не пройдет - Не достаточная сумма):
    * Карта откуда перевод № 2222222222222222 ММ/ГГ 10/24 СМС 123
    * Карта куда переводить № 1111111111111111
    * Сумма 1000
    * Нажать Отправить
  ######
* Пример запроса (платеж не пройдет - Не верный номер карты):
    * Карта откуда перевод № 3333222222222222 ММ/ГГ 10/24 СМС 123
    * Карта куда переводить № 1111111111111111
    * Сумма 10
    * Нажать Отправить
  
### Реализация проекта

- Приложение разработано с использованием Spring Boot.
- Использован сборщик пакетов gradle/maven.
- Для запуска используется Docker, Docker Compose .
- Код покрыт юнит-тестами с использованием mockito.
- Добавлены интеграционные тесты с использованием testcontainers.

### Описание интеграции с FRONT
**Запросы:** формируются самим FRONT приложением на URL состоящий из http://localhost:5500 и эндпоинтов:

- `/transfer` - принимает объект с данными формы
- `/confirmOperation` - принимает объект с id операции и секретным кодом


**Клиентская часть доступна по адресам:**
#### На localhost: http://localhost:3000/card-transfer

##### Для  запуска клиентской части необходимо
1. Склонировать репозиторий https://github.com/serp-ya/card-transfer.git
1. Установить Node.js 12.13.0 (более новые версии не гарантируют обратную совместимость)
1. Открыть корневую директорию проекта и выполнить команду **npm i**
1. Для запуска приложения необходимо выполнить команду **npm run start** в корневой папке проекта

#### Развёрнутое демо-приложение: https://serp-ya.github.io/card-transfer/

