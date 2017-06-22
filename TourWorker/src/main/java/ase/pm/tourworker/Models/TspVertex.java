package ase.pm.tourworker.Models;

/**
 * Created by tommi on 04.06.17.
 */
public class TspVertex {

    public TspVertex(){

    }
    public TspVertex(int id, int localId){
        this.id = id;
        this.localId = localId;
    }


    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    private int localId;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
