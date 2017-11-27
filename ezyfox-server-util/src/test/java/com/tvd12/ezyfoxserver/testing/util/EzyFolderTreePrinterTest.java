package com.tvd12.ezyfoxserver.testing.util;

import java.io.File;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyFolderTreePrinter;

public class EzyFolderTreePrinterTest {
	
	@Test
	public void test() {
		String tree = EzyFolderTreePrinter.builder()
				.tabSize(3)
				.pretty(true)
				.printFile(true)
				.horizontalSymbol(".")
				.verticalSymbol("!")
				.intersectionSymbol("*")
				.build()
				.print(new File("tree-for-test"));
		System.out.println(tree);
	}
	
	@Test
	public void test2() {
		String tree = EzyFolderTreePrinter.builder()
				.tabSize(3)
				.pretty(false)
				.printFile(false)
				.horizontalSymbol(".")
				.verticalSymbol("!")
				.intersectionSymbol("*")
				.build()
				.print(new File("tree-for-test"));
		System.out.println(tree);
	}
	
	@Test
	public void test3() {
		String tree = EzyFolderTreePrinter.builder()
				.tabSize(3)
				.pretty(false)
				.printFile(false)
				.horizontalSymbol(".")
				.verticalSymbol("!")
				.intersectionSymbol("*")
				.build()
				.print(new File("tree-for-test/a.txt"));
		System.out.println(tree);
	}
	
}
