## StoreRestApi ##
#### Выполнил: Чунарёв Сергей. ####

Системы управления товарами для площадки электронной коммерции.

####	Реализовано: ####

- Регистрация пользователя с подтверждением через почту
- Авторизация пользователя с использованием Spring Security и JWT
- Разграничение функциональности по ролям (USER, ADMIN)
- Поддержка входа и выхода с нескольких устройств.
- Возможность создания/редактировани/удаления товаров и внутренней валюты администратором
- Приобретение внутренней валюты со списанием средств ($) у пользователя
- Счета пользователя в разной внутренней валюте (gold: 100; silver:50...)
- Приобретение товаров за внутреннюю валюту
- Пользовательский инвентарь для хранения товаров
- Вадыча исключений при ошибках
- Миграция баз данных с помощью Flayway

---

## Запуск проекта ##

<h4> Загрузка проекта </h4>

```bash
$ git clone https://github.com/chunarevsa/StoreRestApi.git
$ cd StoreRestApi
```

<h4> Создание базы данных MYSQL </h4>

База данных создаётся автоматически при отсутствии 
Если с этим возникли проблемы то:

```bash
$ create database websitechsa
```

<h4> Измените имя пользователя и пароль MySQL в application.properties </h4>

* `spring.datasource.username`
* `spring.datasource.password`

<h4> Измените имя пользователя и пароль для рассылки в mail.properties </h4>

* `spring.mail.username`
* `spring.mail.password` 

<h4> Запуск </h4>

Проект запускается с параметром `server.port:8088`

```bash
$ ./mvnw spring-boot:run   # для UNIX/Linux 
$ mvnw.cmd spring-boot:run # для Windows 
```

---

## API ##

Если вы используете Postman можно импортировать запросы из фаила 
`Website.postman_collection.json`

<h3> Аутентификация </h3>

<details>
<summary> Регистрация </summary>

```
curl --location --request POST 'localhost:8088//auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "admin@gmail.com",
    "password": "test1",
    "registerAsAdmin": true
}'
```

* "registerAsAdmin" - будет ли являться пользователь администратором
* Почта и имя пользователя должны быть уникальными

</details>

---

<details>
<summary> Подстверждение регистрации </summary>

```
curl --location --request POST 'localhost:8088//auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "admin@gmail.com",
    "password": "test1",
    "registerAsAdmin": true
}'
```

* "registerAsAdmin" - будет ли являться пользователь администратором
* Почта и имя пользователя должны быть уникальными

</details>

---

<details>
<summary> Logout </summary>

```
curl --location --request POST 'localhost:8088//auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "admin@gmail.com",
    "password": "test1",
    "registerAsAdmin": true
}'
```

* "registerAsAdmin" - будет ли являться пользователь администратором
* Почта и имя пользователя должны быть уникальными

</details>

---

<h3> Пользователь </h3>

<details>
<summary> Свой профиль </summary>

```
curl --location --request POST 'localhost:8088//auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "admin@gmail.com",
    "password": "test1",
    "registerAsAdmin": true
}'
```

* "registerAsAdmin" - будет ли являться пользователь администратором
* Почта и имя пользователя должны быть уникальными

</details>
---
<details>
<summary> Просмотр профля любого пользователя  </summary>

```
curl --location --request POST 'localhost:8088//auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "admin@gmail.com",
    "password": "test1",
    "registerAsAdmin": true
}'
```

* "registerAsAdmin" - будет ли являться пользователь администратором
* Почта и имя пользователя должны быть уникальными

</details>
---

Аутентификация
* Регистрация на основе email с подтверждение через почту
* Авторизация с использование Spring Security и JWT
* Login и Logout с нескольких устройств
* Разграничение функциональности по ролям (USER, ADMIN)
Пользователь
* Инвентарь пользователя с наличием ячеек под Item
* Разделение счетов под внутренюю валюту
* Получение информации о состоянии своего профиля
* Получение краткой информации о другом пользователе
* Получение списка всех пользователей (ADMIN)
Внутренняя валюта
* Создание, изменение и выключение валюты (ADMIN)
* Получение списка валют и конкретной валюты. Разное предоставление информации в зависимости от роли
* Покупка валюты со списание баланса и добавлением в счет пользователя
Item
* Получение списка Item и конкретного Item. Разное предоставление информации в зависимости от роли
* Получение списка цен у конкретного Item.  Разное предоставление информации в зависимости от роли
* Покупка Item со списание внутренней валюты и добавлнием в инвентарь
* Создание Item c ценами во внутренней валюте (ADMIN). Возможно создать не активную цену.
* Изменение Item без изменения цен (ADMIN)
* Изменеие или выключение конкретной цены (ADMIN)
* Выключение Item с выключение всех цен (ADMIN)

  ### API-методы для управления товарами (операции CRUD):

## Требования
* MYSQL
* Git
* Java
* Maven

## Запуск

1) Запуститить локальный сервер базы данных
2) Внести изменения в фаил application.properties в зависимости от выбранной програмы для запуска локального сервера
3) Запустить фаил WebsiteApplication или командой mvn spring-boot:run

Система будет запущена по адресу https://localhost:8088/items

## RestAPI
 ### Получение каталога товаров:
- /items
 ### Получение информации о товарове по уникальному идентификатору:
- /items/{id}
 ### Создание товара и возварщение его уникального идентификатора:
 - /items
 #### Пример входных данных (Body): 
 - {
 "sku": "160",
 "type": "Game",
 "name": "The Witcher 3: Wild Hunt ",
 "description": "Чеканная монета",
 "cost": 9999
 }
 #### Response 
 - {
    "id": 28
}
 ### Редактирование товара по его идентификатору:
 - /items/{id}
 #### Пример входных данных: 
- { 
 "sku": "112",
 "name": "Half-Life 2",
 "type": "Game",
 "description": "Проснитесь и пойте Mr.Freeman",
 "cost": 5000
}
#### Response 
 - {
 "id": {id},	  
 "sku": "112",
 "name": "Half-Life 2",
 "type": "Game",
 "description": "Проснитесь и пойте Mr.Freeman",
 "cost": 5000
}
 ### Удаление товара по его идентификатору.
/items/{id}
