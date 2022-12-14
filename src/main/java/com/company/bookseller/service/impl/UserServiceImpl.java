package com.company.bookseller.service.impl;

import com.company.bookseller.dao.UserDao;
import com.company.bookseller.dao.entity.User;
import com.company.bookseller.service.UserService;
import com.company.bookseller.service.dto.UserDto;
import com.company.bookseller.service.util.PasswordUtil;
import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    private User userToEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setAvatar(userDto.getAvatar());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setRole(User.Role.valueOf(userDto.getRole().toString()));
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }

    private UserDto userToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setAvatar(user.getAvatar());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setRole(UserDto.Role.valueOf(user.getRole().toString()));
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

    @Override
    public long count() {
        return userDao.count();
    }

    @Override
    public List<UserDto> getAll(int limit, int offset) {
        return userDao.getAll(limit, offset)
                .stream()
                .map(this::userToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto get(Long id) {
        User user = userDao.get(id);
        if (user == null) {
            throw new RuntimeException("There is no user with id: " + id);
        }
        return userToDto(user);
    }

    @Override
    public boolean login(String email, String password) {
        UserDto user = getByEmail(email);
        String encryptedInput = PasswordUtil.encryptPassword(password);
        return user.getPassword().equals(encryptedInput);
    }

    @Override
    public UserDto create(UserDto userDto) {
        User existing = userDao.getByEmail(userDto.getEmail());
        if (existing != null) {
            throw new RuntimeException("User with email: " + userDto.getEmail() + " already exists");
        }
        String encryptedPassword = PasswordUtil.encryptPassword(userDto.getPassword());
        userDto.setPassword(encryptedPassword);
        User created = userDao.create(userToEntity(userDto));
        return get(created.getId());
    }

    @Override
    public UserDto update(UserDto userDto) {
        User existing = userDao.getByEmail(userDto.getEmail());
        if (existing != null && existing.getId() != userDto.getId()) {
            throw new RuntimeException("User with email: " + userDto.getEmail() + " does not exist");
        }
        User updated = userDao.update(userToEntity(userDto));
        return get(updated.getId());
    }

    public void delete(Long id) {
        if (!userDao.delete(id)) {
            throw new RuntimeException("User with [id=" + id + "]");
        }
    }

    @Override
    public UserDto getByEmail(String email) {
        User user = userDao.getByEmail(email);
        if (user == null) {
            throw new RuntimeException("There is no user with email: " + email);
        }
        return userToDto(user);
    }
}
