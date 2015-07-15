package uk.co.informaticslab.sandpit.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.renderer.Camera;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import uk.co.informaticslab.sandpit.domain.DepthMap;
import uk.co.informaticslab.sandpit.domain.Dimension2D;
import uk.co.informaticslab.sandpit.io.Camera3D;
import uk.co.informaticslab.sandpit.utils.DepthMapUtils;
import uk.co.informaticslab.sandpit.utils.HeightMapUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by tom on 29/06/2015.
 */
public class MyTerrainBuilder {

    private static final int TERRAIN_PATCH_SIZE = 65;
    private static final int TERRAIN_QUAD_SIZE = 512;

    public static TerrainQuad buildAsTerrainQuad(AssetManager assetManager, Camera camera, BulletAppState bulletAppState, Camera3D camera3D) {

        DepthMap dm = DepthMapUtils.getDepthMapFromCamera(camera3D);
        TerrainQuad terrain = new TerrainQuad("terrain", TERRAIN_PATCH_SIZE, 513, HeightMapUtils.createHeightMapFromDepthMap(dm, TERRAIN_QUAD_SIZE, TERRAIN_QUAD_SIZE));
        terrain.setMaterial(getTerrainNormalsMaterial(assetManager));

//        terrain.setLocalTranslation(0, 0, 0);
        int terrainScaleDownFactor = 4;
        terrain.setLocalScale(1f, 1f/terrainScaleDownFactor, 1f);
        terrain.setLocked(false);

        TerrainLodControl control = new TerrainLodControl(terrain, camera);
        control.setLodCalculator(new DistanceLodCalculator(65, 2.7f));
        terrain.addControl(control);

        //make terrain solid for collisions to work
        terrain.addControl(new RigidBodyControl(0));
        bulletAppState.getPhysicsSpace().add(terrain);

        return terrain;
    }

    private static Material getTerrainNormalsMaterial(AssetManager assetManager) {
        Material material = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        return material;
    }

    private static Material getTerrainMaterial(AssetManager assetManager, Camera3D camera3D) {
        Material material = new Material(assetManager, "assets/terrain/Terrain.j3md");
        material.setTexture("RawDepthMap", getRawDepthMapTexture(camera3D));

        return material;
    }

    private static Texture2D getRawDepthMapTexture(Camera3D camera3D) {
        return new Texture2D(createImageFromDepthMap(camera3D));
    }

    private static Image createImageFromResizedDepthMap(Camera3D camera3D) {
        short[] resizedData = resizeRawData2(camera3D);
        ByteBuffer buffer = ByteBuffer.allocateDirect(resizedData.length * 3);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.asShortBuffer().put(resizedData);
        return new Image(Image.Format.Luminance16, TERRAIN_QUAD_SIZE, TERRAIN_QUAD_SIZE, buffer);
    }

    private static Image createImageFromDepthMap(Camera3D camera3D) {
        Dimension2D dimensions = camera3D.getDepthMapDimensions();
        short[] data = camera3D.sampleDepthMap();
        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length * 2);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.asShortBuffer().put(data);
        return new Image(Image.Format.Luminance16, dimensions.getWidth(), dimensions.getHeight(), buffer);
    }

    /**
     * Resizes the raw camera output (320 x 240) to required grid size (512 x 512)
     *
     * @param camera3D 3D camera object
     * @return resized data
     */
    private static short[] resizeRawData(Camera3D camera3D) {
        short[] resized = new short[TERRAIN_QUAD_SIZE * TERRAIN_QUAD_SIZE];
        short[] original = camera3D.sampleDepthMap();
        Dimension2D rawDimensions = camera3D.getDepthMapDimensions();
        int ri = 0;
        int oi = 0;
        for (int y = 0; y < rawDimensions.getHeight(); y++) {
            for (int x = 0; x < rawDimensions.getWidth(); x++) {
                resized[ri++] = original[oi++];
            }
            ri += 192;
        }
        return resized;
    }


    /**
     * Possibly more performant!
     * Resizes the raw camera output (320 x 240) to required grid size (512 x 512)
     *
     * @param camera3D 3D camera object
     * @return resized data
     */
    private static short[] resizeRawData2(Camera3D camera3D) {
        short[] resized = new short[TERRAIN_QUAD_SIZE * TERRAIN_QUAD_SIZE];
        short[] original = camera3D.sampleDepthMap();
        int width = camera3D.getDepthMapDimensions().getWidth();
        int ri = 0;
        int i = 0;
        for (short s : original) {
            resized[ri++] = s;
            if (i++ == width - 1) {
                ri += 192;
                i = 0;
            }
        }
        return resized;
    }

}
