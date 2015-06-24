package uk.co.informaticslab.sandpit.domain;

/**
 * Created by tom on 24/06/2015.
 */
public class Dimension2D {

    private final int width;
    private final int height;

    public Dimension2D(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
