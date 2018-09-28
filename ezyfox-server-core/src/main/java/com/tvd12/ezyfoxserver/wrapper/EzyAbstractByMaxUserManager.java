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
        EzyUser answer = addUser0(user);
        return answer;
    }
    
    protected void checkMaxUsers() {
        int current = getUserCount();
        if(current > maxUsers)
            throw new EzyMaxUserException(current, maxUsers);
    }
    
    protected EzyUser addUser0(EzyUser user) {
        EzyUser old = usersByName.putIfAbsent(user.getName(), user);
        if(old != null) return old; 
        EzyUser answer = usersById.putIfAbsent(user.getId(), user);
        getLogger().info("{} add user: {}, locks.size = {}, usersById.size = {}, usersByName.size = {}", getMessagePrefix(), user, locks.size(), usersById.size(), usersByName.size());
        return answer;
    }
    
    public static abstract class Builder<B extends Builder<B>>
            extends EzyAbstractUserManager.Builder<B> {
    }
    
}
