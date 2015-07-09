package uk.co.informaticslab.sandpit.utils;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.terrain.heightmap.HeightMap;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import uk.co.informaticslab.sandpit.domain.DepthMap;
import uk.co.informaticslab.sandpit.io.Camera3D;
import uk.co.informaticslab.sandpit.io.impl.Senz3DCamera;

/**
 * Created by tom on 26/06/2015.
 */
public class DepthMapUtils {

    public static DepthMap getDepthMapFromCamera(Camera3D camera) {
        return new DepthMap(camera.getDepthMapDimensions(), camera.sampleDepthMap(), Senz3DCamera.UNTRUSTED_VALUE);
    }

//    public static Image magic(Camera3D camera3D, AssetManager assetManager) {
//        short[] sample = camera3D.sampleDepthMap();
//
//        Texture rawDepthMapTexture = new Texture2D(new Image());
//
//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.setTexture("texture", );
//
//
//    }

}
