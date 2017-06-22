package ase.pm.tourworker.Misc;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Tommi on 05.06.2017.
 */
public class NullStream extends OutputStream {

    @Override
    public void write(int b) throws IOException {
    }
}
