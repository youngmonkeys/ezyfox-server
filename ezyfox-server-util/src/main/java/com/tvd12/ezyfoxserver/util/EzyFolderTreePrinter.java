package com.tvd12.ezyfoxserver.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;

import lombok.Getter;
import lombok.Setter;

public class EzyFolderTreePrinter {

	protected boolean pretty;
	protected boolean printFile;
	protected String tabsString;
	protected String branchString;
	protected String verticalSymbol;
	
	protected EzyFolderTreePrinter(Builder builder) {
		this.pretty = builder.pretty;
		this.printFile = builder.printFile;
		this.tabsString = builder.printTabs();
		this.branchString = builder.printBranch();
		this.verticalSymbol = builder.verticalSymbol;
	}
	
	public String print(File folder) {
		EzyFile root = new EzyFile(folder);
		visitFile(root, folder);
		StringBuilder sb = new StringBuilder();
		print(root, sb);
		return sb.toString();
	}
	
	private void print(EzyFile file, StringBuilder sb) {
		String first = file.getParent() != null ? branchString : "";
		sb.append(printIndents(file))
			.append(first)
			.append(file.getName())
			.append(file.isFile() ? "" : "/")
			.append("\n");
		for (EzyFile child : file.getChildren())
			print(child, sb);
	}
	
	private String printIndents(EzyFile file) {
		List<String> strings = new ArrayList<>();
		EzyFile parent = file.getParent();
		while(parent != null) {
			boolean vertical = pretty && parent.isLastFileInFolder();
			strings.add((vertical ? "" : verticalSymbol) + tabsString);
			parent = parent.getParent();
		}
		StringBuilder sb = new StringBuilder();
		for(int i = strings.size() - 1 ; i >= 0 ; i--)
			sb.append(strings.get(i));
		return sb.toString();
	}
	
	private void visitFile(EzyFile parent, File file) {
		if(file.isFile()) {
			if(printFile) {
				EzyFile item = new EzyFile(file);
				item.setParent(parent);
			}
		}
		else {
			File[] files = file.listFiles();
			for(int i = 0 ; i < files.length ; i++) {
				if(files[i].isFile() && !printFile)
					continue;
				EzyFile item = new EzyFile(files[i]);
				visitFile(item, files[i]);
				item.setParent(parent);
				item.setLastFileInFolder(i == files.length - 1);
			}
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyFolderTreePrinter> {
		protected int tabSize = 2;
		protected boolean pretty = true;
		protected boolean printFile = true;
		protected String verticalSymbol = "|";
		protected String horizontalSymbol = "-";
		protected String intersectionSymbol = "+";
		
		public Builder tabSize(int tabSize) {
			this.tabSize = tabSize;
			return this;
		}
		
		public Builder pretty(boolean pretty) {
			this.pretty = pretty;
			return this;
		}
		
		public Builder printFile(boolean printFile) {
			this.printFile = printFile;
			return this;
		}
		
		public Builder verticalSymbol(String verticalSymbol) {
			this.verticalSymbol = verticalSymbol;
			return this;
		}
		
		public Builder horizontalSymbol(String horizontalSymbol) {
			this.horizontalSymbol = horizontalSymbol;
			return this;
		}
		
		public Builder intersectionSymbol(String intersectionSymbol) {
			this.intersectionSymbol = intersectionSymbol;
			return this;
		}
		
		@Override
		public EzyFolderTreePrinter build() {
			return new EzyFolderTreePrinter(this);
		}
		
		private String printTabs() {
			StringBuilder builder = new StringBuilder();
			for(int i = 0 ; i < tabSize ; i++)
				builder.append(" ");
			return builder.toString();
		}
		
		private String printBranch() {
			StringBuilder builder = new StringBuilder(intersectionSymbol);
			for(int i = 0 ; i < tabSize ; i++)
				builder.append(horizontalSymbol);
			return builder.toString();
		}
	}
	
	@Getter
	private static class EzyFile {

		private String name;
		private boolean file;
		private EzyFile parent;
		@Setter
		private boolean lastFileInFolder = true;
		private List<EzyFile> children = new ArrayList<>();
		
		public EzyFile(File file) {
			this.file = file.isFile();
			this.name = file.getName();
		}
		
		public void setParent(EzyFile parent) {
			if(!parent.isFile())
				parent.addChild(this);
			this.parent = parent;
		}
		
		public void addChild(EzyFile child) {
			this.children.add(child);
		}
	}
	
}
