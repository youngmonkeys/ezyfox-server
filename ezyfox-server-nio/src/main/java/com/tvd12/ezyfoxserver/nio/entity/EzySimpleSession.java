package com.tvd12.ezyfoxserver.nio.entity;

import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import lombok.Getter;
import lombok.Setter;

import java.nio.channels.SelectionKey;

@Setter
@Getter
public class EzySimpleSession
    extends EzyAbstractSession
    implements EzyNioSession {
    private static final long serialVersionUID = -8390274886953462147L;

    @Override
    public SelectionKey getSelectionKey() {
        return (SelectionKey) properties.get(SELECTION_KEY);
    }
}
