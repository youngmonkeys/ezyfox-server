package com.tvd12.ezyfoxserver.support.command;

import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.context.EzyContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class EzyAbstractArrayResponse
    extends EzyAbstractResponse<EzyArrayResponse>
    implements EzyArrayResponse {

    protected List<Object> additionalParams;

    public EzyAbstractArrayResponse(EzyContext context, EzyMarshaller marshaller) {
        super(context, marshaller);
    }

    @Override
    public EzyArrayResponse param(Object value) {
        initAdditionalParams();
        additionalParams.add(value);
        return this;
    }

    @Override
    public EzyArrayResponse params(Object... values) {
        initAdditionalParams();
        additionalParams.addAll(Arrays.asList(values));
        return this;
    }

    @Override
    public EzyArrayResponse params(Iterable<?> values) {
        initAdditionalParams();
        for (Object value : values) {
            additionalParams.add(value);
        }
        return this;
    }

    private void initAdditionalParams() {
        if (additionalParams == null) {
            additionalParams = new ArrayList<>();
        }
    }

    @Override
    protected EzyData getResponseData() {
        EzyArray array = data != null
            ? marshaller.marshal(data)
            : newArrayBuilder().build();
        if (additionalParams != null) {
            for (Object object : additionalParams) {
                Object value = marshaller.marshal(object);
                array.add(value);
            }
        }
        return array;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (additionalParams != null) {
            this.additionalParams.clear();
            this.additionalParams = null;
        }
    }
}
