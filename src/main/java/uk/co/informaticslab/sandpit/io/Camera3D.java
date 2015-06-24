package uk.co.informaticslab.sandpit.io;

import intel.pcsdk.PXCUPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.informaticslab.sandpit.domain.Dimension2D;

import java.io.Closeable;

/**
 * Wrapper for the 3D camera
 */
public class Camera3D implements Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(Camera3D.class);
    private final PXCUPipeline camera3D;

    /**
     * Constructor
     * Initialises the camera object with its streams.
     */
    public Camera3D() {
        LOG.debug("Initialising 3D camera");
        this.camera3D = new PXCUPipeline();
        if (!camera3D.Init(PXCUPipeline.COLOR_VGA | PXCUPipeline.GESTURE | PXCUPipeline.DEPTH_QVGA_60FPS)) {
            throw new Camera3DException("Could not initialise 3D camera streams");
        }
    }

    /**
     * @return the dimensions of the depth map
     */
    public Dimension2D getDepthMapDimensions() {
        int[] depthMapSize = new int[2];
        camera3D.QueryDepthMapSize(depthMapSize);
        LOG.debug("Depth map size width : " + depthMapSize[0]);
        LOG.debug("Depth map size height : " + depthMapSize[1]);
        Dimension2D dim = new Dimension2D(depthMapSize[0], depthMapSize[1]);
        return dim;
    }

    /**
     * @return the dimensions of the color map
     */
    public Dimension2D getColorMapDimensions() {
        int[] colorMapSize = new int[2];
        camera3D.QueryRGBSize(colorMapSize);
        LOG.debug("Color map size width : " + colorMapSize[0]);
        LOG.debug("Color map size height : " + colorMapSize[1]);
        Dimension2D dim = new Dimension2D(colorMapSize[0], colorMapSize[1]);
        return dim;
    }

    /**
     * @return the dimensions of the IR map
     */
    public Dimension2D getIRMapDimensions() {
        int[] irMapSize = new int[2];
        camera3D.QueryIRMapSize(irMapSize);
        LOG.debug("IR map size width : " + irMapSize[0]);
        LOG.debug("IR map size height : " + irMapSize[1]);
        Dimension2D dim = new Dimension2D(irMapSize[0], irMapSize[1]);
        return dim;
    }

    public PXCUPipeline getCamera3D() {
        return camera3D;
    }

    @Override
    public void close() {
        LOG.debug("Closing 3D camera");
        camera3D.Close();
    }
}
