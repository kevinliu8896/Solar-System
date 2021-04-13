package codesKL280;

/* *********************************************************
 * For use by students to work on assignments and project.
 * Permission required material. Contact: xyuan@uwindsor.ca
 **********************************************************/

import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;
import org.jogamp.java3d.utils.geometry.ColorCube;

public class CodeLab1 extends JPanel {
    private static final long serialVersionUID = 1L;

    private static Shape3D lineShape(int n) { // create a parameter called n it represents the number of sides a 2D shape will have
        float r = 0.6f, x, y;                             // vertex at 0.06 away from origin
        Point3f coor[] = new Point3f[n]; // make Point3f[n] to represent the amount of sides passed in

        LineArray lineArr = new LineArray(n * 2, LineArray.COLOR_3 | LineArray.COORDINATES); // doubling vertex count by using n * 2
        for (int i = 0; i < n; i++) { // iterate through n number of sides
            x = (float) Math.cos(Math.PI / 180 * (90 + 360/n * i)) * r; // take 360 degrees and divide it by the number passed in by n for x and y
            y = (float) Math.sin(Math.PI / 180 * (90 + 360/n * i)) * r;
            coor[i] = new Point3f(x, y, 0.0f);
        }

        for (int i = 0; i < n; i++) { // iterate through n number of sides
            lineArr.setCoordinate(i * 2, coor[i]);
            lineArr.setCoordinate(i * 2 + 1, coor[(i + 1) % n]); // change coor[(i+1)] to change coordinate order and % the number of lines of the shape passed into n
            lineArr.setColor(i * 2, CommonsKL.Red);
            lineArr.setColor(i * 2 + 1, CommonsKL.Green);
        }

        if (n < 3) { // if a number less than 3 is passed in a coloured cube will be generated
            return new ColorCube(0.35); // returning the color cube with scale 0.35
        }

        return new Shape3D(lineArr);
    }

    /* a function to create and return the scene BranchGroup */
    public static BranchGroup createScene() {
        BranchGroup sceneBG = new BranchGroup();		     // create 'objsBG' for content
        TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
        sceneBG.addChild(sceneTG);	                         // add TG to the scene BranchGroup
        sceneBG.addChild(CommonsKL.rotateBehavior(10000, sceneTG));
        // make sceneTG continuously rotating
        sceneTG.addChild(lineShape(5));

        sceneBG.compile();                                   // optimize objsBG
        return sceneBG;
    }

    /* the main entrance of the application via 'MyGUI()' of "CommonXY.java" */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CommonsKL.setEye(new Point3d(1.35, 0.35, 2.0));
                new CommonsKL.MyGUI(createScene(), "KL's Lab 1");
            }
        });
    }
}


