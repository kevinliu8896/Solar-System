package codesKL280;

/* *********************************************************
 * For use by students to work on assignments and project.
 * Permission required material. Contact: xyuan@uwindsor.ca
 **********************************************************/

import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;
import org.jogamp.java3d.utils.geometry.ColorCube;

import java.awt.*;

public class CodeLab2 extends JPanel {
    private static final long serialVersionUID = 1L;

    private static Shape3D triangleShape(int n) { // create a parameter called n it represents the number of sides a 2D shape will have
        float r = 0.6f, x, y;                             // vertex at 0.06 away from origin
        Point3f coor[] = new Point3f[n + 2]; // make Point3f[n + 2] to represent the amount of sides passed in
        Color3f colorArr[] = {CommonsKL.Green, CommonsKL.Red, CommonsKL.Yellow, CommonsKL.Cyan, CommonsKL.Orange, CommonsKL.Magenta}; // create a color array to iterate through

        LineArray lineArr = new LineArray(n * 2, LineArray.COLOR_3 | LineArray.COORDINATES); // doubling vertex count by using n * 2
        for (int i = 0; i < n; i++) { // iterate through n number of sides
            x = (float) Math.cos(Math.PI / 180 * (90 + 360 / n * i)) * r; // take 360 degrees and divide it by the number passed in by n for x and y
            y = (float) Math.sin(Math.PI / 180 * (90 + 360 / n * i)) * r;
            coor[i] = new Point3f(x, y, 0.0f);
        }

//        for (int i = 0; i < n; i++) { // iterate through n number of sides
//            lineArr.setCoordinate(i * 2, coor[i]);
//            lineArr.setCoordinate(i * 2 + 1, coor[(i + 1) % n]); // change coor[(i+1)] to change coordinate order and % the number of lines of the shape passed into n
//            lineArr.setColor(i * 2, CommonsKL.Red);
//            lineArr.setColor(i * 2 + 1, CommonsKL.Green);
//        }

        TriangleArray triArr = new TriangleArray(n * 3, TriangleArray.COLOR_3 | TriangleArray.COORDINATES); // create triangle array

        for (int i = 0; i < n; i++) { // iterate through n number of sides
            triArr.setCoordinate(i * 3, new Point3f(0, 0, 0)); // set a coordinate at the origin on x,y,z plane (0,0,0)
            triArr.setCoordinate(i * 3 + 1, coor[i]); // set a coor[i] at index i * 3 + 1
            triArr.setCoordinate(i * 3 + 2, coor[(i + 1) % n]); // set a coor[(i + 1) & n] at index i * 3 + 2
            triArr.setColor(i * 3, CommonsKL.Blue); // set color of origin to blue
            triArr.setColor(i * 3 + 1, colorArr[i % 6]); // set color of index i * 3 + 1 of colorArr[i % 6]
            triArr.setColor(i * 3 + 2, colorArr[(i + 1) % 6]); // set color of index i * 3 + 2 of colorArr[(i  + 1) % 6]


        }
        triArr.setColor(n * 3 - 1, CommonsKL.Green);

        if (n < 3) { // if a number less than 3 is passed in a coloured cube will be generated
            return new ColorCube(0.35); // returning the color cube with scale 0.35
        }

        return new Shape3D(triArr); // return triangle array
    }

    public static TransformGroup letters3D(String textString) {
        Font my2DFont = new Font("Arial", Font.PLAIN, 1); //setting font: font name, font style, font size
        FontExtrusion myExtrude = new FontExtrusion();
        Font3D font3D = new Font3D(my2DFont, myExtrude);
        Text3D text3D = new Text3D(font3D, textString, new Point3f(0.0f, 0.0f, 0.0f), Text3D.ALIGN_CENTER, Text3D.PATH_RIGHT);
        ColoringAttributes color = new ColoringAttributes();
        color.setColor(new Color3f(0.0f, 1.0f, 0.0f)); //setting color
        Appearance app = new Appearance();
        app.setColoringAttributes(color); //adding color for appearance
        Transform3D scaler = new Transform3D();
        scaler.setScale(0.15); // scaling 0.15 to fit text into polygon
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
        int n = 3; // test value to pass in number of sides of a polygon
        sceneTG.addChild(triangleShape(n)); // create polygon with n sides
        sceneTG.addChild((letters3D("Polygon" + n))); // add text displaying PolygonN (N is the number of polygon sides passed in)

        sceneBG.compile();                                   // optimize objsBG
        return sceneBG;
    }

    /* the main entrance of the application via 'MyGUI()' of "CommonXY.java" */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CommonsKL.setEye(new Point3d(1.35, 0.35, 2.0));
                new CommonsKL.MyGUI(createScene(), "KL's Lab 2");
            }
        });
    }
}


