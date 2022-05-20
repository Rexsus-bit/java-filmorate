package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserFriendListException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    void addToFriendList(long userId, long friendId){
        Map<Long, User> users = userStorage.getUsers();
        User user = users.get(userId);
        User userFriend = users.get(friendId);

        if (user.getFriendsId().contains(userFriend.getId())&&userFriend.getFriendsId().contains(user.getId())) {
            throw new UserFriendListException("Пользователь уже был добавлен в друзья!");
        } else {
            user.getFriendsId().add(userFriend.getId());
            userFriend.getFriendsId().add(user.getId());
        }
    }

    void removeFromFriendList(long userId, long friendId) {
        Map<Long, User> users = userStorage.getUsers();
        User user = users.get(userId);
        User userFriend = users.get(friendId);

        if (!user.getFriendsId().contains(userFriend.getId()) && !userFriend.getFriendsId().contains(user.getId())) {
            throw new UserFriendListException("Пользователя нельзя удалить из списка друзей, т.к. он не является другом");
        } else {
            user.getFriendsId().remove(userFriend.getId());
            userFriend.getFriendsId().remove(user.getId());
        }

    }

        List<Long> showCommonFriendsId(long userId, long friendId) {
            Map<Long, User> users = userStorage.getUsers();
            User user = users.get(userId);
            User userFriend = users.get(friendId);

            return user.getFriendsId().stream().filter(userFriend.getFriendsId()::contains).
                    collect(Collectors.toList());

            }




}
