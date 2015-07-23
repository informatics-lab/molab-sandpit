package uk.co.informaticslab.sandpit;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.informaticslab.sandpit.io.impl.Mock3DCamera;
import uk.co.informaticslab.sandpit.terrain.MyTerrain;

public class Application extends SimpleApplication {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    // Physics engine of the app
    private BulletAppState bulletAppState;
    private MyTerrain myTerrain;

    private ParticleEmitter points;

    public static void main(String[] args) {
        Application app = new Application();
        app.start();
    }

    private float gravity = 500f;
    private float radius = 100f;
    private float height = 50f;
    private int particlesPerSec = 80;
    private int weather = 1;

    @Override
    public void simpleInitApp() {

        LOG.debug("Initialising app");

        //set up app
        flyCam.setMoveSpeed(20f);
        viewPort.setBackgroundColor(ColorRGBA.Black);
        settings.setFrameRate(25);

        //set camera to centre of terrain looking directly down
        cam.setLocation(new Vector3f(160, 300, -120));
        cam.lookAt(new Vector3f(160, 0, -120), new Vector3f(0,1,0));

        //set up physics engine in the app
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);

        //add a lightsource
        sun();


        //add my terrain!!
        //change 3DCamera implementation to switch real/mock data
        myTerrain = new MyTerrain(assetManager, bulletAppState, new Mock3DCamera());
        rootNode.attachChild(myTerrain.getGeometry());

//        rain();


        inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "shoot");

    }

    @Override
    public void simpleUpdate(float tpf) {
        LOG.trace("update");
        //TODO: add update code
        // System.out.println("location : " + cam.getLocation() + ", height : " + cam.getHeight() + ", direction : " + cam.getDirection());
    }

    @Override
    public void simpleRender(RenderManager rm) {
        LOG.trace("render");
        //TODO: add render code
    }

    /**
     * Every time the shoot action is triggered, a new cannon ball is produced.
     * The ball is set up to fly from the camera3D position in the camera3D direction.
     */
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("shoot") && !keyPressed) {
                rain();
            }
        }
    };

//    private void rain() {
//        points = new ParticleEmitter(
//                "rainPoints", ParticleMesh.Type.Triangle, particlesPerSec * weather);
//        points.setShape(new EmitterSphereShape(Vector3f.ZERO, radius));
////        points.setLocalTranslation(new Vector3f(0f, height, 0f));
//        points.getParticleInfluencer().setInitialVelocity(new Vector3f(0.0f, -1.0f, 0.0f));
//        points.getParticleInfluencer().setVelocityVariation(0.1f);
//        points.setImagesX(1);
//        points.setImagesY(1);
//        points.setGravity(0, gravity * weather, 0);
//        points.setLowLife(2000);
//        points.setHighLife(3000);
//        points.setStartSize(20f);
//        points.setEndSize(10f);
//        points.setStartColor(new ColorRGBA(0.0f, 0.0f, 1.0f, 0.8f));
//        points.setEndColor(new ColorRGBA(0.8f, 0.8f, 1.0f, 0.6f));
//        points.setFacingVelocity(false);
//        points.setParticlesPerSec(particlesPerSec * weather);
//        points.setRotateSpeed(0.0f);
//        points.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
//        Material mat = new Material(assetManager,
//                "Common/MatDefs/Misc/Particle.j3md");
//        // "raindrop.png" is just "spark.png", rotated by 90 degrees.
//        mat.setTexture(
//                "Texture", assetManager.loadTexture(
//                        "assets/raindrop.png"));
//        points.setMaterial(mat);
//        points.setQueueBucket(RenderQueue.Bucket.Transparent);
//        points.addControl(new RigidBodyControl(1f));
//        bulletAppState.getPhysicsSpace().addAll(points);
//        rootNode.attachChild(points);
//    }

    private void rain() {

        Material stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        stone_mat.setColor("Color", ColorRGBA.Blue);
        /** Create a cannon ball geometry and attach to scene graph. */
        Sphere sphere = new Sphere(32, 32, 1f, true, false);
        sphere.setTextureMode(Sphere.TextureMode.Projected);
        Geometry ball_geo = new Geometry("cannon ball", sphere);
        ball_geo.setMaterial(stone_mat);
        rootNode.attachChild(ball_geo);
        /** Position the cannon ball  */
        ball_geo.setLocalTranslation(cam.getLocation());
        /** Make the ball physcial with a mass > 0.0f */
        RigidBodyControl ball_phy = new RigidBodyControl(1f);
        /** Add physical ball to physics space. */
        ball_geo.addControl(ball_phy);
        bulletAppState.getPhysicsSpace().add(ball_phy);
        /** Accelerate the physcial ball to shoot it. */
        ball_phy.setLinearVelocity(cam.getDirection().mult(50));
    }

    private void sun() {
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f, -.5f, -.5f).normalizeLocal());
        rootNode.addLight(sun);
    }

}