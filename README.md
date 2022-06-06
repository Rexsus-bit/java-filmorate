# scheme

![This is an image](/scheme.png)


Диаграмма описывает реляционную базу данных, в которой хранится информация о пользователях и фильмах, которые они «лайкают».

FilmGenre и FilmRate реализованы как Enum. Связь в виде лайков хранится в виде таблице user_likes, а связь в виде добавления в друзья хранится в виде таблицы friendship_relations. В указанной таблице переменная friendship_confirmation отвечает за то, было ли получено подтверждение при добавлении в друзья. 



**Примеры запросов:**

- **Получение всех фильмов:**

```
SELECT *
FROM films;
```

- **Получение всех пользователей:**
```
SELECT *
FROM films;
```

- **Запрос топ 10 фильмов:**
```
SELECT f.film_id, COUNT (u.user_id) AS rate
FORM user_likes AS u
LEFT OUTER JOIN films AS f ON u.film_id = f.film_id
GROUP BY f.film_id
SORT BY rate DESC
limit 10;
```
