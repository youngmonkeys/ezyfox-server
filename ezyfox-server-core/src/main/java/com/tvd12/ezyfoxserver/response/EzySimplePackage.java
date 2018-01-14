package com.tvd12.ezyfoxserver.response;

import java.util.Collection;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.util.EzyHashMapList;

import lombok.Getter;
import lombok.Setter;

public class EzySimplePackage implements EzyPackage {
    
    @Getter
    @Setter
    protected EzyArray data;
    @Getter
    @Setter
    protected EzyConstant transportType = EzyTransportType.TCP;
    protected EzyHashMapList<EzyConstant, EzySession> recipients = new EzyHashMapList<>();
    
    public void addRecipients(Collection<EzySession> recipients) {
        recipients.forEach(this::addRecipient);
    }
    
    public void addRecipient(EzySession recipient) {
        this.recipients.addItems(recipient.getConnectionType(), recipient);
    }
    
    @Override
    public Collection<EzySession> getRecipients(EzyConstant connectionType) {
        return recipients.getItems(connectionType);
    }
    
    @Override
    public void release() {
        this.recipients.deepClear();
        this.data = null;
        this.recipients = null;
    }
    
}
