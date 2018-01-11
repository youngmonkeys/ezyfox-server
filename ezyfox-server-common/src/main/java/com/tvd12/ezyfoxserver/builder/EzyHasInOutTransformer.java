package com.tvd12.ezyfoxserver.builder;

import com.tvd12.ezyfoxserver.function.EzyCreation;
import com.tvd12.ezyfoxserver.io.EzyInputTransformer;
import com.tvd12.ezyfoxserver.io.EzyOutputTransformer;

public class EzyHasInOutTransformer {

	protected final EzyInputTransformer inputTransformer;
	protected final EzyOutputTransformer outputTransformer;
	
	public EzyHasInOutTransformer(
			EzyInputTransformer inputTransformer, 
			EzyOutputTransformer outputTransformer) {
		this.inputTransformer = inputTransformer;
		this.outputTransformer = outputTransformer;
	}
	
	public static abstract class AbstractCreator<
			P extends EzyHasInOutTransformer,
			C extends AbstractCreator<P, C>
			> 
			implements EzyCreation<P> {
		protected EzyInputTransformer inputTransformer;
		protected EzyOutputTransformer outputTransformer;
		
		public C inputTransformer(EzyInputTransformer inputTransformer) {
			this.inputTransformer = inputTransformer;
			return getThis();
		}
		public C outputTransformer(EzyOutputTransformer outputTransformer) {
			this.outputTransformer = outputTransformer;
			return getThis();
		}
		
		@Override
		public abstract P create();
		
		@SuppressWarnings("unchecked")
		protected C getThis() {
			return (C)this;
		}
	}
	
}
