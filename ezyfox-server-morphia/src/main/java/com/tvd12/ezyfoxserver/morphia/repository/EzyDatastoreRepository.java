package com.tvd12.ezyfoxserver.morphia.repository;

import java.util.Collection;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.DeleteOptions;
import org.mongodb.morphia.FindAndModifyOptions;
import org.mongodb.morphia.UpdateOptions;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.mongodb.WriteResult;
import com.tvd12.ezyfoxserver.database.query.EzyFindAndModifyOptions;
import com.tvd12.ezyfoxserver.database.query.EzyUpdateOperations;
import com.tvd12.ezyfoxserver.function.EzyApply;
import com.tvd12.ezyfoxserver.mongodb.EzyMongoRepository;
import com.tvd12.ezyfoxserver.morphia.EzyDatastoreAware;
import com.tvd12.ezyfoxserver.morphia.query.impl.EzySimpleFindAndModifyOptions;
import com.tvd12.ezyfoxserver.morphia.query.impl.EzySimpleUpdateOperations;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

import lombok.Setter;

public abstract class EzyDatastoreRepository<I, E> 
		extends EzyLoggable
		implements EzyMongoRepository<I, E>, EzyDatastoreAware {

	@Setter
	protected Datastore datastore;
	
	protected abstract Class<E> getEntityType();

	@Override
	public long count() {
		return datastore.getCount(getEntityType());
	}

	@Override
	public void save(E entity) {
		datastore.save(entity);
	}

	@Override
	public void save(Iterable<E> entities) {
		datastore.save(entities);
	}

	@Override
	public E findById(I id) {
		return findByField("_id", id);
	}
	
	@Override
	public E findByField(String field, Object value) {
		return newQuery(field, value).get();
	}
	
	@Override
	public List<E> findListByIds(Collection<I> ids) {
		return newQuery().field("_id").in(ids).asList();
	}
	
	@Override
	public List<E> findListByField(String field, Object value) {
		return newQuery(field, value).asList();
	}
	
	@Override
	public List<E> findListByField(String field, Object value, int skip, int limit) {
		FindOptions options = new FindOptions().skip(skip).limit(limit);
		return newQuery(field, value).asList(options);
	}

	@Override
	public List<E> findAll() {
		return newQuery().asList();
	}
	
	@Override
	public List<E> findAll(int skip, int limit) {
		FindOptions options = new FindOptions().skip(skip).limit(limit);
		return newQuery().asList(options);
	}
	
	@Override
	public void updateOneById(I id, E entity) {
		updateOneById(id, entity, false);
	}
	
	@Override
	public void updateOneById(I id, E entity, boolean upsert) {
		datastore.updateFirst(newQuery("_id", id), entity, upsert);
	}
	
	@Override
	public void updateOneById(I id, EzyApply<EzyUpdateOperations<E>> operations) {
		updateOneById(id, operations, false);
	}
	
	@Override
	public void updateOneById(I id, EzyApply<EzyUpdateOperations<E>> operations, boolean upsert) {
		updateOneByQuery(newQuery("_id", id), operations, upsert);
	}
	
	@Override
	public void updateOneByField(String field, Object value, E entity) {
		updateOneByField(field, value, entity, false);
	}
	
	@Override
	public void updateOneByField(String field, Object value, E entity, boolean upsert) {
		datastore.updateFirst(newQuery(field, value), entity, upsert);
	}
	
	@Override
	public void updateOneByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations) {
		updateOneByField(field, value, operations, false);
	}
	
	@Override
	public void updateOneByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations, boolean upsert) {
		updateOneByQuery(newQuery(field, value), operations, upsert);
	}
	
	@Override
	public void updateManyByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations) {
		UpdateOperations<E> realOperations = datastore.createUpdateOperations(getEntityType());
		EzyUpdateOperations<E> proxyOperations = new EzySimpleUpdateOperations<>(realOperations);
		operations.apply(proxyOperations);
		UpdateOptions realOptions = new UpdateOptions().multi(true).upsert(false);
		datastore.update(newQuery(field, value), realOperations, realOptions);
	}
	
	private void updateOneByQuery(Query<E> query, EzyApply<EzyUpdateOperations<E>> operations, boolean upsert) {
		UpdateOperations<E> realOperations = datastore.createUpdateOperations(getEntityType());
		EzyUpdateOperations<E> proxyOperations = new EzySimpleUpdateOperations<>(realOperations);
		operations.apply(proxyOperations);
		datastore.updateFirst(query, realOperations, upsert);
	}

	@Override
	public E findAndModifyById(I id, EzyApply<EzyUpdateOperations<E>> operations) {
		return findAndModifyByQuery(newQuery("_id", id), operations);
	}
	
	@Override
	public E findAndModifyById(I id, EzyApply<EzyUpdateOperations<E>> operations, EzyApply<EzyFindAndModifyOptions> options) {
		return findAndModifyByQuery(newQuery("_id", id), operations, options);
	}
	
	@Override
	public E findAndModifyByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations) {
		return findAndModifyByQuery(newQuery(field, value), operations);
	}
	
	@Override
	public E findAndModifyByField(String field, Object value, EzyApply<EzyUpdateOperations<E>> operations,
			EzyApply<EzyFindAndModifyOptions> options) {
		return findAndModifyByQuery(newQuery(field, value), operations, options);
	}
	
	private E findAndModifyByQuery(Query<E> query, EzyApply<EzyUpdateOperations<E>> operations) {
		UpdateOperations<E> realOperations = datastore.createUpdateOperations(getEntityType());
		EzyUpdateOperations<E> proxyOperations = new EzySimpleUpdateOperations<>(realOperations);
		operations.apply(proxyOperations);
		return datastore.findAndModify(query, realOperations);
	}
	
	private E findAndModifyByQuery(Query<E> query, EzyApply<EzyUpdateOperations<E>> operations, EzyApply<EzyFindAndModifyOptions> options) {
		UpdateOperations<E> realOperations = datastore.createUpdateOperations(getEntityType());
		EzyUpdateOperations<E> proxyOperations = new EzySimpleUpdateOperations<>(realOperations);
		FindAndModifyOptions realOptions = new FindAndModifyOptions();
		EzyFindAndModifyOptions proxyOptions = new EzySimpleFindAndModifyOptions(realOptions);
		operations.apply(proxyOperations);
		options.apply(proxyOptions);
		return datastore.findAndModify(query, realOperations, realOptions);
	}
	
	@Override
	public void delete(I id) {
		datastore.delete(newQuery("_id", id), new DeleteOptions().copy());
	}

	@Override
	public int deleteByIds(Collection<I> ids) {
		WriteResult result = datastore.delete(newQuery().field("_id").in(ids), new DeleteOptions().copy());
		return result.getN();
	}
	
	@Override
	public int deleteAll() {
		WriteResult result = datastore.delete(newQuery());
		return result.getN();
	}
	
	protected Query<E> newQuery() {
		return datastore.createQuery(getEntityType());
	}
	
	protected Query<E> newQuery(String field, Object value) {
		return newQuery().field(field).equal(value);
	}
}
