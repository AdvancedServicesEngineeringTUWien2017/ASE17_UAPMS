package ase.shared.Misc;

/**
 * Created by Tommi on 14.06.2017.
 */
public interface IQueueListener<E> {
    void onReceive(E p);
}
