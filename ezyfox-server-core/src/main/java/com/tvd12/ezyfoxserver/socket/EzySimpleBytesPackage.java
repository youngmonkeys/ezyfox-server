package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyHashMapList;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyBytesPackage;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

public class EzySimpleBytesPackage implements EzyBytesPackage {

    @Getter
    @Setter
    protected byte[] bytes;
    @Getter
    @Setter
    protected EzyConstant transportType = EzyTransportType.TCP;
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
        this.bytes = null;
        this.recipients = null;
    }

}
