package com.tvd12.ezyfoxserver.request.impl;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.request.EzyAccessAppParams;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EzySimpleAccessAppParams implements EzyAccessAppParams {

    private String appName;
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyAccessAppParams> {
        
        private String appName;
        
        public Builder appName(String appName) {
            this.appName = appName;
            return this;
        }
        
        @Override
        public EzyAccessAppParams build() {
            EzySimpleAccessAppParams answer = new EzySimpleAccessAppParams();
            answer.setAppName(appName);
            return answer;
        }
    }
    
}
