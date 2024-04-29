package com.herchanivska.viktoriia.testassignment.service.impl;

import com.herchanivska.viktoriia.testassignment.model.User;
import com.herchanivska.viktoriia.testassignment.service.UserService;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final List<User> users; //users list in service replace database usage
    private long idCount; //to generate id due to there are no database

    public UserServiceImpl() {
        this.users = new ArrayList<>();
        this.idCount = 0;
    }

    @Override
    public User create(User user) {
        user.setId(++idCount);
        users.add(user);
        return readById(user.getId());
    }

    @Override
    public User updateField(User user) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        User toUpdate = readById(user.getId());
        Field[] fields = User.class.getDeclaredFields(); //use reflection to make possible change User class fields without rewriting service method
        for (Field field : fields) {
            Object newValue = new PropertyDescriptor(field.getName(), User.class).getReadMethod().invoke(user);
            if (newValue != null) {
                new PropertyDescriptor(field.getName(), User.class).getWriteMethod().invoke(toUpdate, newValue);
            }
        }
        return toUpdate;
    }

    @Override
    public User update(User user) {
        User toUpdate = readById(user.getId());
        users.remove(toUpdate); //use remove and add instead of setters to make possible change User class fields without rewriting service method
        users.add(user);
        return user;
    }

    @Override
    public void delete(long id) {
        User toDelete = readById(id);
        users.remove(toDelete);
    }

    @Override
    public List<User> searchByBirthDate(LocalDate from, LocalDate to) {
        return users.stream()
                .filter(user -> (user.getBirthDate().isBefore(to) || user.getBirthDate().equals(to))
                        && (user.getBirthDate().isAfter(from) || user.getBirthDate().equals(from)))
                .toList();
    }

    @Override
    public User readById(long id) {
        for (User user: users){
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        users.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        return users;
    }
}
