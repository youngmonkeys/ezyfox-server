package com.tvd12.ezyfoxserver.testing.asm;

import java.lang.reflect.Method;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.asm.EzyFunction;
import com.tvd12.ezyfoxserver.asm.EzyInstruction;
import com.tvd12.test.base.BaseTest;

public class EzyFunctionTest extends BaseTest {

	@Test
	public void test() throws Exception {
		Method method = Reader.class.getMethod("read", String.class);
		EzyFunction function = new EzyFunction(method)
				.modifier("public");
		EzyFunction.EzyBody body = function.body();
		body.append(new EzyInstruction("\t", "\n", true)
				.variable(Integer.class, "number")
				.equal()
				.clazz(Integer.class)
				.append(".valueOf(arg0)"))
			.append(new EzyInstruction("\t", "\n")
					.append("number += 1"))
			.append(new EzyInstruction("\t", "\n")
					.answer()
					.append("java.lang.String.valueOf(number)"));
		System.out.println(body.function().toString());
	}
	
	public static interface Reader {
		public String read(String value);
	}
	
}
