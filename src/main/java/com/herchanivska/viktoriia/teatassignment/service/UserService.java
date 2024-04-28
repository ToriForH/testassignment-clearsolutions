package com.herchanivska.viktoriia.teatassignment.service;

import com.herchanivska.viktoriia.teatassignment.model.User;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;

public interface UserService {
    User create(User user);
    User updateField(User user) throws IntrospectionException, InvocationTargetException, IllegalAccessException;
    User update(User user);
    void delete(User user);
    List<User> searchByBirthDate(LocalDate from, LocalDate to);
    User readById(long id);
    List<User> getAll();
}
