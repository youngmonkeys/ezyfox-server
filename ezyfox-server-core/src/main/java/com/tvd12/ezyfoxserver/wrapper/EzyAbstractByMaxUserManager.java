package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.exception.EzyMaxUserException;

public class EzyAbstractByMaxUserManager 
        extends EzyAbstractUserManager 
        implements EzyUserAddable {

    protected EzyAbstractByMaxUserManager(Builder<?> builder) {
        super(builder);
    }
    
    @Override
    public EzyUser addUser(EzyUser user) {
        checkMaxUsers();
        return addUser0(user);
    }
    
    protected void checkMaxUsers() {
        int current = getUserCount();
        if(current > maxUsers)
            throw new EzyMaxUserException(current, maxUsers);
    }
    
    protected EzyUser addUser0(EzyUser user) {
        EzyUser old = usersByName.putIfAbsent(user.getName(), user);
        if(old != null) return old; 
        getLogger().info("{} add user {}, user count = {}", getMessagePrefix(), user, usersByName.size());
        return usersById.putIfAbsent(user.getId(), user);
    }
    
    public static abstract class Builder<B extends Builder<B>>
            extends EzyAbstractUserManager.Builder<B> {
    }
    
}
