package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
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

    public void addToFriendList(long userId, long friendId){
        Map<Long, User> users = userStorage.getUsers();
        User user = users.get(userId);
        User userFriend = users.get(friendId);

        if (user.getFriendsId().contains(friendId)&&userFriend.getFriendsId().contains(userId)) {
            throw new UserFriendListException("Пользователь уже был добавлен в друзья!");
        } else if(!userStorage.getUsers().containsKey(userId) || !userStorage.getUsers().containsKey(friendId)){
            throw new NotFoundException("Пользователя с таким ID нет!");
        } else {
            user.getFriendsId().add(userFriend.getId());
            userFriend.getFriendsId().add(user.getId());
        }
    }

    public void removeFromFriendList(long userId, long friendId) {
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

    public List<User> getUserFriendsList(long id){
        return userStorage.getUsers().values().stream()
                .filter((a) -> userStorage.getUsers().get(id).getFriendsId().contains(a.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriendsList(long userId, long otherId) {
        Map<Long, User> users = userStorage.getUsers();
        User user = users.get(userId);
        User userFriend = users.get(otherId);

//        if (!users.containsKey(userId) || !users.containsKey(otherId)) throw new NotFoundException("Пользователь с таким ID не найден");

            List<Long> commonFriendsId = user.getFriendsId().stream()
                    .filter(userFriend.getFriendsId()::contains)
                    .collect(Collectors.toList());

            return userStorage.getUsers().values().stream()
                    .filter((a) -> commonFriendsId.contains(a.getId()))
                    .collect(Collectors.toList());
        }




}
