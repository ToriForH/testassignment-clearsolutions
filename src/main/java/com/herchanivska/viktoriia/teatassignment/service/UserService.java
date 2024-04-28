package com.herchanivska.viktoriia.teatassignment.service;

import com.herchanivska.viktoriia.teatassignment.model.User;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.List;

public interface UserService {
    User create(User user) throws IOException;
    User updateField(long id, User user) throws IntrospectionException, InvocationTargetException, IllegalAccessException;
    User update(User user);
    boolean delete(User user);
    List<User> searchByBirthDate(Instant from, Instant to);
    User readById(long id);
    List<User> getAll();
}
