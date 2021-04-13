package codesKL280;/* *********************************************************
 * For use by students to work on assignments and project.
 * Permission required material. Contact: xyuan@uwindsor.ca
 **********************************************************/

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jdesktop.j3d.examples.sound.PointSoundBehavior;
import org.jdesktop.j3d.examples.sound.audio.JOALMixer;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.Viewer;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.*;

public class CodeLab7 extends JPanel {

    private static final long serialVersionUID = 1L;

    /* a function to position viewer to 'eye' location */
    private void defineViewer(SimpleUniverse simple_U, Point3d eye) {

        TransformGroup viewTransform = simple_U.getViewingPlatform().getViewPlatformTransform();
        Point3d center = new Point3d(0, 0, 0);               // define the point where the eye looks at
        Vector3d up = new Vector3d(0, 1, 0);                 // define camera's up direction
        Transform3D view_TM = new Transform3D();
        view_TM.lookAt(eye, center, up);
        view_TM.invert();
        viewTransform.setTransform(view_TM);                 // set the TransformGroup of ViewingPlatform
    }

    private Shape3D createAxisGeometry(Color3f yColor, float length) { // method to display x, y, z axis
        int strip[] = {2, 2, 2}; // 2 vertex for 3 different lines
        LineStripArray lineStripArray = new LineStripArray(6, LineStripArray.COLOR_3 | GeometryArray.COORDINATES, strip); // create line strip array

        lineStripArray.setCoordinate(0, new Point3f(0.0f, 0.0f, 0.0f)); // connect origin with x axis
        lineStripArray.setCoordinate(1, new Point3f(length, 0.0f, 0.0f));

        lineStripArray.setCoordinate(2, new Point3f(0.0f, 0.0f, 0.0f)); // connect origin with y axis
        lineStripArray.setCoordinate(3, new Point3f(0.0f, length, 0.0f));

        lineStripArray.setCoordinate(4, new Point3f(0.0f, 0.0f, 0.0f)); // connect origin with z axis
        lineStripArray.setCoordinate(5, new Point3f(0.0f, 0.0f, length));

        lineStripArray.setColor(0, CommonsKL.Green); // set x axis to green
        lineStripArray.setColor(1, CommonsKL.Green);

        lineStripArray.setColor(2, yColor); // set y axis to yColor
        lineStripArray.setColor(3, yColor);

        lineStripArray.setColor(4, CommonsKL.Red); // set z axis to red
        lineStripArray.setColor(5, CommonsKL.Red);

        return new Shape3D(lineStripArray); // return lineStripArray
    }

    private Material setMaterial(Color3f color3f) { // set material function with ambient colour, setting emissive color, diffuse color, specular colour, shininess and lighing enable
        int sh = 128; // set shine to 128
        Material material = new Material(); // create a material
        material.setAmbientColor(new Color3f(0.6f, 0.6f, 0.6f));
        material.setEmissiveColor(new Color3f(0, 0,0));
        material.setDiffuseColor(color3f);
        material.setSpecularColor(new Color3f(1f, 1f, 1f));
        material.setShininess(sh);
        material.setLightingEnable(true);
        return material; // return material
    }

    private Sphere createSphere(float radius, int division, Color3f color3f) { // create half transparent sphere
        Appearance appearance = new Appearance(); // create a new appearance
        appearance.setMaterial(setMaterial(color3f)); // setting material using setMaterial method
        return new Sphere(radius, Sphere.GENERATE_NORMALS, division, appearance);
    }

    private void createLight(BranchGroup bg) { // create light with ambient lighting with point light
        AmbientLight ambientLight = new AmbientLight(true, new Color3f(0.2f, 0.2f, 0.2f)); // lightOn set to true, with Color3f(0.2f, 0.2f, 0.,2f)
        ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(0,0,0), 100));// set influencing bounds with bouding sphere at point3d (0,0,0) with radius 100
        bg.addChild(ambientLight); // add child to branch group with ambient light
        PointLight pointLight = new PointLight(true, CommonsKL.White, new Point3f(2, 2, 2), new Point3f(1, 0, 0));
        pointLight.setInfluencingBounds(new BoundingSphere(new Point3d(0,0,0), 100));
        bg.addChild(pointLight); // add point light to branch group
    }

    /* a function to allow key navigation with the ViewingPlateform */
    private KeyNavigatorBehavior keyNavigation(SimpleUniverse simple_U) {
        ViewingPlatform view_platfm = simple_U.getViewingPlatform();
        TransformGroup view_TG = view_platfm.getViewPlatformTransform();
        KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(view_TG);
        BoundingSphere view_bounds = new BoundingSphere(new Point3d(), 100.0); // Create bounding sphere with radius 100
        keyNavBeh.setSchedulingBounds(view_bounds);
        return keyNavBeh;
    }

    /* a function to build the content branch and attach to 'scene' */
    private void createScene(BranchGroup scene) {

        TransformGroup content_TG = new TransformGroup();    // create a TransformGroup (TG)
        scene.addChild(content_TG);	                         // add TG to the scene BranchGroup
        scene.addChild(createAxisGeometry(CommonsKL.Yellow, 0.5f)); // calling createAxisGeometry method
        createLight(scene);

        float [] distances = {4.0f, 7.5f, 12.0f}; // distances
        Switch switchTarget = new Switch();
        switchTarget.setCapability(Switch.ALLOW_SWITCH_WRITE);
        switchTarget.addChild(createSphere(0.75f, 60, CommonsKL.Green)); // adding spheres with different radius, division, and colours
        switchTarget.addChild(createSphere(0.6f, 45, CommonsKL.Blue));
        switchTarget.addChild(createSphere(0.5f, 30, CommonsKL.Orange));
        switchTarget.addChild(createSphere(0.35f, 15, CommonsKL.Red));
        DistanceLOD distanceLOD = new DistanceLOD(distances, new Point3f()); // create distanceLOD
        distanceLOD.addSwitch(switchTarget); // add switch
        distanceLOD.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0)); // use bounding sphere
        if((switchTarget.numChildren() - 1) != distanceLOD.numDistances()){
            System.out.println("Error");
        }
        scene.addChild(distanceLOD); // add distanceLOD to scene
        scene.addChild(switchTarget);// add switchTarget to scene
    }

    /* a constructor to set up and run the application */
    public CodeLab7() {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas_3D = new Canvas3D(config);
        SimpleUniverse su = new SimpleUniverse(canvas_3D);   // create a SimpleUniverse
        defineViewer(su, new Point3d(1.35, 0.35, 2.0));    // set the viewer's location

        BranchGroup scene = new BranchGroup();
        createScene(scene);                           // add contents to the scene branch
        scene.addChild(keyNavigation(su));                   // allow key navigation

        scene.compile();		                             // optimize the BranchGroup
        su.addBranchGraph(scene);                            // attach the scene to SimpleUniverse

        setLayout(new BorderLayout());
        add("Center", canvas_3D);
        setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Java3D Demo with Sound"); // NOTE: change XY to your initials
        frame.getContentPane().add(new CodeLab7());         // create an instance of the class
        frame.setSize(600, 600);                             // set the size of the JFrame
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}