package uk.co.informaticslab.sandpit;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.renderer.RenderManager;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.informaticslab.sandpit.domain.Dimension2D;
import uk.co.informaticslab.sandpit.io.Camera3D;
import uk.co.informaticslab.sandpit.io.impl.Mock3DCamera;

public class Application extends SimpleApplication {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        Application app = new Application();
        app.start();
    }

    private TerrainQuad terrain;
    private Camera3D camera;
    
    @Override
    public void simpleInitApp() {
        
        LOG.debug("Initialising app");
        flyCam.setMoveSpeed(100f);

        camera = new Mock3DCamera();
        Dimension2D terrainDimensions = new Dimension2D(512, 512);
        
        
        Material mat = new Material(assetManager,
          "Common/MatDefs/Misc/ShowNormals.j3md");

        terrain = new TerrainQuad("my terrain", 65, 513, regridSample(camera.sampleDepthMap(), camera.getDepthMapDimensions(), terrainDimensions));

        terrain.setMaterial(mat);
        terrain.setLocalTranslation(0, -100, 0);
        terrain.setLocalScale(2f, 1f, 2f);
        rootNode.attachChild(terrain);

        /** 5. The LOD (level of detail) depends on were the camera is: */
        TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
        terrain.addControl(control);
    }

    @Override
    public void simpleUpdate(float tpf) {
        LOG.trace("update");
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        LOG.trace("render");
        //TODO: add render code
    }
    
    private float[] regridSample(short[] data, Dimension2D current, Dimension2D required) {
        float[] requiredArray = new float[required.getWidth() * required.getHeight()];
        int i = 0;
        System.out.println("data length " + data.length);
        for(int y = 0; y < required.getHeight(); y++) {
            for(int x = 0; x < required.getWidth(); x++) {
                if(x < current.getWidth() && y < current.getHeight() && i < data.length) {
                    if (data[i] == 32001) {    
                        requiredArray[i] = 0f;
                    } else {
                        requiredArray[i] = data[i];
                    }
                } else {
                    requiredArray[i] = 0f;
                }
                i++;
            }
        }
        return requiredArray;
    }

}