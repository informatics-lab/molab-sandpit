package cam;

import ch.qos.logback.classic.util.ContextInitializer;
import intel.pcsdk.PXCMPoint3DF32;
import intel.pcsdk.PXCUPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

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

        final int[] depthMapSize = new int[2];
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
        

        final short[] depthMap = new short[depthMapSize[0] * depthMapSize[1]];
        final File dataFile = new File("height-map.dat");
        
        JButton button = new JButton("snap"); 
        button.setBounds(320, 0, 100, 30);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                int xMax = depthMapSize[0];
                int yMax = depthMapSize[1];
                System.out.println("xMax: " + xMax);
                System.out.println("yMax: " + yMax);
                
                try (FileWriter fw = new FileWriter(dataFile)) {
                    
                    for (int y = 0; y < yMax; y++) {
                        StringBuilder rowBuilder = new StringBuilder();
                        for (int x = 0; x < xMax; x++) {
                            int index = x + y * xMax;
                            short value = depthMap[index];
                            rowBuilder.append(value).append(',');
                        }
                        CharSequence row = rowBuilder.subSequence(0, rowBuilder.length() - 1);
                        fw.write(row.toString());
                        fw.write("\n");
                    }
                    
                    fw.flush();
                    
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(CamTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        frame.add(button);
        frame.add(app);
        frame.setVisible(true);
//        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        PXCMPoint3DF32[] points3D = new PXCMPoint3DF32[depthMapSize[0] * depthMapSize[1]];

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
                int value;
                if (height <= 100) {
                 value = Color.blue.getRGB();
                } else if (height <= 200) {
                 value = Color.green.getRGB();
                } else if (height <= 300) {
                 value = Color.red.getRGB();
                } else if (height <= 400) {
                 value = Color.gray.getRGB();
                } else if (height <= 500) {
                 value = Color.pink.getRGB();
                } else if (height <= 600) {
                 value = Color.yellow.getRGB();
                } else if (height <= 700) {
                 value = 70;   
                } else if (height <= 800) {
                 value = 80;   
                } else if (height <= 900) {
                 value = 90;   
                } else if (height <= 1000) {
                 value = 100;   
                } else {
                 value = 0;   
                }
                
                int rgb;
                if (height > 2000) {
                    rgb = 0;
                } else {
                    int t1 = height - 521;
                    if (t1 < 0) {
                        t1 = 0;
                    }
                    double t2 = t1 / 465.0;
                    int t3 = (int) (t2 * 255);
                    if (t3 > 255) {
                        t3 = 255;
                    }
                    t3 = Math.abs(t3 - 255);
                    
                    rgb = toGreyScale(t3);
                }
                
                df.image.setRGB((int) points3D[i].x, (int) points3D[i].y, rgb);

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
    
    private static int toGreyScale(int value) {
        return ((0 & 0xFF) << 24) |
                ((value & 0xFF) << 16) |
                ((value & 0xFF) << 8) |
                ((value & 0xFF) << 0);
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
