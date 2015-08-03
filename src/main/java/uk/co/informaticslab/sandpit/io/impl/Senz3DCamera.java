/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.informaticslab.sandpit.io.impl;

import com.jme3.math.Vector2f;
import intel.pcsdk.PXCUPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.informaticslab.sandpit.io.Camera3D;
import uk.co.informaticslab.sandpit.io.Camera3DException;

import java.io.Closeable;

/**
 * @author Tom
 */
public class Senz3DCamera implements Camera3D, Closeable {

    public static final short UNTRUSTED_VALUE = 32001;

    private static final Logger LOG = LoggerFactory.getLogger(Camera3D.class);

    private final PXCUPipeline pipeline;

    /**
     * Constructor
     * Initialises the camera object with its streams.
     */
    public Senz3DCamera() {
        LOG.debug("Initialising 3D camera");
        this.pipeline = new PXCUPipeline();
        if (!pipeline.Init(PXCUPipeline.COLOR_VGA | PXCUPipeline.GESTURE | PXCUPipeline.DEPTH_QVGA_60FPS)) {
            throw new Camera3DException("Could not initialise 3D camera streams");
        }
    }

    /**
     * @return the dimensions of the depth map
     */
    @Override
    public Vector2f getDepthMapDimensions() {
        int[] depthMapSize = new int[2];
        pipeline.QueryDepthMapSize(depthMapSize);
//        LOG.debug("Depth map size width : " + depthMapSize[0]);
//        LOG.debug("Depth map size height : " + depthMapSize[1]);
        Vector2f dim = new Vector2f(depthMapSize[0], depthMapSize[1]);
        return dim;
    }

    @Override
    public short[] sampleDepthMap() {
        pipeline.AcquireFrame(true);
        Vector2f depthMapSize = getDepthMapDimensions();
        short[] depthMapSample = new short[(int)depthMapSize.getX() * (int)depthMapSize.getY()];
        pipeline.QueryDepthMap(depthMapSample);
        pipeline.ReleaseFrame();
        return depthMapSample;
    }

    @Override
    public void close() {
        LOG.debug("Closing 3D camera");
        pipeline.Close();
    }
}
