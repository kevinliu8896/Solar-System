package codesKL280;

/* *********************************************************
 * For use by students to work on assignments and project.
 * Permission required material. Contact: xyuan@uwindsor.ca
 **********************************************************/

import javax.swing.*;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;
import org.jogamp.java3d.utils.geometry.ColorCube;

import java.awt.*;

public class CodeAssign1 extends JPanel {
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

    private static Shape3D createRectangleGeometry(Color3f color, Point3f size, Vector2f scale) { // method to create a rectangle
        QuadArray quadArray = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.COORDINATES); // initialize a QuadArray with a vertexCount of 4

        quadArray.setCoordinate(0, new Point3f(size.x * scale.x, size.y * scale.y, size.z)); // set coordinate for quad array at size.x * scale.x, size.y * scale.y, size.z
        quadArray.setCoordinate(1, new Point3f(-size.x * scale.x, size.y * scale.y, size.z)); // set coordinate for quad array at -size.x * scale.x, size.y * scale.y, size.z
        quadArray.setCoordinate(2, new Point3f(-size.x * scale.x, -size.y * scale.y, size.z)); // set coordinate for quad array at -size.x * scale.x, -size.y * scale.y, size.z
        quadArray.setCoordinate(3, new Point3f(size.x * scale.x, -size.y * scale.y, size.z)); // set coordinate for quad array at size.x * scale.x, -size.y * scale.y, size.z


        for (int i = 0; i < 4; i++) { // iterate through all indexes of the QuadArray to:
            quadArray.setColor(i, color); // set each coordinates index to the color that was passed in the method call
        }

        return new Shape3D(quadArray); // return quadArray

    }


    private static Shape3D createPyramidGeometry(int n) { // create a parameter called n it represents the number of sides a 2D shape will have
        float r = 0.435f, x, y;                             // vertex at 0.435 away from origin to match scaling
        Point3f coor[] = new Point3f[n + 2]; // make Point3f[n + 2] to represent the amount of sides passed in
        Color3f colorArr[] = {CommonsKL.Yellow, CommonsKL.Green, CommonsKL.Red, CommonsKL.Magenta, CommonsKL.Cyan, CommonsKL.Orange}; // create a color array to iterate through

        LineArray lineArr = new LineArray(n * 2, LineArray.COLOR_3 | LineArray.COORDINATES); // doubling vertex count by using n * 2
        for (int i = 0; i < n; i++) { // iterate through n number of sides
            x = (float) Math.cos(Math.PI / 180 * (90 + 360 / n * i)) * r; // take 360 degrees and divide it by the number passed in by n for x and y
            y = (float) Math.sin(Math.PI / 180 * (90 + 360 / n * i)) * r;
            coor[i] = new Point3f(x, y, 0.25f); // set z to 0.25f for the center of the base
        }

        TriangleArray triArr = new TriangleArray(n * 6, TriangleArray.COLOR_3 | TriangleArray.COORDINATES); // create triangle array

        for (int i = 0; i < n; i++) { // iterate through n number of sides
            triArr.setCoordinate(i * 3, new Point3f(0, 0, 0.5f)); // set z to 0.5 f to create apex of the pyramid
            triArr.setCoordinate(i * 3 + 1, coor[i]); // set a coor[i] at index i * 3 + 1
            triArr.setCoordinate(i * 3 + 2, coor[(i + 1) % n]); // set a coor[(i + 1) % n] at index i * 3 + 2

            triArr.setColor(i * 3, CommonsKL.Blue); // set color of origin to blue
            triArr.setColor(i * 3 + 1, colorArr[i % 6]); // set color of index i * 3 + 1 of colorArr[i % 6]
            triArr.setColor(i * 3 + 2, colorArr[(i + 1) % 6]); // set color of index i * 3 + 2 of colorArr[(i  + 1) % 6]

            triArr.setCoordinate(3 * n + i * 3, new Point3f(0, 0, 0.25f)); // set a coordinate at the origin on x,y,z plane (0, 0, 0.25f)
            triArr.setCoordinate(3 * n + i * 3 + 2, coor[i]); // set a coor[i] at index 3 * n + i * 3 + 2
            triArr.setCoordinate(3 * n + i * 3 + 1, coor[(i + 1) % n]); // set a coor[(i + 1) & n] at index 3 * n * i * 3 + 1
            // set all colours of each triangle to blue
            triArr.setColor(3 * n + i * 3, CommonsKL.Blue); // set color of index 3 * n + i * 3 to blue
            triArr.setColor(3 * n + i * 3 + 1, CommonsKL.Blue); // set color of index 3 * n + i * 3 + 1 to blue
            triArr.setColor(3 * n + i * 3 + 2, CommonsKL.Blue); // set color of index 3 * n + i * 3 + 2 to blue
        }

        triArr.setColor(n * 3 - 1, colorArr[0]); // blend color array in from element 0

        if (n < 3) { // if a number less than 3 is passed in a coloured cube will be generated
            return new ColorCube(0.35); // returning the color cube with scale 0.35
        } else {
            return new Shape3D(triArr); // return triangle array
        }
    }

    public static TransformGroup displayText3D(String txt, double scl, Point3f pnt, Color3f clr) {
        Font my2DFont = new Font("Arial", Font.PLAIN, 1); //setting font: font name, font style, font size
        FontExtrusion myExtrude = new FontExtrusion();
        Font3D font3D = new Font3D(my2DFont, myExtrude);
        Text3D text3D = new Text3D(font3D, txt, pnt);
        ColoringAttributes color = new ColoringAttributes();
        color.setColor(clr); //setting color to clr passed in from parameters
        Appearance app = new Appearance();
        app.setColoringAttributes(color); //adding color for appearance
        Transform3D scaler = new Transform3D();
        scaler.setScale(scl); // set scale to value of scl
        TransformGroup scene_TG = new TransformGroup(scaler);
        scene_TG.addChild(new Shape3D(text3D, app)); // create new scene group
        return scene_TG; //returning the scene_TF
    }

    /* a function to create and return the scene BranchGroup */
    public static BranchGroup createScene() {
        BranchGroup sceneBG = new BranchGroup();             // create 'objsBG' for content
        TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
        sceneBG.addChild(sceneTG);                             // add TG to the scene BranchGroup
        sceneBG.addChild(CommonsKL.rotateBehavior(10000, sceneTG));
        // make sceneTG continuously rotating
        sceneBG.addChild(createAxisGeometry(CommonsKL.Yellow, 0.5f)); // calling createAxisGeometry method
        sceneTG.addChild(createRectangleGeometry(CommonsKL.Grey, new Point3f(1f, 0.8f, -0.25f), new Vector2f(0.6f, 0.6f))); // call createRectangleGeometry method
        sceneTG.addChild(createPyramidGeometry(2)); // call createPyramidGeometry method
        String str = "Java3D"; // set str to desired text to display
        sceneTG.addChild(displayText3D(str, 0.2d, new Point3f(-str.length() / 4f, 0, 0), CommonsKL.White)); // display what str is set to

        sceneBG.compile();                                   // optimize objsBG
        return sceneBG;
    }

    /* the main entrance of the application via 'MyGUI()' of "CommonXY.java" */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CommonsKL.setEye(new Point3d(1.35, 0.35, 2.0));
                new CommonsKL.MyGUI(createScene(), "KL's Assignment 1");
            }
        });
    }
}


