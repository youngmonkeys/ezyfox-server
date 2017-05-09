package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfoxserver.entity.EzyUser;

public class EzySimpleUserAddableManager 
        extends EzySimpleUserManager 
        implements EzyUserAddable {

    @Override
    public EzyUser addUser(EzyUser user) {
        // put user if absent
        EzyUser old = usersByName.putIfAbsent(user.getName(), user);
        
        // check if user added then return added user
        if(old != null) return old; 

        getLogger().info("add user {}, user count = {}", user.getName(), usersByName.size());
        
        // map user with id
        return usersById.putIfAbsent(user.getId(), user);
    }
    
}
