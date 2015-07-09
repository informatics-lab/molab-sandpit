package uk.co.informaticslab.sandpit.io;

import uk.co.informaticslab.sandpit.domain.Dimension2D;

/**
 * Interface for the 3D camera
 */
public interface Camera3D {

    /**
     * @return the dimensions of the depth map
     */
    Dimension2D getDepthMapDimensions();


    /**
     * @return the current depth map sample
     */
    short[] sampleDepthMap();

}
