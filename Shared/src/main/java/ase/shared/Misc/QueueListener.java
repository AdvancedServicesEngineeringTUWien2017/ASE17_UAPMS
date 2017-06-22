package ase.shared.Misc;

/**
 * Created by Tommi on 14.06.2017.
 */
public class QueueListener<E> implements Runnable {

    private IQueueListener<E> listener;
    private TypestaticQueue<E> queue;

    public QueueListener(TypestaticQueue<E> queue, IQueueListener<E> listener){
        this.queue = queue;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()){
                E e = this.queue.take();
                notifyListener(e);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void notifyListener(E e){
        if(listener != null) {
            listener.onReceive(e);
        }
    }
}
