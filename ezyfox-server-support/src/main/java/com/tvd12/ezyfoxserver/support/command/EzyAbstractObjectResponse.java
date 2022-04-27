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

    protected Set<Object> excludeParamKeys;
    protected Map<Object, Object> additionalParams;

    public EzyAbstractObjectResponse(
        EzyContext context,
        EzyMarshaller marshaller
    ) {
        super(context, marshaller);
    }

    @Override
    public EzyObjectResponse param(Object key, Object value) {
        if (additionalParams == null) {
            additionalParams = new HashMap<>();
        }
        additionalParams.put(key, value);
        return this;
    }

    @Override
    public EzyObjectResponse exclude(Object key) {
        if (excludeParamKeys == null) {
            excludeParamKeys = new HashSet<>();
        }
        excludeParamKeys.add(key);
        return this;
    }

    @Override
    protected EzyData getResponseData() {
        EzyObject object = data != null
            ? marshaller.marshal(data)
            : newObjectBuilder().build();
        if (additionalParams != null) {
            for (Map.Entry<Object, Object> e : additionalParams.entrySet()) {
                Object skey = marshaller.marshal(e.getKey());
                Object svalue = marshaller.marshal(e.getValue());
                object.put(skey, svalue);
            }
        }
        if (excludeParamKeys != null) {
            object.removeAll(excludeParamKeys);
        }
        return object;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (excludeParamKeys != null) {
            this.excludeParamKeys.clear();
            this.excludeParamKeys = null;
        }
        if (additionalParams != null) {
            this.additionalParams.clear();
            this.additionalParams = null;
        }
    }
}
