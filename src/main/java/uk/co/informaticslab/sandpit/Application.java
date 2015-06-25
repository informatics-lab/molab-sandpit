package uk.co.informaticslab.sandpit;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.renderer.RenderManager;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.texture.Texture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.informaticslab.sandpit.domain.Dimension2D;
import uk.co.informaticslab.sandpit.io.Camera3D;
import uk.co.informaticslab.sandpit.io.impl.Mock3DCamera;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

        Set<Short> uvs = new HashSet<>();
        for (short value : camera.sampleDepthMap()) {
            uvs.add(value);
        }
        System.out.println(uvs);

        Dimension2D terrainDimensions = new Dimension2D(512, 512);

        Material mat_terrain;
        /** 1. Create terrain material and load four textures into it. */
        mat_terrain = new Material(assetManager,
                "Common/MatDefs/Misc/ShowNormals.j3md");
//
//        /** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
//        mat_terrain.setTexture("Alpha", assetManager.loadTexture(
//                "Textures/Terrain/splat/alphamap.png"));
//
//        /** 1.2) Add GRASS texture into the red layer (Tex1). */
//        Texture grass = assetManager.loadTexture(
//                "Textures/Terrain/splat/grass.jpg");
//        grass.setWrap(Texture.WrapMode.Repeat);
//        mat_terrain.setTexture("Tex1", grass);
//        mat_terrain.setFloat("Tex1Scale", 64f);
//
//        /** 1.3) Add DIRT texture into the green layer (Tex2) */
//        Texture dirt = assetManager.loadTexture(
//                "Textures/Terrain/splat/dirt.jpg");
//        dirt.setWrap(Texture.WrapMode.Repeat);
//        mat_terrain.setTexture("Tex2", dirt);
//        mat_terrain.setFloat("Tex2Scale", 32f);
//
//        /** 1.4) Add ROAD texture into the blue layer (Tex3) */
//        Texture rock = assetManager.loadTexture(
//                "Textures/Terrain/splat/road.jpg");
//        rock.setWrap(Texture.WrapMode.Repeat);
//        mat_terrain.setTexture("Tex3", rock);
//        mat_terrain.setFloat("Tex3Scale", 128f);

        terrain = new TerrainQuad("my terrain", 33, 513, createDepthMap(camera));

        terrain.setMaterial(mat_terrain);
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
//                    if (data[i] == 32001) {
//                        requiredArray[i] = 0f;
//                    } else {
//                        requiredArray[i] = data[i];
//                    }
                    requiredArray[i] = getValue(data[i]);
                } else {
                    requiredArray[i] = 0f;
                }
                System.out.println(requiredArray[i]);
                i++;
            }
        }
        return requiredArray;
    }

    private static float getValue(int rawValue) {

        int rgb = rawValue;
        if (rgb == 32001) {
            rgb = 0;
        } else {
            int t1 = rgb - 400;
            double t2 = t1 / 850.0;
            int t3 = (int) (t2 * 255);
            t3 = Math.abs(t3 - 255);
            return t3;
        }
        return rgb;
    }

    private static float[] createDepthMap(Camera3D camera) {

        short[] shorts = camera.sampleDepthMap();

        float[] depthMap = new float[512 * 512];

        int i = 0;
        for (int y = 100; y < 340; y++) {
            for (int x = 100; x < 420; x++) {
                depthMap[(y * 512) + x] = getValue(shorts[i++]);
            }
        }

        return depthMap;
    }
}