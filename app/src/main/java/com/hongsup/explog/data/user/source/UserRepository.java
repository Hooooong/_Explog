package com.hongsup.explog.data.user.source;

import com.hongsup.explog.data.user.User;

/**
 * Created by Android Hong on 2017-12-13.
 */

public class UserRepository implements UserSource {

    private static UserRepository instance;
    private User user;

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        if (instance == null)
            instance = new UserRepository();
        return instance;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void clearUser() {
        user = null;
    }

    @Override
    public boolean isExistUser() {
        if (user != null)
            return true;
        return false;
    }


}
