package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyHashMapList;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyPackage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class EzySimplePackage implements EzyPackage {

    protected EzyArray data;
    protected boolean encrypted;
    protected EzyConstant transportType = EzyTransportType.TCP;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected EzyHashMapList<EzyConstant, EzySession> recipients
        = new EzyHashMapList<>();
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected Set<String> connectionTypes = new HashSet<>();

    public void addRecipients(Collection<EzySession> recipients) {
        for (EzySession recipient : recipients) {
            this.addRecipient(recipient);
        }
    }

    public void addRecipient(EzySession recipient) {
        EzyConstant connectionType = recipient.getConnectionType();
        this.recipients.addItems(connectionType, recipient);
        if (connectionType != null) {
            this.connectionTypes.add(connectionType.getName());
        }
    }

    @Override
    public Collection<EzySession> getRecipients(EzyConstant connectionType) {
        return recipients.getItems(connectionType);
    }

    @Override
    public Set<String> getConnectionTypes() {
        return connectionTypes;
    }

    @Override
    public void release() {
        this.recipients.deepClear();
        this.connectionTypes.clear();
        this.data = null;
        this.recipients = null;
        this.connectionTypes = null;
    }
}
