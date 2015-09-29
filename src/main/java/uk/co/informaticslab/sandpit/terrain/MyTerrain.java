package uk.co.informaticslab.sandpit.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import uk.co.informaticslab.sandpit.domain.DepthMap;
import uk.co.informaticslab.sandpit.domain.DepthMapCalibration;
import uk.co.informaticslab.sandpit.io.Camera3D;
import uk.co.informaticslab.sandpit.utils.DepthMapUtils;
import uk.co.informaticslab.sandpit.utils.HeightMapUtils;

/**
 * Created by tom on 17/07/2015.
 */
public class MyTerrain {

    private final Camera3D camera3D;
    private Geometry geometry;
    private float avgTerrainHeight;
    private DepthMapCalibration depthMapCalibration;

    public MyTerrain(AssetManager assetManager, BulletAppState bulletAppState, Camera3D camera3D) {

        this.camera3D = camera3D;

        Vector2f depthMapDims = camera3D.getDepthMapDimensions();
        float[] heights = HeightMapUtils.createHeightMapFromDepthMap(DepthMapUtils.getDepthMapFromCamera(camera3D), 4);
        avgTerrainHeight = HeightMapUtils.avgFloatArray(heights);
        Mesh mesh = MyMeshBuilder.buildMesh((int) depthMapDims.getX() - 1, (int) depthMapDims.getY() - 1, heights);
        this.geometry = new Geometry("terrain", mesh);

        //add the skin to our underlying mesh - se below for some predefined materials :)
        Material material = getCustom(assetManager);
        geometry.setMaterial(material);

        //transform the terrain so that the y axis is up/down
        Quaternion tip270 = new Quaternion();
        tip270.fromAngleAxis(FastMath.PI * 3 / 2, new Vector3f(1, 0, 0));
        geometry.setLocalRotation(tip270);

        //make the terrain a solid surface
        RigidBodyControl floorPhysics = new RigidBodyControl(0.0f);
        geometry.addControl(floorPhysics);
        bulletAppState.getPhysicsSpace().add(floorPhysics);
    }

    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * Gets the current mesh and updates the vertices based on the currently sampled depth map.
     */
    public void updateTerrainHeights() {
        Mesh m = geometry.getMesh();
        float[] heights;
        Vector2f depthMapDims;
        if(depthMapCalibration == null) {
            heights = HeightMapUtils.createHeightMapFromDepthMap(DepthMapUtils.getDepthMapFromCamera(camera3D), 4);
            depthMapDims = camera3D.getDepthMapDimensions();
        } else {
            heights = HeightMapUtils.createHeightMapFromDepthMap(DepthMapUtils.getDepthMapFromCamera(camera3D), 4, depthMapCalibration);
            depthMapDims = depthMapCalibration.getDepthMapDimensions();
        }
        float avg = HeightMapUtils.avgFloatArray(heights);
        if(Math.abs(avgTerrainHeight-avg)<(avgTerrainHeight/10)) {
            Vector3f[] vertices = MyMeshBuilder.getVertices((int) depthMapDims.getX() - 1, (int) depthMapDims.getY() - 1, heights);
            m.getBuffer(VertexBuffer.Type.Position).updateData(BufferUtils.createFloatBuffer(vertices));
        }
    }

    public void calibrateDepthMap() {
        DepthMap dm = DepthMapUtils.getDepthMapFromCamera(camera3D);
        DepthMapCalibration dmc = new DepthMapCalibration(dm.getMin(),dm.getMax(),dm.getDimensions());
        this.depthMapCalibration = dmc;
    }

    private static Material getNormals(AssetManager assetManager) {
        return new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");  // create a simple material
    }

    private static Material getCustom(AssetManager assetManager) {
        Material mat = new Material(assetManager, "assets/terrain/TerrainGraphics.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("assets/terrain-color-map.png"));

        //tells it not to bother rendering the back of the faces
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Back);
        return mat;
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
