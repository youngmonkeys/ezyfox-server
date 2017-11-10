package com.tvd12.ezyfoxserver.morphia;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.mapping.Mapper;

import com.google.common.collect.Sets;
import com.mongodb.MongoClient;
import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.reflect.EzyPackages;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

@SuppressWarnings("rawtypes")
public class EzyDataStoreBuilder
		extends EzyLoggable 
		implements EzyBuilder<Datastore> {

	protected String databaseName;
	protected MongoClient mongoClient;
	protected Set<Class> entityClasses;
	
	public EzyDataStoreBuilder() {
		this.entityClasses = new HashSet<>();
	}
	
	public static EzyDataStoreBuilder dataStoreBuilder() {
		return new EzyDataStoreBuilder();
	}
	
	public EzyDataStoreBuilder databaseName(String databaseName) {
		this.databaseName = databaseName;
		return this;
	}
	
	public EzyDataStoreBuilder mongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
		return this;
	}
	
	public EzyDataStoreBuilder scan(String packageName) {
		this.entityClasses.addAll(getAnnotatedClasses(packageName, Entity.class));
		return this;
	}
	
	public EzyDataStoreBuilder addEntityClass(Class entityClass) {
		this.entityClasses.add(entityClass);
		return this;
	}
	
	public EzyDataStoreBuilder addEntityClasses(Class... classes) {
		return addEntityClasses(Sets.newHashSet(classes));
	}
	
	public EzyDataStoreBuilder addEntityClasses(Iterable<Class> classes) {
		classes.forEach(this::addEntityClass);
		return this;
	}
	
	@Override
	public Datastore build() {
		Mapper mapper = new Mapper();
		Datastore datastore = newDataStore(mapper);
		return datastore;
	}
	
	private Datastore newDataStore(Mapper mapper) {
		Morphia morphia = new Morphia(mapper, entityClasses);
		return morphia.createDatastore(mongoClient, databaseName);
	}
	
	private Set<Class<?>> getAnnotatedClasses(
			String packageName, Class<? extends Annotation> annClass) {
		return EzyPackages.getAnnotatedClasses(packageName, annClass);
	}
	
}
