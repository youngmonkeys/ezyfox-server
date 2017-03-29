package com.tvd12.ezyfoxserver.pattern;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.entity.EzyDestroyable;
import com.tvd12.ezyfoxserver.entity.EzyStartable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyReturner;

public abstract class EzyObjectPool<T> 
		extends EzyLoggable 
		implements EzyStartable, EzyDestroyable {
	
	protected Queue<T> pool;
	protected int minObjects;
	protected int maxObjects;
	protected long validationInterval;
	protected List<T> borrowedObjects;
	protected EzyObjectFactory<T> objectFactory;
	protected ScheduledExecutorService validationService;
	
	@SuppressWarnings({ "rawtypes"})
	protected EzyObjectPool(AbstractBuilder builder) {
		initialize(builder);
	}
	
	@SuppressWarnings("rawtypes")
	private final void initialize(AbstractBuilder builder) {
		initComponents(builder);
		initializeObjects();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initComponents(AbstractBuilder builder) {
		this.logger = newLogger();
		this.pool = builder.pool;
		this.minObjects = builder.minObjects;
		this.maxObjects = builder.maxObjects;
		this.objectFactory = builder.objectFactory;
		this.borrowedObjects = builder.newBorrowedObjects();
		this.validationService = builder.validationService;
		this.validationInterval = builder.validationInterval;
	}
	
	protected final void initializeObjects() {
		for(int i = 0 ; i < minObjects ; i++)
			pool.add(createObject());
	}
	
	protected T createObject() {
		return objectFactory.newProduct();
	}
	
	protected void removeObject(T object) {
	}
	
	@Override
	public void start() throws Exception {
		validationService.scheduleWithFixedDelay(newValidationTask(), 
				validationInterval, validationInterval, TimeUnit.MILLISECONDS);
	}
	
	protected Runnable newValidationTask() {
		return new Runnable() {
			
			@Override
			public void run() {
				try {
					addOrRemoveObjects(pool.size());
					removeStaleObjects();
				}
				catch(Exception e) {
					getLogger().error("object poll validation error", e);
				}
			}
			
			private void addOrRemoveObjects(int poolSize) {
				if(poolSize < minObjects)
					addObjects(poolSize);
				else if(poolSize > maxObjects)
					removeObjects(poolSize);
			}
			
			private void addObjects(int poolSize) {
				if(poolSize < minObjects)
					addNewObjects(minObjects - poolSize);
			}
			
			private void addNewObjects(int size) {
				for(int i = 0 ; i < size ; i++)
					pool.add(createObject());
			}
			
			private void removeObjects(int poolSize) {
				if(poolSize > maxObjects)
					removeUnusedObjects(poolSize - maxObjects);
			}
			
			private void removeUnusedObjects(int size) {
				for(int i = 0 ; i < size ; i++)
					removeObject(pool.poll());
			}
		};
	}
	
	protected void removeStaleObjects() {
	}

	protected final T borrowObject() {
		T obj = borrowOrNewObject();
		borrowedObjects.add(obj);
		return obj;
	}
	
	private final T borrowOrNewObject() {
		return EzyReturner.returnNotNull(pool.poll(), createObject());
	}
	
	protected final boolean returnObject(T object) {
		return object != null ? doReturnObject(object) : false;
	}
	
	private final boolean doReturnObject(T object) {
		borrowedObjects.remove(object);
		return pool.offer(object);
	}
	
	@Override
	public void destroy() {
		try {
			validationService.shutdown();
		}
		catch(Exception e) {
			getLogger().error(getClass().getSimpleName() + " error", e);
		}
	}
	
	protected Logger getLogger() {
		return this.logger;
	}
	
	private Logger newLogger() {
		return LoggerFactory.getLogger(getClass());
	}
	
	@SuppressWarnings("rawtypes")
	public static abstract class AbstractBuilder<T, B extends AbstractBuilder> 
			implements EzyBuilder<EzyObjectPool<T>> {
		
		private Queue<T> pool;
		private int minObjects;
		private int maxObjects;
		private long validationInterval;
		protected EzyObjectFactory<T> objectFactory;
		private ScheduledExecutorService validationService;
		
		public B pool(Queue<T> pool) {
			this.pool = pool;
			return getThis();
		}
		
		public B minObjects(int minObjects) {
			this.minObjects = minObjects;
			return getThis();
		}
		
		public B maxObjects(int maxObjects) {
			this.maxObjects = maxObjects;
			return getThis();
		}
		
		public B objectFactory(EzyObjectFactory<T> objectFactory) {
			this.objectFactory = objectFactory;
			return getThis();
		}
		
		public B validationInterval(long validationInterval) {
			this.validationInterval = validationInterval;
			return getThis();
		}
		
		public B validationService(ScheduledExecutorService validationService) {
			this.validationService = validationService;
			return getThis();
		}
		
		@Override
		public EzyObjectPool<T> build() {
			this.prepare();
			return newProduct();
		}

		protected abstract String getProductName();
		protected abstract EzyObjectPool<T> newProduct();
		
		protected void prepare() {
			initPool();
			initSimpleValue();
			initValidationService();
		}
		
		protected void initSimpleValue() {
			this.minObjects = 300;
			this.maxObjects = 5000;
			this.validationInterval = 3 * 1000;
		}
		
		protected void initPool() {
			if(pool == null)
				pool = new ConcurrentLinkedQueue<>();
		}
		
		protected void initValidationService() {
			if(validationService == null)
				validationService = newValidationService();
		}
		
		protected ScheduledExecutorService newValidationService() {
			return EzyExecutors.newSingleThreadScheduledExecutor(getProductName());
		}
		
		protected List<T> newBorrowedObjects() {
			return new CopyOnWriteArrayList<>();
		}
		
		@SuppressWarnings("unchecked")
		private B getThis() {
			return (B)this;
		}
	}
	
}
