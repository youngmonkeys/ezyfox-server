package com.tvd12.ezyfoxserver.delegate;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.command.EzyDisconnectUser;
import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.impl.EzyUserRemovedEventImpl;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

import lombok.Setter;

@Setter
public class EzySimpleUserRemoveDelegate
        extends EzyLoggable
        implements EzyUserRemoveDelegate {

    protected EzyUser user;
    protected EzyContext context;
    
    @Override
    public void onUserRemoved(EzyConstant reason) {
        notifyUserRemoved(reason);
        disconectUser(reason);
    }
    
    protected void notifyUserRemoved(EzyConstant reason) {
        try {
            context
                .get(EzyFirePluginEvent.class)
                .fire(EzyEventType.USER_REMOVED, newUserRemovedEvent(reason));
        }
        catch(Exception e) {
            getLogger().error("notify user removed error", e);
        }
    }
    
    protected EzyEvent newUserRemovedEvent(EzyConstant reason) {
        return EzyUserRemovedEventImpl.builder()
                .user(user)
                .reason(reason)
                .build();
    }
    
    protected void disconectUser(EzyConstant reason) {
        context.get(EzyDisconnectUser.class)
            .user(user)
            .reason(reason)
            .fireClientEvent(true)
            .fireServerEvent(true)
            .execute();
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyUserRemoveDelegate> {
        protected EzyUser user;
        protected EzyContext context;
        
        public Builder user(EzyUser user) {
            this.user = user;
            return this;
        }
        
        public Builder context(EzyContext context) {
            this.context = context;
            return this;
        }
        
        @Override
        public EzyUserRemoveDelegate build() {
            EzySimpleUserRemoveDelegate answer = new EzySimpleUserRemoveDelegate();
            answer.setContext(context);
            answer.setUser(user);
            return answer;
        }
    }
    
}
