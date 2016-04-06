package uk.co.informaticslab.sandpit.io.impl;

import com.jme3.math.Vector2f;
import org.junit.*;
import org.openkinect.freenect.Device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by tom on 05/04/16.
 */
public class Kinect3DCameraTest {

    private Kinect3DCamera kinect;

    @Before
    public void setUp() {
        kinect = new Kinect3DCamera();
    }

    @After
    public void tearDown() throws Exception {
        kinect.close();
    }

    @Test
    public void testDepthDimensions() {
        Vector2f expected = new Vector2f(640,480);
        assertEquals("Depth map resolution", kinect.getDepthMapDimensions(), expected);
    }

    /*
     * verify this by looking at the actual device and checking it moves!
     */
    @Test
    public void testTiltKinect() {
        Device device = kinect.getDevice();
        try {
            device.setTiltAngle(20);
            Thread.sleep(2000);
            device.setTiltAngle(-20);
            Thread.sleep(2000);
            device.setTiltAngle(0);
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    @Test(timeout = 10000)
    public void testDepthSample() throws Exception {
        Thread.sleep(5000);
        assertNotNull("Depth map sample", kinect.sampleDepthMap());
    }

}