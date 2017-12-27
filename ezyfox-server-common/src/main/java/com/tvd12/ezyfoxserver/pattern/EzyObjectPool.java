package com.tvd12.ezyfoxserver.pattern;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithLogException;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.function.EzyVoid;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyProcessor;
import com.tvd12.ezyfoxserver.util.EzyReturner;
import com.tvd12.ezyfoxserver.util.EzyStartable;

public abstract class EzyObjectPool<T> 
		extends EzyLoggable 
		implements EzyStartable, EzyDestroyable {
	
	protected final Queue<T> pool;
	protected final int minObjects;
	protected final int maxObjects;
	protected final long validationInterval;
	protected final List<T> borrowedObjects;
	protected final EzyObjectFactory<T> objectFactory;
	protected final ScheduledExecutorService validationService;
	
	protected final Lock lock = new ReentrantLock();
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	protected EzyObjectPool(Builder builder) {
		this.pool = builder.getPool();
		this.minObjects = builder.minObjects;
		this.maxObjects = builder.maxObjects;
		this.objectFactory = builder.getObjectFactory();
		this.borrowedObjects = builder.newBorrowedObjects();
		this.validationInterval = builder.validationInterval;
		this.validationService = builder.getValidationService();
	}
	
	protected final void initializeObjects() {
		for(int i = 0 ; i < minObjects ; i++)
			pool.add(createObject());
	}
	
	protected T createObject() {
		return objectFactory.newProduct();
	}
	
	private void releaseObject0(T object) {
		releaseObject(object);
	}
	
	protected void releaseObject(T object) {
	}
	
	protected List<T> getBorrowedObjects() {
        return new ArrayList<>(borrowedObjects);
    }
	
	protected List<T> getRemainObjects() {
		return new ArrayList<>(pool);
	}
	
	@Override
	public void start() throws Exception {
		startValidationService();
	}
	
	protected void startValidationService() {
		validationService.scheduleWithFixedDelay(newValidationTask(), 
				validationInterval, validationInterval, TimeUnit.MILLISECONDS);
	}
	
	protected Runnable newValidationTask() {
		return new Runnable() {
			
			@Override
			public void run() {
				try {
					removeObjects(pool.size());
					removeStaleObjects();
				}
				catch(Exception e) {
					getLogger().error("object poll validation error", e);
				}
			}
			
			private void removeObjects(int poolSize) {
				if(poolSize > maxObjects)
					removeObjects0(poolSize);
			}
			
			private void removeObjects0(int poolSize) {
				removeUnusedObjects(poolSize - maxObjects);
			}
			
			private void removeUnusedObjects(int size) {
				runWithLock(() -> removeUnusedObjects0(size));
			}
			
			private void removeUnusedObjects0(int size) {
				for(int i = 0 ; i < size ; i++)
					releaseObject0(pool.poll());
				getLogger().info("object pool: remove {} excessive objects, remain {}", size, pool.size());
			}
		};
	}
	
	protected void removeStaleObjects() {
		removeStaleObjects0();
	}
	
	protected void removeStaleObjects0() {
		removeStaleObjects(getCanBeStaleObjects());
	}
	
	protected void removeStaleObjects(List<T> objects) {
		for(T object : objects)
			if(isStaleObject(object))
				removeStaleObject(object);
	}
	
	protected void removeStaleObject(T object) {
	}
	
	protected boolean isStaleObject(T object) {
		return false;
	}
	
	protected List<T> getCanBeStaleObjects() {
		return getBorrowedObjects();
	}

	protected final T borrowObject() {
		return returnWithLock(() -> borrowObject0());
	}
	
	private final T borrowObject0() {
		T obj = borrowOrNewObject();
		borrowedObjects.add(obj);
		return obj;
	}
	
	protected final boolean returnObject(T object) {
        return returnWithLock(() -> returnObject0(object));
    }
	
	private final boolean returnObject0(T object) {
		return object != null ? doReturnObject(object) : false;
	}
	
	protected void runWithLock(EzyVoid applier) {
		EzyProcessor.processWithLock(applier, lock);
	}
	
	protected <R> R returnWithLock(Supplier<R> supplier) {
		return EzyReturner.returnWithLock(supplier, lock);
	}
	
	private T borrowOrNewObject() {
		return EzyReturner.returnNotNull(pool.poll(), createObject());
	}
	
	private boolean doReturnObject(T object) {
        borrowedObjects.remove(object);
        return pool.offer(object);
    }
	
	@Override
	public void destroy() {
		try {
			shutdownAll();
		}
		catch(Exception e) {
			getLogger().error(getClass().getSimpleName() + " error", e);
		}
	}
	
	protected void shutdownAll() {
		processWithLogException(validationService::shutdown);
	}
	
	@SuppressWarnings("rawtypes")
	public static abstract class Builder<T, B extends Builder> 
			implements EzyBuilder<EzyObjectPool<T>> {
		
		private Queue<T> pool;
		private int minObjects = 300;
		private int maxObjects = 300;
		private long validationInterval = 3 * 1000;
		protected EzyObjectFactory<T> objectFactory;
		private ScheduledExecutorService validationService;
		
		@SuppressWarnings("unchecked")
		public B pool(Queue<T> pool) {
			this.pool = pool;
			return (B)this;
		}
		
		@SuppressWarnings("unchecked")
		public B minObjects(int minObjects) {
			this.minObjects = minObjects;
			return (B)this;
		}
		
		@SuppressWarnings("unchecked")
		public B maxObjects(int maxObjects) {
			this.maxObjects = maxObjects;
			return (B)this;
		}
		
		@SuppressWarnings("unchecked")
		public B objectFactory(EzyObjectFactory<T> objectFactory) {
			this.objectFactory = objectFactory;
			return (B)this;
		}
		
		@SuppressWarnings("unchecked")
		public B validationInterval(long validationInterval) {
			this.validationInterval = validationInterval;
			return (B)this;
		}
		
		@SuppressWarnings("unchecked")
		public B validationService(ScheduledExecutorService validationService) {
			this.validationService = validationService;
			return (B)this;
		}
		
		protected abstract String getProductName();
		
		protected Queue<T> getPool() {
			return pool != null ? pool : newPool();
		}
		
		protected Queue<T> newPool() {
		    return new ConcurrentLinkedQueue<>();
		}
		
		protected EzyObjectFactory<T> getObjectFactory() {
		    return objectFactory != null ? objectFactory : newObjectFactory(); 
		}
		
		protected abstract EzyObjectFactory<T> newObjectFactory();
		
		protected ScheduledExecutorService getValidationService() {
			return validationService != null ? validationService : newValidationService(); 
		}
		
		protected ScheduledExecutorService newValidationService() {
			ScheduledExecutorService service = EzyExecutors.newScheduledThreadPool(3, newThreadFactory());
			Runtime.getRuntime().addShutdownHook(new Thread(() -> service.shutdown()));
			return service;
		}
		
		protected ThreadFactory newThreadFactory() {
			return EzyExecutors.newThreadFactory(getProductName());
		}
		
		protected List<T> newBorrowedObjects() {
			return new ArrayList<>();
		}
		
	}
	
}
