package com.tvd12.ezyfoxserver.support.command;

import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.context.EzyContext;

import java.util.ArrayList;
import java.util.List;

public abstract class EzyAbstractArrayResponse
    extends EzyAbstractResponse<EzyArrayResponse>
    implements EzyArrayResponse {

    protected List<Object> additionalParams = new ArrayList<>();

    public EzyAbstractArrayResponse(EzyContext context, EzyMarshaller marshaller) {
        super(context, marshaller);
    }

    @Override
    public EzyArrayResponse param(Object value) {
        additionalParams.add(value);
        return this;
    }

    @Override
    public EzyArrayResponse params(Object... values) {
        for (Object value : values) {
            additionalParams.add(value);
        }
        return this;
    }

    @Override
    public EzyArrayResponse params(Iterable<?> values) {
        for (Object value : values) {
            additionalParams.add(value);
        }
        return this;
    }

    @Override
    protected EzyData getResponseData() {
        EzyArray array = data != null
            ? marshaller.marshal(data)
            : newArrayBuilder().build();
        for (Object object : additionalParams) {
            Object value = marshaller.marshal(object);
            array.add(value);
        }
        return array;
    }

    @Override
    public void destroy() {
        super.destroy();
        this.additionalParams.clear();
        this.additionalParams = null;
    }
}
