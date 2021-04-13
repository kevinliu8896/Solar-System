package codesKL280;

/* *********************************************************
 * For use by students to work on assignments and project.
 * Permission required material. Contact: xyuan@uwindsor.ca
 **********************************************************/

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

import codesKL280.SoundUtilityJOAL;

public class CodeAssign5b extends JPanel implements MouseListener {
    private static final long serialVersionUID = 1L;
    private static JFrame frame;
    private int sound; // make a private int sound variable for pick tool

    private Canvas3D canvas;
    private static SoundUtilityJOAL soundJOAL;               // needed for sound
    private static PickTool pickTool;

    /* a function to make a continuously rotating TransformGroup with Alpha being 'rotAlpha' */
    public static RotationInterpolator rotateBehavior(TransformGroup rotTG, Alpha rotAlpha) {
        rotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D yAxis = new Transform3D();                        // y-axis is the default
        RotationInterpolator rot_beh = new RotationInterpolator(
                rotAlpha, rotTG, yAxis, 0.0f, (float) Math.PI * 2.0f);  // 360 degrees of rotation
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        rot_beh.setSchedulingBounds(bounds);
        return rot_beh;
    }

    /* a function to create and return the scene BranchGroup */
    public static BranchGroup createScene() {
        BranchGroup sceneBG = new BranchGroup();
        sceneBG.addChild(texturedBackground());
        cloneObjects(sceneBG);
        pickTool = new PickTool(sceneBG);                   // allow picking of objs in 'sceneBG'
        pickTool.setMode(PickTool.BOUNDS);

        addLights(sceneBG, CommonsKL.White);
        return sceneBG;
    }

    private static Background texturedBackground() {
        String filename = "background.jpg";
        TextureLoader loader = new TextureLoader(filename, null); // load file name
        ImageComponent2D image = loader.getImage(); // loader gets image
        Background backTex = new Background();
        backTex.setImage(image);
        backTex.setImageScaleMode(Background.SCALE_FIT_MAX);
        backTex.setApplicationBounds(new BoundingSphere(new Point3d(0, 0, 0), 100));
        return backTex;
    }

    private static Texture setTexture(String name) {// set texture with filename + .jpg
        String filename = name + ".jpg";
        TextureLoader loader = new TextureLoader(filename, null); // load texture
        ImageComponent2D imageComponent2D = loader.getImage(); //loader gets image
        if (imageComponent2D == null) { // if image not available
            System.out.println("Load failed for the desired texture " + filename);
        }
        Texture2D texture2D = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, imageComponent2D.getWidth(), imageComponent2D.getHeight());
        texture2D.setImage(0, imageComponent2D);
        return texture2D;
    }

    private static Appearance texturedApp(String name) { // function to render Marble Texture
        Appearance appearance = new Appearance(); // create appearance
        appearance.setTexture(setTexture(name)); // setTexture of name

        PolygonAttributes polyAttrib = new PolygonAttributes(); // create polygon attributes
        polyAttrib.setCullFace(PolygonAttributes.CULL_NONE); // set cull face to cull_none
        appearance.setPolygonAttributes(polyAttrib); // set appearance using polygon attributes

        TextureAttributes ta = new TextureAttributes(); // create texture attributes
        ta.setTextureMode(TextureAttributes.REPLACE); // set texture mode
        appearance.setTextureAttributes(ta); // set textrue attributes
        // rotations
        int angle = 0;
        Vector3d scale = new Vector3d(1f, 1f, 1f);
        Transform3D transMap = new Transform3D();
        transMap.rotZ((angle / 90.0f) * Math.PI / 2);
        transMap.setScale(scale);
        ta.setTextureTransform(transMap);

        return appearance;
    }

    private static Sphere nonTransparentSphere(float radius) { // create half transparent sphere
        Sphere sphere = new Sphere(radius, Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS | Sphere.ENABLE_APPEARANCE_MODIFY, 80, texturedApp("MarbleTexture"));
        sphere.setUserData(0); // set userData to 0
        // make it so that the spheres are non transparent
        return sphere;
    }

    private static void cloneObjects(BranchGroup branchGroup) {
        Vector3f[] pos = {new Vector3f(0f, 0f, 0f), new Vector3f(1.05f, 0f, 1.05f), new Vector3f(0f, 1.05f, 1.05f)}; // create new vectors for position x, y, z

        TransformGroup transformGroupL = new TransformGroup(); // transform group for links
        TransformGroup transformGroupS = new TransformGroup(); // transform group for reference frame
        transformGroupL.addChild(nonTransparentSphere(1)); // add sphere to child
        Transform3D scalar = new Transform3D(); // create a scalar of Transform3d
        scalar.setScale(1.1); // set scalar value to 1.1
        scalar.setTranslation(pos[0]);
        transformGroupS.setTransform(scalar); // set frame to 1.1
        transformGroupS.addChild(transformGroupL); // add frame to link
        branchGroup.addChild(transformGroupS); // add child(reference frame) to branch group
        branchGroup.addChild(CommonsKL.rotateBehavior(10000, transformGroupL)); // set rotation behavior 10000

        TransformGroup transformGroup2L = new TransformGroup();// second transform group link
        TransformGroup transformGroup2S = new TransformGroup(); // transform group for reference frame
        Transform3D scalar2 = new Transform3D(); //create second scalar
        scalar2.setScale(0.5); // set scale to 0.5
        scalar2.setTranslation(pos[1]); // set translation of scalar2 to pos index 1
        transformGroup2S.setTransform(scalar2); // set reference frame #2 to 0.5
        transformGroup2L.addChild(nonTransparentSphere(1)); // add child to new Link sg

        transformGroup2S.addChild(transformGroup2L); // adding link tg to reference frame
        transformGroupL.addChild(transformGroup2S); // add child to link #2 with reference frame #2

        branchGroup.addChild(CommonsKL.rotateBehaviorZ(5000, transformGroup2L)); // add child with rotation behaviour 5000


        TransformGroup transformGroup3L = new TransformGroup(); // create transform group link #3
        TransformGroup transformGroup3S = new TransformGroup(); // transform group for reference frame $3
        Transform3D scalar3 = new Transform3D(); // create 3rd scalar
        scalar3.setScale(0.35); // set scale t 0.35
        scalar3.setTranslation(pos[2]);// set translation of scalar3 to pos index 2
        transformGroup3S.setTransform(scalar3);
        transformGroup3L.addChild(nonTransparentSphere(1));

        transformGroup3S.addChild(transformGroup3L); // adding link tg to reference frame
        transformGroup2L.addChild(transformGroup3S);
        branchGroup.addChild(CommonsKL.rotateBehaviorX(2500, transformGroup3L));

        transformGroupL.getChild(0).setName("sun"); // get child at index 0 with the names of each transformGroupL

        transformGroup2L.getChild(0).setName("earth");

        transformGroup3L.getChild(0).setName("moon");
    }

    /* a constructor to set up and run the application */
    public CodeAssign5b(BranchGroup sceneBG) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);
        canvas.addMouseListener(this);                        // NOTE: enable mouse clicking

        SimpleUniverse su = new SimpleUniverse(canvas);       // create a SimpleUniverse
        CommonsKL.setEye(new Point3d(2, 2, 6.0));
        CommonsKL.defineViewer(su);                           // set the viewer's location

        sceneBG.compile();
        su.addBranchGraph(sceneBG);                           // attach the scene to SimpleUniverse

        setLayout(new BorderLayout());
        add("Center", canvas);
        frame.setSize(600, 600);                              // set the size of the JFrame
        frame.setVisible(true);
    }

    public CodeAssign5b(Canvas3D canvas3D, int k) {
        this.sound = k;
        canvas = canvas3D;
    }

    public static void main(String[] args) {
        frame = new JFrame("KL's Assignment 4");
        frame.getContentPane().add(new CodeAssign5b(createScene()));
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();           // mouse coordinates
        Point3d point3d = new Point3d(), center = new Point3d();
        canvas.getPixelLocationInImagePlate(x, y, point3d);   // obtain AWT pixel in ImagePlate coordinates
        canvas.getCenterEyeInImagePlate(center);              // obtain eye's position in IP coordinates

        Transform3D transform3D = new Transform3D();          // matrix to relate ImagePlate coordinates~
        canvas.getImagePlateToVworld(transform3D);            // to Virtual World coordinates
        transform3D.transform(point3d);                       // transform 'point3d' with 'transform3D'
        transform3D.transform(center);                        // transform 'center' with 'transform3D'

        Vector3d mouseVec = new Vector3d();
        mouseVec.sub(point3d, center);
        mouseVec.normalize();
        pickTool.setShapeRay(point3d, mouseVec);              // send a PickRay for intersection

        if (pickTool.pickClosest() != null) {
            PickResult pickResult = pickTool.pickClosest();   // obtain the closest hit
            Sphere sphere = (Sphere) pickResult.getNode(PickResult.PRIMITIVE);
            if (sphere != null) {
                // get rid of error where sphere is null. the error is: Cannot invoke "org.jogamp.java3d.utils.geometry.Sphere.getUserData()" because "sphere" is null
                Appearance app = new Appearance();                // originally a PRIMITIVE as a box
                if ((int) sphere.getUserData() == 0) {               // retrieve 'UserData'
                    app.setTexture(setTexture(sphere.getName()));
                    sphere.setAppearance(app);
                    CodeAssign5b.playSound(sound); // play variable "sound" if user clicks sphere
                    sphere.setUserData(1);                           // set 'UserData' to a new value
                } else {
                    app.setTexture(setTexture("MarbleTexture"));
                    sphere.setAppearance(app);
                    CodeAssign5b.playSound(sound); // play variable "sound" if user clicks sphere
                    sphere.setUserData(0);                           // reset 'UserData'
                }
            }
        }
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    /* for A5: a function to initialize for playing sound */
    public static void initialSound() {
        soundJOAL = new SoundUtilityJOAL();
        if (!soundJOAL.load("laser2", 0f, 0f, 10f, true))
            System.out.println("Could not load " + "laser2");
        if (!soundJOAL.load("magic_bells", 0f, 0f, 10f, true))
            System.out.println("Could not load " + "magic_bells");
    }

    /* a function to play different sound according to key (user) */
    public static void playSound(int key) {
        String snd_pt = "laser2";
        if (key > 1)
            snd_pt = "magic_bells";
        soundJOAL.play(snd_pt);
        try {
            Thread.sleep(500); // sleep for 0.5 secs
        } catch (InterruptedException ex) {
        }
        soundJOAL.stop(snd_pt);
    }

    /* a function to add ambient light and a point light to 'sceneBG' */
    public static void addLights(BranchGroup sceneBG, Color3f clr) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        AmbientLight amLgt = new AmbientLight(new Color3f(0.2f, 0.2f, 0.2f));
        amLgt.setInfluencingBounds(bounds);
        sceneBG.addChild(amLgt);
        Point3f pt = new Point3f(2.0f, 2.0f, 2.0f);
        Point3f atn = new Point3f(1.0f, 0.0f, 0.0f);
        PointLight ptLight = new PointLight(clr, pt, atn);
        ptLight.setInfluencingBounds(bounds);
        sceneBG.addChild(ptLight);
    }

    /* a function to create and return material definition */
    public static Material setMaterial(Color3f clr) {
        int SH = 128;               // 10
        Material ma = new Material();
        Color3f c = new Color3f(0.6f * clr.x, 0.6f * clr.y, 0.6f * clr.z);
        ma.setAmbientColor(c);
        ma.setEmissiveColor(CommonsKL.None);
        ma.setDiffuseColor(c);
        ma.setSpecularColor(clr);
        ma.setShininess(SH);
        ma.setLightingEnable(true);
        return ma;
    }
}
