package uk.co.informaticslab.sandpit.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import uk.co.informaticslab.sandpit.io.Camera3D;
import uk.co.informaticslab.sandpit.utils.DepthMapUtils;
import uk.co.informaticslab.sandpit.utils.HeightMapUtils;

/**
 * Created by tom on 17/07/2015.
 */
public class MyTerrain {

    private final Camera3D camera3D;
    private Geometry geometry;

    public MyTerrain(AssetManager assetManager, BulletAppState bulletAppState, Camera3D camera3D) {

        this.camera3D = camera3D;

        Vector2f depthMapDims = camera3D.getDepthMapDimensions();
        float[] heights = HeightMapUtils.createHeightMapFromDepthMap(DepthMapUtils.getDepthMapFromCamera(camera3D), 4);
        Mesh mesh = MyMeshBuilder.buildMesh((int) depthMapDims.getX() - 1, (int) depthMapDims.getY() - 1, heights);
        this.geometry = new Geometry("terrain", mesh);

        //add the skin to our underlying mesh - se below for some predefined materials :)
        Material material = getSolid(assetManager);
        geometry.setMaterial(material);
        //TODO transform geometry to be the correct orientation (will make gravity work)

        //make the terrain a solid surface
        RigidBodyControl floorPhysics = new RigidBodyControl(0.0f);
        geometry.addControl(floorPhysics);
        bulletAppState.getPhysicsSpace().add(floorPhysics);
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void updateTerrainHeights() {
        Mesh m = geometry.getMesh();
    }

    private static Material getNormals(AssetManager assetManager) {
        return new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");  // create a simple material
    }

    private static Material getSolid(AssetManager assetManager) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");  // create a simple material
        mat.setBoolean("UseMaterialColors",true);
        mat.setColor("Diffuse", ColorRGBA.Green);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 64f);
        return mat;
    }

    private static Material getWireframe(AssetManager assetManager) {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.Green);
        mat.getAdditionalRenderState().setWireframe(true);
        return mat;
    }

}
