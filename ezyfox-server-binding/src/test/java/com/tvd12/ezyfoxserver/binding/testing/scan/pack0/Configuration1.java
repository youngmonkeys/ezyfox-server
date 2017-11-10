package com.tvd12.ezyfoxserver.binding.testing.scan.pack0;

import com.tvd12.ezyfoxserver.binding.EzyBindingConfig;
import com.tvd12.ezyfoxserver.binding.EzyBindingContext;
import com.tvd12.ezyfoxserver.binding.EzyBindingContextAware;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnwrapper;
import com.tvd12.ezyfoxserver.binding.annotation.EzyConfiguration;
import com.tvd12.ezyfoxserver.binding.annotation.EzyPackagesScan;
import com.tvd12.ezyfoxserver.binding.testing.objectbinding.ClassD;
import com.tvd12.ezyfoxserver.entity.EzyObject;

import lombok.Setter;

@EzyConfiguration
@EzyPackagesScan({"com.tvd12.ezyfoxserver.binding.testing.scan.pack1"})
public class Configuration1 implements EzyBindingContextAware, EzyBindingConfig {

	@Setter
	private EzyBindingContext context;
	
	@Override
	public void config() {
		context.bindTemplate(ClassD.class, new EzyUnwrapper<EzyObject, ClassD>() {
			@Override
			public void unwrap(EzyUnmarshaller unmarshaller, EzyObject input, ClassD output) {
			}
		});
	}
	
	
	
}
