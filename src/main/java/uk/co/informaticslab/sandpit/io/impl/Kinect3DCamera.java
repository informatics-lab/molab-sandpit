package uk.co.informaticslab.sandpit.io.impl;

import boofcv.gui.image.ImagePanel;
import boofcv.gui.image.ShowImages;
import boofcv.gui.image.VisualizeImageData;
import boofcv.openkinect.UtilOpenKinect;
import boofcv.struct.image.GrayU16;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;
import com.jme3.math.Vector2f;
import org.openkinect.freenect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.informaticslab.sandpit.io.Camera3D;
import uk.co.informaticslab.sandpit.io.Camera3DException;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by tom on 05/04/16.
 */
public class Kinect3DCamera implements Camera3D, Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(Kinect3DCamera.class);

    private final Context ctx;
    private final Device device;

    private ByteBuffer currentFrame;

    BufferedImage outDepth = null;
    ImagePanel guiDepth = null;
    GrayU16 depth = null;

    /**
     * Constructor
     * Initialises the camera object with its streams.
     */
    public Kinect3DCamera() {

        LOG.debug("Initialising 3D camera");
        this.ctx = Freenect.createContext();
        if (ctx.numDevices() < 0) {
            throw new Camera3DException("No kinect found!");
        }
        this.device = ctx.openDevice(0);
        device.setDepthFormat(DepthFormat.REGISTERED);
        device.setVideoFormat(VideoFormat.RGB);

        device.startDepth(new DepthHandler() {
            @Override
            public void onFrameReceived(FrameMode mode, ByteBuffer frame, int timestamp) {
                if(depth == null) {
                    depth = new GrayU16(mode.getWidth(), mode.getHeight());
                }
                currentFrame = frame;
//                processDepth(mode, frame, timestamp);
            }
        });

    }

    @Override
    public Vector2f getDepthMapDimensions() {
        return new Vector2f(device.getDepthMode().getWidth(), device.getDepthMode().getHeight());
    }

    @Override
    public short[] sampleDepthMap() {
        if(currentFrame==null){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        UtilOpenKinect.bufferDepthToU16(currentFrame, depth);
        return depth.getData();
    }

    public Device getDevice() {
        return device;
    }

    @Override
    public void close() throws IOException {
        device.stopDepth();
        device.close();
        ctx.shutdown();
    }

    protected void processDepth(FrameMode mode, ByteBuffer frame, int timestamp) {

        if (outDepth == null) {
            depth.reshape(mode.getWidth(), mode.getHeight());
            outDepth = new BufferedImage(depth.width, depth.height, BufferedImage.TYPE_INT_RGB);
            guiDepth = ShowImages.showWindow(outDepth, "Depth Image");
        }

        UtilOpenKinect.bufferDepthToU16(frame, depth);

//		VisualizeImageData.grayUnsigned(depth,outDepth,UtilOpenKinect.FREENECT_DEPTH_MM_MAX_VALUE);
        VisualizeImageData.disparity(depth, outDepth, 0, UtilOpenKinect.FREENECT_DEPTH_MM_MAX_VALUE, 0);
        guiDepth.repaint();
    }

}
