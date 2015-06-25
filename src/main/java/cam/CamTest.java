package cam;

import intel.pcsdk.PXCMPoint3DF32;
import intel.pcsdk.PXCUPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * @author Tom
 */
public class CamTest extends JApplet {

    private static final Logger LOG = LoggerFactory.getLogger(CamTest.class);

    public static void main(String[] args) {

        PXCUPipeline pp = new PXCUPipeline();
        //we must initialise each stream we wish to sample
        if (!pp.Init(PXCUPipeline.COLOR_VGA | PXCUPipeline.GESTURE | PXCUPipeline.DEPTH_QVGA_60FPS)) {
            System.out.println("Failed to initialize PXCUPipeline (camera input streams)\n");
            System.exit(3);
        }

        int[] depthMapSize = new int[2];
        pp.QueryDepthMapSize(depthMapSize);
        System.out.println("Depth map size x : " + depthMapSize[0]);
        System.out.println("Depth map size y : " + depthMapSize[1]);

        int[] colorMapSize = new int[2];
        pp.QueryRGBSize(colorMapSize);
        System.out.println("Color map size x :" + colorMapSize[0]);
        System.out.println("Color map size y :" + colorMapSize[1]);

        int[] irMapSize = new int[2];
        pp.QueryIRMapSize(irMapSize);
        System.out.println("IR map size x :" + irMapSize[0]);
        System.out.println("IR map size y :" + irMapSize[1]);

        CamTest app = new CamTest();
        DrawFrame df = new DrawFrame(colorMapSize[0], colorMapSize[1]);
        app.add(df);

        JFrame frame = new JFrame("MOLAB Perceptual Computing SDK Java Sample");
        Listener listener = new Listener();
        frame.addWindowListener(listener);
        frame.setSize(depthMapSize[0], depthMapSize[1]);
        frame.add(app);
        frame.setVisible(true);
//        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        PXCMPoint3DF32[] points3D = new PXCMPoint3DF32[depthMapSize[0] * depthMapSize[1]];

        short[] depthMap = new short[depthMapSize[0] * depthMapSize[1]];

        //loop whilst the app window is open
        while (!listener.exit) {
            //capture a frame
            pp.AcquireFrame(true);
            //get the depth map from the frame
            pp.QueryDepthMap(depthMap);
//            pp.QueryIRMap(depthMap);

            //step through the depth map and store as a point
            int x = 0;
            int y = 0;
            for (int i = 0; i < depthMap.length; i++) {
                if (i % depthMapSize[0] == 0) {
                    x = 0;
                    y++;
                }
                points3D[i] = new PXCMPoint3DF32(x, y, depthMap[i]);
                x++;
            }

            //draw the image
            for (int i = 0; i < points3D.length; i++) {
                int height = (int) points3D[i].z;
                df.image.setRGB((int) points3D[i].x, (int) points3D[i].y, height);

            }
            df.repaint();

            //sleep to avoid memory issues
//            try {
//                Thread.sleep(2000l);
//            } catch (InterruptedException ex) {
//                LOG.log(Level.SEVERE, "Thread sleep interrupted", ex);
//            }
            //must release the frame in order to capture a new one on the next loop
            pp.ReleaseFrame();
        }

        pp.Close();
        System.exit(0);
    }
}

/**
 * Listens for exit event on jframe
 */
class Listener extends WindowAdapter {

    public boolean exit = false;

    @Override
    public void windowClosing(WindowEvent e) {
        exit = true;
    }
}

/**
 * Image that gets drawn into the jframe window
 */
class DrawFrame extends Component {

    public BufferedImage image;

    public DrawFrame(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_555_RGB);
    }

    public void paint(Graphics g) {
        ((Graphics2D) g).drawImage(image, 0, 0, null);
    }
}
