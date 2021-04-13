package codesKL280;/* *********************************************************
 * For use by students to work on lab quiz.
 * Permission required material. Contact: xyuan@uwindsor.ca
 **********************************************************/

import javax.swing.JPanel;
import javax.swing.tree.TreeNode;

import codesKL280.Commons;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;
import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;

public class CodeQuiz extends JPanel {
    private static final long serialVersionUID = 1L;
    private static int n = 4;                                // set number of corner points
    private static float pt = 0.6f;                          // set corner points
    private static Point3f[] pts = {new Point3f(pt, 0f, 0f), new Point3f(0f, 0f, -pt),
            new Point3f(-pt, 0f, 0f), new Point3f(0f, 0f, pt)};

    /* a function to create an n-side polygonal shape in specific size, color, and line style */
    private static Shape3D lineShape(Color3f c, int w, int p, boolean g) {
        int[] strip = {n + 1};                               // add one point to close the polygon
        IndexedLineStripArray ilsa = new IndexedLineStripArray(n + 1,
                GeometryArray.COLOR_3 | GeometryArray.COORDINATES, n + 1, strip);

        int[] pntIndices = new int[n];                       // the strip has 'n' indices
        Color3f[] color = {c};                               // use only one color
        int[] clrIndices = new int[n];                       // one strip has 'n' indices
        for (int i = 0; i < n; i++) {
            pntIndices[i] = i;                               // set point index
            clrIndices[i] = 0;                               // fix color index
        }

        ilsa.setColors(0, color);                            // set color for points
        ilsa.setColorIndices(0, clrIndices);
        ilsa.setCoordinates(0, pts);                         // link points with indices
        ilsa.setCoordinateIndices(0, pntIndices);

        Appearance app = new Appearance();
        LineAttributes lineAtt = new LineAttributes(w, p, g);
        app.setLineAttributes(lineAtt);

        return new Shape3D(ilsa, app);                       // return the Shape3D
    }

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

    private static Shape3D createSquareGeometry(Color3f color) { // method to create a rectangle
        QuadArray quadArray = new QuadArray(2 * 4, QuadArray.COLOR_3 | QuadArray.COORDINATES); // initialize a QuadArray with a vertexCount of 4

        quadArray.setCoordinate(0, new Point3f(pt, 0f, 0f)); // set coordinate for quad array index 0
        quadArray.setCoordinate(1, new Point3f(0f, 0f, -pt)); // set coordinate for quad array at index 1
        quadArray.setCoordinate(2, new Point3f(-pt, 0f, 0f)); // set coordinate for quad array at index 2
        quadArray.setCoordinate(3, new Point3f(0f, 0f, pt)); // set coordinate for quad array at index 3

        // this code will make it show up on the bottom side

        for (int i = 0; i < n; i++) {
            quadArray.setCoordinate(2 * n - 1 - i, pts[i]);
            quadArray.setColor(n + i, color);
        }

        for (int i = 0; i < 4; i++) { // iterate through all indexes of the QuadArray to:
            quadArray.setColor(i, color); // set each coordinates index to the color that was passed in the method call
        }
        Appearance app = new Appearance(); // create a new appearance called app
        TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.SCREEN_DOOR, 0.5f); // set transparency attributes to screen door mode while being half transparent with 0.5f
        app.setTransparencyAttributes(ta); // set transparency attribute to ta

        return new Shape3D(quadArray, app); // return quadArray with appearance

    }

    // ROTATIONS FOR Z AXIS
    public static RotationInterpolator rotateBehaviorZ(int r_num, TransformGroup my_TG) {
        my_TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D yAxis = new Transform3D();
        yAxis.rotZ(-Math.PI / 2);
        yAxis.rotX(-Math.PI / 2);
        Alpha rotationAlpha = new Alpha(-1, r_num);
        RotationInterpolator rot_beh = new RotationInterpolator(
                rotationAlpha, my_TG, yAxis, 0.0f, (float) Math.PI * 2.0f);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        rot_beh.setSchedulingBounds(bounds);
        return rot_beh;
    }

    /* a function to create and return the scene BranchGroup */
    public static BranchGroup createScene() {
        BranchGroup sceneBG = new BranchGroup();             // create 'scene' as content branch
        TransformGroup sceneTG = new TransformGroup();
        TransformGroup sceneTGROT = new TransformGroup();
        sceneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        sceneBG.addChild(sceneTG);
        sceneBG.addChild(createAxisGeometry(Commons.Yellow, 0.6f)); // call axis

        Alpha alpha = new Alpha(-1, Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE, 0, 0, 5000, 3000, 0, 5000, 3000, 0);
        Transform3D axisPosition = new Transform3D();
        axisPosition.rotZ(Math.PI / 2.0);
        PositionInterpolator positionInterpolator = new PositionInterpolator(alpha, sceneTG, axisPosition, -0.5f, 0.5f); // define start and end position for translations
        positionInterpolator.setSchedulingBounds(new BoundingSphere(new Point3d(), 10d)); // bounding sphere of 10d radius
        sceneBG.addChild(positionInterpolator);

        sceneBG.addChild(rotateBehaviorZ(5000, sceneTGROT)); // add rotateBeahbiorZ to sceneBG
        sceneTG.addChild(sceneTGROT); // add sceneTGROT to sceneTG
        sceneTGROT.addChild(createSquareGeometry(Commons.Green)); // add child for rotation

        Shape3D shape3D = lineShape(Commons.Red, 3, LineAttributes.PATTERN_SOLID, true);
        sceneTG.addChild(shape3D);                           // attach the shape to 'sceneBG'
        sceneBG.compile();                                   // optimize 'sceneBG'

        return sceneBG; // return sceneBG
    }

    /* the main entrance of the application with specified window dimension */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Commons.setEye(new Point3d(1.35, 0.75, 2.0));
                new Commons.MyGUI(createScene(), "KL's Lab Quiz");
            }
        });
    }
}
