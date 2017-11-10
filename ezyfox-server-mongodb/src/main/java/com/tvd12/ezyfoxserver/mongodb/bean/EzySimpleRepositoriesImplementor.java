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

public abstract class EzySimpleRepositoriesImplementor
		extends EzyLoggable
		implements EzyRepositoriesImplementor {

	protected Set<Class<?>> autoImplInterfaces;
	
	public EzySimpleRepositoriesImplementor() {
		this.autoImplInterfaces = new HashSet<>();
	}
	
	public EzyRepositoriesImplementor scan(String packageName) {
		autoImplInterfaces.addAll(getAutoImplRepoInterfaces(packageName));
		return this;
	}
	
	public EzyRepositoriesImplementor scan(String... packageNames) {
		return scan(Sets.newHashSet(packageNames));
	}
	
	public EzyRepositoriesImplementor scan(Iterable<String> packageNames) {
		packageNames.forEach(this::scan);
		return this;	
	}
	
	@Override
	public EzyRepositoriesImplementor repositoryInterface(Class<?> itf) {
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
	public EzyRepositoriesImplementor repositoryInterfaces(Class<?>... itfs) {
		return repositoryInterfaces(Sets.newHashSet(itfs));
	}
	
	@Override
	public EzyRepositoriesImplementor repositoryInterfaces(Iterable<Class<?>> itfs) {
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
		EzySimpleRepositoryImplementor implementor = newRepoImplementor(itf);
		return implementor.implement(template);
	}
	
	protected abstract EzySimpleRepositoryImplementor newRepoImplementor(Class<?> itf);
	
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
