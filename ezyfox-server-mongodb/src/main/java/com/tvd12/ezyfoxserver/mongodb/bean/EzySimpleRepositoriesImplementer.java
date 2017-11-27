package com.tvd12.ezyfoxserver.mongodb.bean;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.annotation.EzyAutoImpl;
import com.tvd12.ezyfoxserver.io.EzyLists;
import com.tvd12.ezyfoxserver.mongodb.EzyMongoRepository;
import com.tvd12.ezyfoxserver.reflect.EzyPackages;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public abstract class EzySimpleRepositoriesImplementer
		extends EzyLoggable
		implements EzyRepositoriesImplementer {

	protected Set<Class<?>> autoImplInterfaces;
	
	public EzySimpleRepositoriesImplementer() {
		this.autoImplInterfaces = new HashSet<>();
	}
	
	public EzyRepositoriesImplementer scan(String packageName) {
		autoImplInterfaces.addAll(getAutoImplRepoInterfaces(packageName));
		return this;
	}
	
	public EzyRepositoriesImplementer scan(String... packageNames) {
		return scan(Sets.newHashSet(packageNames));
	}
	
	public EzyRepositoriesImplementer scan(Iterable<String> packageNames) {
		packageNames.forEach(this::scan);
		return this;	
	}
	
	@Override
	public EzyRepositoriesImplementer repositoryInterface(Class<?> itf) {
		if(!Modifier.isInterface(itf.getModifiers())) {
			getLogger().warn("class {} is not an interface, ignore its", itf.getSimpleName());
		}
		else if(!EzyMongoRepository.class.isAssignableFrom(itf)) {
			getLogger().warn("interface {} doestn't extends {}, ignore its", 
					itf.getSimpleName(), EzyMongoRepository.class.getSimpleName());
		}
		else {
			autoImplInterfaces.add(itf);
		}
		return this;
	}
	
	@Override
	public EzyRepositoriesImplementer repositoryInterfaces(Class<?>... itfs) {
		return repositoryInterfaces(Sets.newHashSet(itfs));
	}
	
	@Override
	public EzyRepositoriesImplementer repositoryInterfaces(Iterable<Class<?>> itfs) {
		itfs.forEach(this::repositoryInterface);
		return this;
	}
	
	@Override
	public Map<Class<?>, Object> implement(Object template) {
		Map<Class<?>, Object> repositories = new ConcurrentHashMap<>();
		for(Class<?> itf : autoImplInterfaces) {
			Object repo = implementRepoInterface(itf, template);
			repositories.put(itf, repo);
		}
		return repositories;
	}
	
	private Object implementRepoInterface(Class<?> itf, Object template) {
		EzySimpleRepositoryImplementer implementer = newRepoImplementer(itf);
		return implementer.implement(template);
	}
	
	protected abstract EzySimpleRepositoryImplementer newRepoImplementer(Class<?> itf);
	
	private Set<Class<?>> getRepoInterfaces(String packageName) {
		return EzyPackages.getExtendsClasses(packageName, EzyMongoRepository.class);
	}
	
	private Collection<Class<?>> getAutoImplRepoInterfaces(String packageName) {
		Set<Class<?>> classes = getRepoInterfaces(packageName);
		return EzyLists.filter(classes, this::isAutoImplRepoInterface);
	}
	
	private boolean isAutoImplRepoInterface(Class<?> clazz) {
		return  clazz.isAnnotationPresent(EzyAutoImpl.class) &&
				Modifier.isPublic(clazz.getModifiers()) &&
				Modifier.isInterface(clazz.getModifiers());
				
	}
	
}
