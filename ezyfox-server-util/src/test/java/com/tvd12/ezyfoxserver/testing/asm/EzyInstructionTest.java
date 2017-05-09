package com.tvd12.ezyfoxserver.testing.asm;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.asm.EzyInstruction;
import com.tvd12.test.base.BaseTest;

public class EzyInstructionTest extends BaseTest {

	@Test
	public void test() {
		EzyInstruction instruction = new EzyInstruction();
		assertEquals(instruction.finish().toString(), ";");
		
		instruction = new EzyInstruction();
		assertEquals(instruction.constructor(A.class).toString(), 
				A.class.getName() + "();");
		
		instruction = new EzyInstruction();
		assertEquals(instruction.append((Object)"hello").toString(), "hello;");
		
		instruction = new EzyInstruction();
		assertEquals(instruction.string("hello").toString(), "\"hello\";");
		
		instruction = new EzyInstruction();
		assertEquals(instruction.clazz(A.class, true).toString(), 
				A.class.getName() + ".class;");
		
		instruction = new EzyInstruction();
		assertEquals(instruction.dot().toString(), ".;");
		
		instruction = new EzyInstruction();
		assertEquals(instruction.comma().toString(), ", ;");
		
		instruction = new EzyInstruction();
		assertEquals(instruction.bracketopen().bracketclose().toString(), "();");
		
		instruction = new EzyInstruction();
		assertEquals(instruction.brackets(A.class).toString(), 
				"(" + A.class.getTypeName() + ");");
		
		instruction = new EzyInstruction();
		assertEquals(instruction.brackets("hello").toString(), "(hello);");
		
		instruction = new EzyInstruction();
		assertEquals(instruction.variable(A.class).toString(), 
				A.class.getTypeName() + " a;");
		
		instruction = new EzyInstruction();
		assertEquals(instruction.cast(A.class, "va").toString(), 
				"((" + A.class.getTypeName() + ")(va));");
		
		instruction = new EzyInstruction();
		assertEquals(instruction.valueOf(int.class, "value").toString(), 
				Integer.class.getName() + ".valueOf(value);");
		
		instruction = new EzyInstruction();
		assertEquals(instruction.valueOf(Integer.class, "value").toString(), "value;");
		
		instruction = new EzyInstruction("", "", true);
		assertEquals(instruction.append((Object)"hello").toString(), "hello;");
		
		instruction = new EzyInstruction("", "", true);
		assertEquals(instruction.append((Object)"hello;").toString(), "hello;");
		
		instruction = new EzyInstruction("", "", false);
		assertEquals(instruction.append((Object)"hello").toString(true), "hello");
		
		instruction = new EzyInstruction("", "", true);
		assertEquals(instruction.append((Object)"hello;").toString(true), "hello;");
		
		instruction = new EzyInstruction("", "", true);
		assertEquals(instruction.append((Object)"hello;").toString(false), "hello;");
		
		instruction = new EzyInstruction("", "", true);
		assertEquals(instruction.append((Object)"hello").toString(false), "hello");
	}
	
	public static class A {
		
	}
	
}
