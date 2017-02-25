package com.tvd12.ezyfoxserver.builder.impl;

import com.tvd12.ezyfoxserver.function.EzyCreation;
import com.tvd12.ezyfoxserver.transformer.EzyInputTransformer;
import com.tvd12.ezyfoxserver.transformer.EzyOutputTransformer;

public class EzyInOutTransformerNeeder {

	protected final EzyInputTransformer inputTransformer;
	protected final EzyOutputTransformer outputTransformer;
	
	public EzyInOutTransformerNeeder(
			EzyInputTransformer inputTransformer, 
			EzyOutputTransformer outputTransformer) {
		this.inputTransformer = inputTransformer;
		this.outputTransformer = outputTransformer;
	}
	
	public static abstract class AbstractCreator<P extends EzyInOutTransformerNeeder> 
			implements EzyCreation<P> {
		protected EzyInputTransformer inputTransformer;
		protected EzyOutputTransformer outputTransformer;
		
		public AbstractCreator<P> inputTransformer(EzyInputTransformer inputTransformer) {
			this.inputTransformer = inputTransformer;
			return this;
		}
		public AbstractCreator<P> outputTransformer(EzyOutputTransformer outputTransformer) {
			this.outputTransformer = outputTransformer;
			return this;
		}
		
		@Override
		public abstract P create();
	}
	
}
