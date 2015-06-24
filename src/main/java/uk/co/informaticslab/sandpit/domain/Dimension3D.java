package uk.co.informaticslab.sandpit.domain;

/**
 * Created by tom on 24/06/2015.
 */
public class Dimension3D extends Dimension2D {

    private final int depth;

    public Dimension3D(int width, int height, int depth) {
        super(width, height);
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }
}
