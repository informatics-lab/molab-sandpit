package uk.co.informaticslab.sandpit.terrain;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tom on 16/07/2015.
 */
public class MyMeshBuilder {
    
    private static final Logger LOG = LoggerFactory.getLogger(MyMeshBuilder.class);

    /**
     * Constructs the mesh.
     * Number of heights supplied should be (x+1) * (y+1)
     * @param dimensionX size in x direction
     * @param dimensionY size in y direction
     * @param heights array of heights; must be 1 for each vertex
     */
    public static Mesh buildMesh(int dimensionX, int dimensionY, float[] heights) {

        if(heights.length != (dimensionX+1) * (dimensionY+1)) {
            throw new TerrainException("For specified xy:[{},{}] dimensions, number of heights provided must be equal to {}",dimensionX, dimensionY, (dimensionX+1)+(dimensionY+1));
        }

        Vector3f[] vertices = getVertices(dimensionX, dimensionY, heights);
        Vector2f[] texCoords = getTexCoord();
        int[] indices = getIndices(dimensionX, dimensionY);

        Mesh mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoords));
        mesh.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createIntBuffer(indices));
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(getNormals(vertices, indices)));
        mesh.updateBound();

        return mesh;
    }

    /**
     * @param dimensionX num points in the x direction
     * @param dimensionY num points in the z direction
     * @param heights height values at each point in order
     * @return a 3 dimensional vector array of vertices in the order of x,y,z
     */
    public static Vector3f[] getVertices(int dimensionX, int dimensionY, float[] heights) {     
        Vector3f[] vertices = new Vector3f[heights.length];
        int loopCount = 0;
        int heightsIndex = 0;
        for (int y = 0; y <= dimensionY; y++) {
            for (int x = 0; x <= dimensionX; x++) {
                vertices[loopCount++] = new Vector3f(x, y, heights[heightsIndex++]);
            }
        }

        return vertices;
    }

    public static Vector2f[] getTexCoord(){
        Vector2f [] texCoord = new Vector2f[4];
        texCoord[0] = new Vector2f(0,0);
        texCoord[1] = new Vector2f(1,0);
        texCoord[2] = new Vector2f(0,1);
        texCoord[3] = new Vector2f(1,1);
        return texCoord;
    }

    /**
     * see http://wiki.jmonkeyengine.org/doku.php/jme3:advanced:custom_meshes
     * @param dimensionX num points in the x direction
     * @param dimensionY num points in the y direction
     * @return array of ints specifying indices
     */
    public static int[] getIndices(int dimensionX, int dimensionY) {
        int xVerticesAmount = dimensionX + 1;
        int[] indices = new int[dimensionX * dimensionY * 6];
        int arrayIndex = 0;
        for (int y = 0; y < dimensionY; ++y) {                                            //0 0 1 1
            for (int x = 0; x < dimensionX; ++x) {                                        //0 1 0 1
                indices[arrayIndex++] = x + (y * xVerticesAmount);                        //0 1 3 4
                indices[arrayIndex++] = x + (y * xVerticesAmount) + 1;                    //1 2 4 5
                indices[arrayIndex++] = x + (y * xVerticesAmount) + xVerticesAmount;      //3 4 6 7
                indices[arrayIndex++] = x + (y * xVerticesAmount) + 1;                    //1 2
                indices[arrayIndex++] = x + (y * xVerticesAmount) + xVerticesAmount + 1;  //4 5
                indices[arrayIndex++] = x + (y * xVerticesAmount) + xVerticesAmount;      //3 4
            }
        }
        return indices;
    }

    private static float[] getNormals(Vector3f[] vertices, int[] indices) {
        boolean smooth = true;


        Map<Vector3f, Vector3f> normalMap = new HashMap<>(vertices.length);
        for (int i = 0; i < indices.length; i += 3) {
            Vector3f n = FastMath.computeNormal(vertices[indices[i]], vertices[indices[i + 1]], vertices[indices[i + 2]]);
            addNormal(n, normalMap, smooth, vertices[indices[i]], vertices[indices[i + 1]], vertices[indices[i + 2]]);
        }

        //preparing normal list (the order of normals must match the order of vertices)
        float[] normals = new float[vertices.length * 3];
        int arrayIndex = 0;
        for (int i = 0; i < vertices.length; ++i) {
            Vector3f n = normalMap.get(vertices[i]);
            normals[arrayIndex++] = n.x;
            normals[arrayIndex++] = n.y;
            normals[arrayIndex++] = n.z;
        }
        return normals;
    }

    private static void addNormal(Vector3f normalToAdd, Map<Vector3f, Vector3f> normalMap, boolean smooth, Vector3f... vectors) {
        for (Vector3f v : vectors) {
            Vector3f n = normalMap.get(v);
            if (!smooth || n == null) {
                normalMap.put(v, normalToAdd.clone());
            } else {
                n.addLocal(normalToAdd).normalizeLocal();
            }
        }
    }

}
