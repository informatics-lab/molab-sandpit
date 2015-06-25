/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.informaticslab.sandpit.io.impl;

import intel.pcsdk.PXCUPipeline;
import java.io.Closeable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.informaticslab.sandpit.domain.Dimension2D;
import uk.co.informaticslab.sandpit.io.Camera3D;
import uk.co.informaticslab.sandpit.io.Camera3DException;

/**
 *
 * @author Tom
 */
public class Senz3DCamera implements Camera3D, Closeable {
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
    public Dimension2D getDepthMapDimensions() {
        int[] depthMapSize = new int[2];
        pipeline.QueryDepthMapSize(depthMapSize);
        LOG.debug("Depth map size width : " + depthMapSize[0]);
        LOG.debug("Depth map size height : " + depthMapSize[1]);
        Dimension2D dim = new Dimension2D(depthMapSize[0], depthMapSize[1]);
        return dim;
    }

    /**
     * @return the dimensions of the color map
     */
    @Override
    public Dimension2D getColorMapDimensions() {
        int[] colorMapSize = new int[2];
        pipeline.QueryRGBSize(colorMapSize);
        LOG.debug("Color map size width : " + colorMapSize[0]);
        LOG.debug("Color map size height : " + colorMapSize[1]);
        Dimension2D dim = new Dimension2D(colorMapSize[0], colorMapSize[1]);
        return dim;
    }

    /**
     * @return the dimensions of the IR map
     */
    @Override
    public Dimension2D getIRMapDimensions() {
        int[] irMapSize = new int[2];
        pipeline.QueryIRMapSize(irMapSize);
        LOG.debug("IR map size width : " + irMapSize[0]);
        LOG.debug("IR map size height : " + irMapSize[1]);
        Dimension2D dim = new Dimension2D(irMapSize[0], irMapSize[1]);
        return dim;
    }
    
    @Override
    public short[] sampleDepthMap() {
        pipeline.AcquireFrame(true);
        
        Dimension2D depthMapSize = getDepthMapDimensions();
        short[] depthMap = new short[depthMapSize.getWidth() * depthMapSize.getHeight()];
        pipeline.QueryDepthMap(depthMap);
        
        pipeline.ReleaseFrame();
        return depthMap;
    }
    
    @Override
    public short[] sampleIRMap() {
        pipeline.AcquireFrame(true);
        
        Dimension2D irMapSize = getIRMapDimensions();
        short[] irMap = new short[irMapSize.getWidth() * irMapSize.getHeight()];
        pipeline.QueryIRMap(irMap);
        
        pipeline.ReleaseFrame();
        return irMap;
    }
    
    @Override
    public int[] sampleColorMap() {
        pipeline.AcquireFrame(true);
        
        Dimension2D colorMapSize = getColorMapDimensions();
        int[] colorMap = new int[colorMapSize.getWidth() * colorMapSize.getHeight()];
        pipeline.QueryRGB(colorMap);
        
        pipeline.ReleaseFrame();
        return colorMap;
    }
    

    @Override
    public void close() {
        LOG.debug("Closing 3D camera");
        pipeline.Close();
    }
}
