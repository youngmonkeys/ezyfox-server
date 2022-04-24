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

@Getter
@Setter
public class EzySimplePackage implements EzyPackage {

    protected EzyArray data;
    protected boolean encrypted;
    protected EzyConstant transportType = EzyTransportType.TCP;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected EzyHashMapList<EzyConstant, EzySession> recipients = new EzyHashMapList<>();

    public void addRecipients(Collection<EzySession> recipients) {
        for (EzySession recipient : recipients) {
            this.addRecipient(recipient);
        }
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
