package uk.co.informaticslab.sandpit.io;

import intel.pcsdk.PXCUPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.informaticslab.sandpit.domain.Dimension2D;

import java.io.Closeable;

/**
 * Wrapper for the 3D camera
 */
public interface Camera3D {

    /**
     * @return the dimensions of the depth map
     */
    public Dimension2D getDepthMapDimensions();

    /**
     * @return the dimensions of the color map
     */
    public Dimension2D getColorMapDimensions();

    /**
     * @return the dimensions of the IR map
     */
    public Dimension2D getIRMapDimensions();

    public short[] sampleDepthMap();
    
    public short[] sampleIRMap();
    
    public int[] sampleColorMap();
    
}
