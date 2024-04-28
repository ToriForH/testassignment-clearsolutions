package com.herchanivska.viktoriia.teatassignment.service.impl;

import com.herchanivska.viktoriia.teatassignment.model.User;
import com.herchanivska.viktoriia.teatassignment.service.UserService;
import jakarta.persistence.EntityNotFoundException;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

public class UserServiceImpl implements UserService {
    private final List<User> users; //users list in service replace database usage
    private long idCount; //to generate id due to there are no database

    public UserServiceImpl() {
        this.users = new ArrayList<>();
        this.idCount = 0;
    }

    @Override
    public User create(User user) throws IOException {
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("application.properties");
        properties.load(stream);
        String minAge = properties.getProperty("min.age");
        Instant minBirthDate = ZonedDateTime.now().minusYears(Long.parseLong(minAge)).toInstant();
        if (user.getBirthDate().isAfter(minBirthDate)) {
            throw new IllegalArgumentException("User should be at least " + minAge + " years old");
        }
        user.setId(++idCount);
        users.add(user);
        return readById(user.getId());
    }

    @Override
    public User updateField(long id, User user) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        User toUpdate = readById(id);
        if (toUpdate == null) {
            throw new EntityNotFoundException("User not exist");
        }
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
        if (toUpdate == null) {
            throw new EntityNotFoundException("User not exist");
        }
        users.remove(toUpdate); //use remove and add instead of setters to make possible change User class fields without rewriting service method
        users.add(user);
        return toUpdate;
    }

    @Override
    public boolean delete(User user) {
        User toDelete = readById(user.getId());
        if (toDelete != null) {
            return users.remove(toDelete);
        }
        return false;
    }

    @Override
    public List<User> searchByBirthDate(Instant from, Instant to) {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range");
        }
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
