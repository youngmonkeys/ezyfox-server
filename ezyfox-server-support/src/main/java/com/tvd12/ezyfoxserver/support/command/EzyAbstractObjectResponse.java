package com.tvd12.ezyfoxserver.support.command;

import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.entity.EzyObject;
import com.tvd12.ezyfoxserver.context.EzyContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class EzyAbstractObjectResponse
    extends EzyAbstractResponse<EzyObjectResponse>
    implements EzyObjectResponse {

    protected final Set<Object> excludeParamKeys = new HashSet<>();
    protected final Map<Object, Object> additionalParams = new HashMap<>();

    public EzyAbstractObjectResponse(EzyContext context, EzyMarshaller marshaller) {
        super(context, marshaller);
    }

    @Override
    public EzyObjectResponse param(Object key, Object value) {
        additionalParams.put(key, value);
        return this;
    }

    @Override
    public EzyObjectResponse exclude(Object key) {
        excludeParamKeys.add(key);
        return this;
    }

    @Override
    protected EzyData getResponseData() {
        EzyObject object = data != null
            ? marshaller.marshal(data)
            : newObjectBuilder().build();
        for (Object key : additionalParams.keySet()) {
            Object value = additionalParams.get(key);
            Object skey = marshaller.marshal(key);
            Object svalue = marshaller.marshal(value);
            object.put(skey, svalue);
        }
        object.removeAll(excludeParamKeys);
        return object;
    }
}
