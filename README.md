# scheme

![This is an image](/scheme.png)


Диаграмма описывает реляционную базу данных, в которой хранится информация о пользователях и фильмах, которые они «лайкают».

**Примеры запросов:**

- **Получение всех фильмов:**

```
SELECT *
FROM films;
```

- **Получение всех пользователей:**
```
SELECT *
FROM users;
```

- **Запрос топ 10 фильмов:**
```
SELECT f.film_id, 
COUNT (u.user_id) AS rate
FROM user_likes AS u
LEFT OUTER JOIN films AS f ON u.film_id = f.film_id
GROUP BY f.film_id
ORDER BY rate DESC
LIMIT 10;
```
