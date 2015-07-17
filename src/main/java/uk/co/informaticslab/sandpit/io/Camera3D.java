package uk.co.informaticslab.sandpit.io;

import com.jme3.math.Vector2f;

/**
 * Interface for the 3D camera
 */
public interface Camera3D {

    /**
     * @return the dimensions of the depth map
     */
    Vector2f getDepthMapDimensions();


    /**
     * @return the current depth map sample
     */
    short[] sampleDepthMap();

}
