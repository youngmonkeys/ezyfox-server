package com.tvd12.ezyfoxserver.asm;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.tvd12.ezyfoxserver.reflect.EzyMethod;

public class EzyFunction {

	protected final EzyMethod method;
	protected final EzyBody body;
	protected String modifier = "public";
	
	public EzyFunction(Method method) {
		this(new EzyMethod(method));
	}
	
	public EzyFunction(EzyMethod method) {
		this.method = method;
		this.body = new EzyBody(this);
	}
	
	public EzyFunction modifier(String modifier) {
		this.modifier = modifier;
		return this;
	}
	
	public EzyBody body() {
		return body;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append(method.getDeclaration(modifier))
				.append(" {\n")
				.append(body)
				.append("}")
				.toString();
	}
	
	public static class EzyBody {
		protected EzyFunction function;
		protected List<EzyInstruction> instructions = new ArrayList<>();
		
		public EzyBody(EzyFunction function) {
			this.function = function;
		}
		
		public EzyBody append(EzyInstruction instruction) {
			this.instructions.add(instruction);
			return this;
		}
		
		public EzyFunction function() {
			return function;
		}
		
		@Override
		public String toString() {
			return StringUtils.join(instructions, "");
		}
	}
	
}
