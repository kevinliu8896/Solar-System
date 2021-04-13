package codesKL280;

/* *********************************************************
 * For use by students to work on assignments and project.
 * Permission required material. Contact: xyuan@uwindsor.ca
 **********************************************************/

import javax.swing.*;

import com.jogamp.opengl.util.texture.TextureCoords;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;
import org.jogamp.java3d.utils.geometry.ColorCube;

import java.awt.*;
import java.sql.SQLOutput;

public class CodeLab6 extends JPanel {
    private static final long serialVersionUID = 1L;

    private static Shape3D createAxisGeometry(Color3f yColor, float length) { // method to display x, y, z axis
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

    public static Material setMaterial(Color3f clr) {
        int SH = 128;               // 10
        Material ma = new Material();
        Color3f c = new Color3f(0.6f * clr.x, 0.6f * clr.y, 0.6f * clr.z);
        ma.setAmbientColor(c);
        ma.setEmissiveColor(new Color3f(0f, 0f, 0f));
        ma.setDiffuseColor(c);
        ma.setSpecularColor(clr);
        ma.setShininess(SH);
        ma.setLightingEnable(true);
        return ma;
    }

    static Material setMaterial() { // set material function with ambient colour, setting emissive color, diffuse color, specular colour, shininess and lighing enable
        int sh = 128; // set shine to 128
        Material material = new Material(); // create a material
        material.setAmbientColor(new Color3f(0.6f, 0.6f, 0.6f));
        material.setEmissiveColor(new Color3f(0, 0,0));
        material.setDiffuseColor(new Color3f(0.6f, 0.6f, 0.6f));
        material.setSpecularColor(new Color3f(1f, 1f, 1f));
        material.setShininess(sh);
        material.setLightingEnable(true);
        return material; // return material
    }

    private static Shape3D createRectangleGeometry(Color3f color, Point3f size, Vector2f scale) { // method to create a rectangle to create base for pavillion
        QuadArray quadArray = new QuadArray(4, QuadArray.NORMALS|QuadArray.COLOR_3 | QuadArray.COORDINATES); // initialize a QuadArray with a vertexCount of 4

        quadArray.setCoordinate(3, new Point3f(size.x * scale.x, size.y * scale.y, size.z)); // set coordinate for quad array at size.x * scale / 2.x, size.y * scale / 2.y, size.z
        quadArray.setCoordinate(2, new Point3f(-size.x * scale.x, size.y * scale.y, size.z)); // set coordinate for quad array at -size.x * scale / 2.x, size.y * scale / 2.y, size.z
        quadArray.setCoordinate(1, new Point3f(-size.x * scale.x, -size.y * scale.y, size.z)); // set coordinate for quad array at -size.x * scale / 2.x, -size.y * scale / 2.y, size.z
        quadArray.setCoordinate(0, new Point3f(size.x * scale.x, -size.y * scale.y, size.z)); // set coordinate for quad array at size.x * scale / 2.x, -size.y * scale / 2.y, size.z


        for (int i = 0; i < 4; i++) { // iterate through all indexes of the QuadArray to:
            quadArray.setColor(i, color); // set each coordinates index to the color that was passed in the method call
            quadArray.setNormal(i, new float[]{0,0,-1}); // set Normal at  index i with a float of {0,0,-1} to invert
        }

        Appearance appearance = new Appearance();// create new appearance
        appearance.setMaterial(setMaterial()); // set material to appearance
        return new Shape3D(quadArray, appearance); // return quadArray
    }

    private static TransformGroup createBase(float scale) { // function to create the base for pavillion
        String sideText[] = {"front", "left", "back", "right", "top", "bottom"}; // side text array
        TransformGroup transformGroupBase = new TransformGroup(); // create a transform group
        Color3f[] color3fs = {CommonsKL.Red, CommonsKL.Yellow, CommonsKL.Cyan, CommonsKL.Green, CommonsKL.Blue, CommonsKL.Magenta}; //color array
        Vector3f[] vector3fs = new Vector3f[4]; // create vector3f array with 4 index
        Vector3f[] vector3fs2 = {new Vector3f(0, 0.4f, 0), new Vector3f(0, -0.4f, 0)}; // initialize vector3fs2

        // declaring variables for x and z in reference to unit circle for creating base
        float x;
        float z;
        // this will make the 4 sides of the base:
        for (int i = 0; i < 4; i++) { // iterate through 4 times to set x and z values
            x = (float) Math.cos(Math.PI / 2 * i) * scale / 2;
            z = (float) Math.sin(Math.PI / 2 * i) * scale / 2;
            vector3fs[i] = new Vector3f(x, 0, z); // set vector3fs at index i's iteration and set it to x, y:-0.5 - (0.08f * scale / 2/2), z
            Transform3D transform3D = new Transform3D(); // create new transform3D

            if (i % 2 == 0) { // if the iteration of i % 2 is equal to 0 rotate the transform3D's y to angle: -Math.cos(Math.PI / 2 * i) * Math.PI / 2
                transform3D.rotY(-Math.cos(Math.PI / 2 * i) * Math.PI / 2);
            }

            if (i == 1) { // if the iteration of i % 2 is equal to 0 rotate the transform3D's y to angle: transform3D.rotY(Math.PI)
                transform3D.rotY(Math.PI);
            }

            transform3D.setTranslation(vector3fs[i]); // set translation of transform3D to vector3fs at index i
            TransformGroup transformGroup = new TransformGroup(); // create a TransformGroup called transformGroup
            transformGroup.setTransform(transform3D); // set transformGroup to transform3D
            transformGroup.addChild(createRectangleGeometry(color3fs[i], new Point3f(1f, 1, 0f), new Vector2f(scale / 2, scale / 2))); // add child to transformGroup using new Shape3D quadArray
            TransformGroup text3d = i != 2 ? displayText3D(sideText[i], 0.1f, new Point3f(0,0,0), CommonsKL.Blue) : displayText3D(sideText[i], 0.1f, new Point3f(0,0,0), CommonsKL.Yellow);
            transformGroup.addChild(text3d);
            transformGroupBase.addChild(transformGroup); // add child to transformGroupBase using transformGroup
        }

        // create the top and bottom of the base:
        for (int j = 0; j < 2; j++) { // iterate 2 times for top and bottom
            Transform3D transform3D = new Transform3D(); // create new Transform3d

            if (j == 0) { // rotating x's angle using angle: -Math.cos(Math.PI * 2) * Math.PI / 2
                transform3D.rotX(-Math.cos(Math.PI * 2) * Math.PI / 2);
            }

            else { // else rotate x angle: Math.cos(Math.PI * 2) * Math.PI / 2
                transform3D.rotX(Math.cos(Math.PI * 2) * Math.PI / 2);
            }

            transform3D.setTranslation(vector3fs2[j]); // set translation of transform3D with vector3fs2 at index j
            TransformGroup transformGroup = new TransformGroup(); // create new transformGroup
            transformGroup.setTransform(transform3D); // set transform of transformGroup to transform 3d
            transformGroup.addChild(createRectangleGeometry(color3fs[j + 4], new Point3f(1, 1, 0), new Vector2f(scale / 2, scale / 2))); // add child to transformGroup using quadArray
            TransformGroup text3d = displayText3D(sideText[j+4], 0.2f, new Point3f(0,0,0), CommonsKL.Blue); // display text 3d from side text array at index j + 4
            transformGroup.addChild(text3d);
            transformGroupBase.addChild(transformGroup); // add child to transformGroupBase
        }

        return transformGroupBase; // return transformGroupBase
    }

    private static void createLight(BranchGroup bg) { // create light with ambient lighting with point light
        AmbientLight ambientLight = new AmbientLight(true, new Color3f(0.2f, 0.2f, 0.2f)); // lightOn set to true, with Color3f(0.2f, 0.2f, 0.,2f)
        ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(0,0,0), 100));// set influencing bounds with bouding sphere at point3d (0,0,0) with radius 100
        bg.addChild(ambientLight); // add child to branch group with ambient light
        PointLight pointLight = new PointLight(true, CommonsKL.White, new Point3f(2, 2, 2), new Point3f(1, 0, 0));
        pointLight.setInfluencingBounds(new BoundingSphere(new Point3d(0,0,0), 100));
        bg.addChild(pointLight); // add point light to branch group
    }

    public static TransformGroup displayText3D(String txt, double scl, Point3f pnt, Color3f clr) {
        Font my2DFont = new Font("Arial", Font.PLAIN, 1); //setting font: font name, font style, font size
        FontExtrusion myExtrude = new FontExtrusion();
        Font3D font3D = new Font3D(my2DFont, myExtrude);
        Text3D text3D = new Text3D(font3D, txt, pnt);
        text3D.setAlignment(Text3D.ALIGN_CENTER);
        ColoringAttributes color = new ColoringAttributes();
        color.setColor(clr); //setting color to clr passed in from parameters
        Appearance app = new Appearance();
        app.setColoringAttributes(color); //adding color for appearance
        Transform3D scaler = new Transform3D();
        scaler.rotY(Math.PI); // rotate text by 180 degrees
        scaler.setScale(scl); // set scale to value of scl
        TransformGroup scene_TG = new TransformGroup(scaler);
        scene_TG.addChild(new Shape3D(text3D, app)); // create new scene group
        return scene_TG; //returning the scene_TF
    }


    /* a function to create and return the scene BranchGroup */
    public static BranchGroup createScene() {
        BranchGroup sceneBG = new BranchGroup();             // create 'objsBG' for content
        TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
        sceneBG.addChild(sceneTG);                           // add TG to the scene BranchGroup
        sceneBG.addChild(CommonsKL.rotateBehavior(10000, sceneTG));
        // make sceneTG continuously rotating
        sceneBG.addChild(createAxisGeometry(CommonsKL.Yellow, 0.5f)); // calling createAxisGeometry method
        createLight(sceneBG);
        sceneTG.addChild(createBase(0.8f));

        sceneBG.compile(); // optimize objsBG
        return sceneBG;
    }

    /* the main entrance of the application via 'MyGUI()' of "CommonXY.java" */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CommonsKL.setEye(new Point3d(0.05, 0.35, 2.5));
                new CommonsKL.MyGUI(createScene(), "KL's Lab 6");
            }
        });
    }
}


