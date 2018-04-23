package com.tvd12.ezyfoxserver.pattern;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithLogException;

import java.util.ArrayList;
import java.util.List;
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

public abstract class EzyObjectProvider<T> 
		extends EzyLoggable 
		implements EzyStartable, EzyDestroyable {
	
	protected final long validationInterval;
	protected final List<T> providedObjects;
	protected final EzyObjectFactory<T> objectFactory;
	protected final ScheduledExecutorService validationService;
	
	protected final Lock lock = new ReentrantLock();
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	protected EzyObjectProvider(Builder builder) {
		this.objectFactory = builder.getObjectFactory();
		this.providedObjects = builder.newProvidedObjects();
		this.validationInterval = builder.validationInterval;
		this.validationService = builder.getValidationService();
	}
	
	protected T createObject() {
		return objectFactory.newProduct();
	}
	
	protected List<T> getProvidedObjects() {
        return new ArrayList<>(providedObjects);
    }
	
	@Override
	public void start() throws Exception {
		startValidationService();
	}
	
	protected void startValidationService() {
		validationService.scheduleWithFixedDelay(newValidationTask(), 
				3 * 1000, validationInterval, TimeUnit.MILLISECONDS);
	}
	
	protected Runnable newValidationTask() {
		return new Runnable() {
			
			@Override
			public void run() {
				try {
					removeStaleObjects();
				}
				catch(Exception e) {
					getLogger().error("object provider validation error", e);
				}
			}
			
		};
	}
	
	protected void removeStaleObjects() {
	}
	
	protected final T provideObject() {
		return returnWithLock(this::provideObject0);
	}
	
	private final T provideObject0() {
		T object = createObject();
		providedObjects.add(object);
		return object;
	}
	
	protected void runWithLock(EzyVoid applier) {
		EzyProcessor.processWithLock(applier, lock);
	}
	
	protected <R> R returnWithLock(Supplier<R> supplier) {
		return EzyReturner.returnWithLock(supplier, lock);
	}
	
	@Override
	public void destroy() {
		try {
			clearAll();
			shutdownAll();
		}
		catch(Exception e) {
			getLogger().error(getClass().getSimpleName() + " error", e);
		}
	}
	
	protected void clearAll() {
		providedObjects.clear();
	}
	
	protected void shutdownAll() {
		processWithLogException(validationService::shutdown);
	}
	
	@SuppressWarnings("rawtypes")
	public static abstract class Builder<T, B extends Builder> 
			implements EzyBuilder<EzyObjectProvider<T>> {
		
		private long validationInterval = 100;
		protected EzyObjectFactory<T> objectFactory;
		private ScheduledExecutorService validationService;
		
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
		
		protected List<T> newProvidedObjects() {
			return new ArrayList<>();
		}
		
	}
	
}
