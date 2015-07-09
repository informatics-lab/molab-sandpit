package uk.co.informaticslab.sandpit.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.terrain.ProgressMonitor;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.MultiTerrainLodControl;
import com.jme3.terrain.geomipmap.NeighbourFinder;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.texture.Texture;

import java.util.List;

/**
 *     ___________
 *     | 1  | 2  |
 *     |____|____|
 *
 * Tiles 2 {@link TerrainQuad} objects into the required terrain map (320 x 240).
 */
public class MyTiledTerrain extends Node implements Terrain, NeighbourFinder {

    private static final int TERRAIN_QUAD_SIZE = 257;
    private static final int TERRAIN_PATCH_SIZE = 65;

    private TerrainQuad terrain1;
    private TerrainQuad terrain2;

    private Material material;

    private final Camera cam;
    private final AssetManager assetManager;
    private final BulletAppState bulletAppState;

    public MyTiledTerrain(AssetManager assetManager, Camera cam, BulletAppState bulletAppState) {
        this.assetManager = assetManager;
        this.cam = cam;
        this.bulletAppState = bulletAppState;

        initTerrainTexture();
        initTerrainTopology();

        terrain1.setNeighbourFinder(this);
        terrain2.setNeighbourFinder(this);

        MultiTerrainLodControl lodControl = new MultiTerrainLodControl(cam);
        lodControl.setLodCalculator(new DistanceLodCalculator(65, 2.7f)); // patch size, and a multiplier
        lodControl.addTerrain(terrain1);
        lodControl.addTerrain(terrain2);
        this.addControl(lodControl);

        /*
         * Create PhysicsRigidBodyControl for collision
         */
        this.addControl(new RigidBodyControl(0));
        bulletAppState.getPhysicsSpace().add(this);
    }

    private void initTerrainTexture() {
        // TERRAIN TEXTURE material
        material = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        material.setBoolean("useTriPlanarMapping", true);
        material.setBoolean("WardIso", true);
        material.setFloat("Shininess", 0);

        // GRASS texture
        Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("DiffuseMap", grass);
        material.setFloat("DiffuseMap_0_scale", 64);

    }

    private void initTerrainTopology() {
        terrain1 = new TerrainQuad("terrain 1", TERRAIN_PATCH_SIZE, TERRAIN_QUAD_SIZE, null);
        terrain1.setMaterial(material);
        terrain1.setLocalTranslation(-128, -100, 0);
        terrain1.setLocalScale(1f, 1f, 1f);
        terrain1.setLocked(false);
        this.attachChild(terrain1);

        terrain2 = new TerrainQuad("terrain 2", TERRAIN_PATCH_SIZE, TERRAIN_QUAD_SIZE, null);
        terrain2.setMaterial(material);
        terrain2.setLocalTranslation(128, -100, 0);
        terrain2.setLocalScale(1f, 1f, 1f);
        terrain2.setLocked(false);
        this.attachChild(terrain2);
    }



    @Override
    public TerrainQuad getRightQuad(TerrainQuad center) {
        if (center == terrain1) {
            return terrain2;
        } else {
            return null;
        }
    }

    @Override
    public TerrainQuad getLeftQuad(TerrainQuad center) {
        if (center == terrain2) {
            return terrain1;
        } else {
            return null;
        }
    }

    /*
     * not required for current tile topology
     */
    @Override
    public TerrainQuad getTopQuad(TerrainQuad center) {
        return null;
    }

    /*
     * not required for current tile topology
     */
    @Override
    public TerrainQuad getDownQuad(TerrainQuad center) {
        return null;
    }

    @Override
    public float getHeight(Vector2f xz) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector3f getNormal(Vector2f xz) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getHeightmapHeight(Vector2f xz) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setHeight(Vector2f xzCoordinate, float height) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setHeight(List<Vector2f> xz, List<Float> height) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void adjustHeight(Vector2f xzCoordinate, float delta) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void adjustHeight(List<Vector2f> xz, List<Float> height) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float[] getHeightMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getMaxLod() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLocked(boolean locked) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void generateEntropy(ProgressMonitor monitor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Material getMaterial(Vector3f worldLocation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getTerrainSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getNumMajorSubdivisions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
