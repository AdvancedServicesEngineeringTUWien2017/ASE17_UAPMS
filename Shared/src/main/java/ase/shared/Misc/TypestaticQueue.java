package ase.shared.Misc;

import java.util.concurrent.*;

/**
 * Created by Tommi on 14.06.2017.
 */
public class TypestaticQueue<E>{

    private static ConcurrentMap<Class, BlockingQueue<Object>> map = new ConcurrentHashMap();
    private BlockingQueue<Object> queue;

    public TypestaticQueue(Class<E> c){
        if(!map.containsKey(c)){
            queue = new LinkedBlockingQueue<>();
            map.put(c, queue);
        }else{
            queue = map.get(c);
        }
    }

    public boolean add(E e) {
        return queue.add(e);
    }

    public E poll() {
        return (E) queue.poll();
    }

    public E poll(long timeout, TimeUnit timeUnit) throws InterruptedException {
        return (E) queue.poll(timeout, timeUnit);
    }

    public E take() throws InterruptedException {
        return (E) queue.take();
    }
}
