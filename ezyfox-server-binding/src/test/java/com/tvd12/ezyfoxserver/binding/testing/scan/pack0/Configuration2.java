package com.tvd12.ezyfoxserver.binding.testing.scan.pack0;

import com.tvd12.ezyfoxserver.binding.EzyBindingConfig;
import com.tvd12.ezyfoxserver.binding.EzyBindingContext;
import com.tvd12.ezyfoxserver.binding.EzyBindingContextAware;
import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyTemplate;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.annotation.EzyConfiguration;
import com.tvd12.ezyfoxserver.binding.annotation.EzyPackagesScan;
import com.tvd12.ezyfoxserver.binding.testing.scan.pack1.ClassC;
import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;

import lombok.Setter;

@EzyConfiguration
@EzyPackagesScan({"com.tvd12.ezyfoxserver.binding.testing.scan.pack1"})
public class Configuration2 implements EzyBindingContextAware, EzyBindingConfig {

	@Setter
	private EzyBindingContext context;
	
	@Override
	public void config() {
		context.addTemplate(new EzyTemplate<EzyArray, ClassA>() {
			@Override
			public ClassA read(EzyUnmarshaller unmarshaller, EzyArray value) {
				return new ClassA();
			}
			
			@Override
			public EzyArray write(EzyMarshaller marshaller, ClassA object) {
				return EzyEntityFactory.create(EzyArrayBuilder.class).build();
			}
		});
		
		context.bindTemplate(ClassA.class, new EzyTemplate<EzyArray, ClassA>() {
			@Override
			public ClassA read(EzyUnmarshaller unmarshaller, EzyArray value) {
				return new ClassA();
			}
			
			@Override
			public EzyArray write(EzyMarshaller marshaller, ClassA object) {
				return EzyEntityFactory.create(EzyArrayBuilder.class).build();
			}
		});
		
		context.addTemplate(new Object());
		context.bindTemplate(ClassC.class, new Object());
		
	}
	
}
