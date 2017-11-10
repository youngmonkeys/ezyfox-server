package com.tvd12.ezyfoxserver.bean.testing.supplier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPrototype;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EzyPrototype("ca")
@SuppressWarnings("rawtypes")
public class ClassA {

	@EzyAutoBind
	private Map map;
	@EzyAutoBind
	private HashMap hashMap;
	@EzyAutoBind
	private ConcurrentHashMap concurrentHashMap;
	@EzyAutoBind
	private Set set;
	@EzyAutoBind
	private TreeMap treeMap;
	@EzyAutoBind
	private HashSet hashSet;
	@EzyAutoBind
	private CopyOnWriteArrayList copyOnWriteArrayList;
	@EzyAutoBind
	private CopyOnWriteArraySet copyOnWriteArraySet;
	@EzyAutoBind
	private LinkedList linkedList;
	@EzyAutoBind
	private Queue queue;
	@EzyAutoBind
	private List list;
	@EzyAutoBind
	private ArrayList arrayList;
	@EzyAutoBind
	private Stack stack;
	@EzyAutoBind
	private Collection collection;
	
}
