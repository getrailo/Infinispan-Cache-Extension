package railo.extension.io.cache.infinispan;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.infinispan.AbstractDelegatingAdvancedCache;
import org.infinispan.AdvancedCache;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryActivated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryEvicted;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryInvalidated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryLoaded;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryPassivated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryVisited;
import org.infinispan.notifications.cachelistener.annotation.TransactionCompleted;
import org.infinispan.notifications.cachelistener.annotation.TransactionRegistered;
import org.infinispan.notifications.cachelistener.event.Event;
import org.infinispan.stats.Stats;

public class ClassLoaderAwareCache<K, V> extends AbstractDelegatingAdvancedCache<K, V>
{
   final WeakReference<ClassLoader> classLoaderRef;
   
   public ClassLoaderAwareCache(AdvancedCache<K, V> cache, ClassLoader classLoader)
   {
      super(cache);
      this.classLoaderRef = new WeakReference<ClassLoader>(classLoader);
   }

   @Override
   public Stats getStats()
   {
      return this.getAdvancedCache().getStats();
   }
   
   @Override
   public void stop()
   {
      super.stop();
      this.classLoaderRef.clear();
   }

   @Override
   public void addListener(Object listener)
   {
      super.addListener(new ClassLoaderAwareListener(listener));
   }
   
   static final Map<Class<? extends Annotation>, Event.Type> events = new HashMap<Class<? extends Annotation>, Event.Type>();

   static {
      events.put(CacheEntryActivated.class, Event.Type.CACHE_ENTRY_ACTIVATED);
      events.put(CacheEntryCreated.class, Event.Type.CACHE_ENTRY_CREATED);
      events.put(CacheEntryEvicted.class, Event.Type.CACHE_ENTRY_EVICTED);
      events.put(CacheEntryInvalidated.class, Event.Type.CACHE_ENTRY_INVALIDATED);
      events.put(CacheEntryLoaded.class, Event.Type.CACHE_ENTRY_LOADED);
      events.put(CacheEntryModified.class, Event.Type.CACHE_ENTRY_MODIFIED);
      events.put(CacheEntryPassivated.class, Event.Type.CACHE_ENTRY_PASSIVATED);
      events.put(CacheEntryRemoved.class, Event.Type.CACHE_ENTRY_REMOVED);
      events.put(CacheEntryVisited.class, Event.Type.CACHE_ENTRY_VISITED);
      events.put(TransactionCompleted.class, Event.Type.TRANSACTION_COMPLETED);
      events.put(TransactionRegistered.class, Event.Type.TRANSACTION_REGISTERED);
   }
   
   @Listener
   public class ClassLoaderAwareListener
   {
      private final Object listener;
      private final Map<Event.Type, List<Method>> methods = new EnumMap<Event.Type, List<Method>>(Event.Type.class);
      
      ClassLoaderAwareListener(Object listener)
      {
         this.listener = listener;
         for (Event.Type type: Event.Type.values())
         {
            this.methods.put(type, new LinkedList<Method>());
         }
         for (Method method: listener.getClass().getMethods())
         {
            for (Map.Entry<Class<? extends Annotation>, Event.Type> entry: events.entrySet())
            {
               Class<? extends Annotation> annotation = entry.getKey();
               if (method.isAnnotationPresent(annotation))
               {
                  this.methods.get(entry.getValue()).add(method);
               }
            }
         }
      }
      
      @CacheEntryActivated
      @CacheEntryCreated
      @CacheEntryEvicted
      @CacheEntryInvalidated
      @CacheEntryLoaded
      @CacheEntryModified
      @CacheEntryPassivated
      @CacheEntryRemoved
      @CacheEntryVisited
      public void swapClassLoaderEvent(Event event) throws Throwable
      {
         List<Method> methods = this.methods.get(event.getType());
         if (!methods.isEmpty())
         {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            this.setContextClassLoader(ClassLoaderAwareCache.this.classLoaderRef.get());
            try
            {
               this.event(event, methods);
            }
            finally
            {
               this.setContextClassLoader(classLoader);
            }
         }
      }

      private void setContextClassLoader(final ClassLoader classLoader)
      {
         PrivilegedAction<Void> action = new PrivilegedAction<Void>()
         {
            @Override
            public Void run()
            {
               Thread.currentThread().setContextClassLoader(classLoader);
               return null;
            }
         };
         AccessController.doPrivileged(action);
      }
      
      @TransactionCompleted
      @TransactionRegistered
      public void otherEvent(Event event) throws Throwable
      {
         this.event(event, this.methods.get(event.getType()));
      }
      
      private void event(Event event, List<Method> methods) throws Throwable
      {
         for (Method method: methods)
         {
            try
            {
               method.invoke(this.listener, event);
            }
            catch (InvocationTargetException e)
            {
               throw e.getCause();
            }
         }
      }
      
      public int hashCode()
      {
         return this.listener.hashCode();
      }
      
      public boolean equals(Object object)
      {
         if (object == null) return false;
         if (object instanceof ClassLoaderAwareCache.ClassLoaderAwareListener)
         {
            @SuppressWarnings("unchecked")
            ClassLoaderAwareListener listener = (ClassLoaderAwareListener) object;
            return this.listener.equals(listener.listener);
         }
         return this.listener.equals(object);
      }
   }
}