package codesKL280;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

public class CodeFinal extends JPanel {
    private static final long serialVersionUID = 1L;
    private static PickTool pickTool;
    private static Shape3D shape3D[]= new Shape3D[5]; // create shape3d array for transparency

    public static RotationInterpolator rotationInterpolator;
    private static JFrame frame;

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

    private static Node createRectangleGeometry(Color3f color, Point3f size, Vector2f scale) { // method to create a rectangle to create base for pavillion
        QuadArray quadArray = new QuadArray(8, QuadArray.COLOR_3 | QuadArray.COORDINATES); // initialize a QuadArray with a vertexCount of 4
        Point3f[] points = new Point3f[4];
        points[0] = new Point3f(size.x * scale.x, size.y * scale.y, size.z);// set coordinate for quad array at size.x * scale.x, size.y * scale.y, size.z
        points[1] = new Point3f(-size.x * scale.x, size.y * scale.y, size.z);// set coordinate for quad array at -size.x * scale.x, size.y * scale.y, size.z
        points[2] = new Point3f(-size.x * scale.x, -size.y * scale.y, size.z); // set coordinate for quad array at -size.x * scale.x, -size.y * scale.y, size.z
        points[3] = new Point3f(size.x * scale.x, -size.y * scale.y, size.z); // set coordinate for quad array at size.x * scale.x, -size.y * scale.y, size.z

//        quadArray.setCoordinate(0, new Point3f(size.x * scale.x, size.y * scale.y, size.z)); // set coordinate for quad array at size.x * scale.x, size.y * scale.y, size.z
//        quadArray.setCoordinate(1, new Point3f(-size.x * scale.x, size.y * scale.y, size.z)); // set coordinate for quad array at -size.x * scale.x, size.y * scale.y, size.z
//        quadArray.setCoordinate(2, new Point3f(-size.x * scale.x, -size.y * scale.y, size.z)); // set coordinate for quad array at -size.x * scale.x, -size.y * scale.y, size.z
//        quadArray.setCoordinate(3, new Point3f(size.x * scale.x, -size.y * scale.y, size.z)); // set coordinate for quad array at size.x * scale.x, -size.y * scale.y, size.z


        for (int i = 0; i < 8; i++) { // iterate through all indexes of the QuadArray to:
            quadArray.setCoordinate(i, points[i % 4]); // set coordinates at index i
            quadArray.setCoordinate(7 - i, points[i % 4]);
            quadArray.setColor(i, color); // setting colors of the quad array at index i following the order of the color array
        }
        Appearance appearance = new Appearance();
        appearance.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
        Shape3D shape3D = new Shape3D(quadArray);
        shape3D.setPickable(false);
        shape3D.setUserData(0);
        return shape3D;
    }

    private static TransformGroup createBase(float scale) { // create base method
        TransformGroup base = new TransformGroup();
        Color3f[] clr3f = {Commons.Yellow, Commons.Red, Commons.Green, Commons.Blue, Commons.White}; // alter color array to red as the
        // front and blue on the back. Green for left side and yellow for right side. And white covers the bottoms side as the top is hollow

        Vector3f[] sides = new Vector3f[5];
        sides[4] = new Vector3f(0, -0.4f, 0);
        float x; // create varaibles x and z to scale them
        float z;
        for (int i = 0; i < 5; i++) {
            if (i < 4) {
                double a = (Math.PI / 2) * i;
                // scaling for x and z
                x = (float) Math.cos(a) * scale;
                z = (float) Math.sin(a) * scale;
                sides[i] = new Vector3f(x, 0, z);
            }

            Transform3D transform3D = new Transform3D();
            if (i < 4) { // go through all 4 indexes of the shape

                if (i % 2 == 0) { // if the iteration of i % 2 is equal to 0 rotate the transform3D's y to angle: -Math.cos(Math.PI / 2 * i) * Math.PI / 2
                    transform3D.rotY(-Math.cos(Math.PI / 2 * i) * Math.PI / 2);
                }

                if (i == 3) {
                    transform3D.rotY(Math.PI);
                }
            } else { // rotating the top and bottom of base
                transform3D.rotX(Math.cos(Math.PI * (i % 4)) * Math.PI / 2); // rotate x by 90 degrees
            }
            transform3D.setTranslation(sides[i]); // set translation at sides index i for transfrom 3d
            TransformGroup transformGroup = new TransformGroup(); // create new transform group
            transformGroup.setTransform(transform3D); // set transform for transform group to transform 3d
            transformGroup.addChild(createRectangleGeometry(clr3f[i], new Point3f(1, 1, 0), new Vector2f(scale, scale)));
            base.addChild(transformGroup); // add child of transform group to the base
        }
        return base; // returns base
    }

    public static TransformGroup letters3D(String textString, Color3f color3f, Point3f point3f) {
        Font my2DFont = new Font("Arial", Font.PLAIN, 1); //setting font: font name, font style, font size
        FontExtrusion myExtrude = new FontExtrusion();
        Font3D font3D = new Font3D(my2DFont, myExtrude);
        Text3D text3D = new Text3D(font3D, textString, point3f, Text3D.ALIGN_CENTER, Text3D.PATH_RIGHT);
        ColoringAttributes color = new ColoringAttributes();
        color.setColor(color3f); //setting color
        Appearance app = new Appearance();
        app.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
        app.setColoringAttributes(color); //adding color for appearance
        Transform3D scaler = new Transform3D();
        scaler.setScale(0.4); // scaling to 0.4
        TransformGroup scene_TG = new TransformGroup(scaler);
        Shape3D shape3D = new Shape3D(text3D, app);
        shape3D.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        shape3D.setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE);
        shape3D.setPickable(true);
        shape3D.setUserData(0);
        shape3D.setName(textString);

        scene_TG.addChild(new Shape3D(text3D, app)); // create new scene group
        return scene_TG; //returning the scene_TF
    }

    /* a function to define Appearance with color 'c' and transparency 't' */
    private static Appearance myApp(float t, Color3f c) {
        Appearance app = new Appearance();

        if (t > 0.0f) {                                      // set only when non-opaque
            TransparencyAttributes ta =
                    new TransparencyAttributes(TransparencyAttributes.FASTEST, t);
            app.setTransparencyAttributes(ta);               // FASTEST NICEST SCREEN_DOOR BLENDED NONE
        }
        // set for color
        ColoringAttributes ca = new ColoringAttributes(c, ColoringAttributes.FASTEST);
        app.setColoringAttributes(ca);

        PolygonAttributes pa = new PolygonAttributes();      // set polygon attributes:
        pa.setCullFace(PolygonAttributes.CULL_NONE);         // make both sides visible with 'CULL_NONE'
        app.setPolygonAttributes(pa);

        return app;
    }

    /* a function to create and return the scene BranchGroup */
    public static BranchGroup createScene() {
        BranchGroup sceneBG = new BranchGroup();             // create 'sceneBG' for content
        pickTool = new PickTool(sceneBG);
        pickTool.setMode(PickTool.GEOMETRY);                     // allow for mouse picking on bound

        TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
        sceneBG.addChild(new ColorCube(0.2));

        sceneBG.addChild(createAxisGeometry(CommonsKL.Yellow, 0.6f)); // calling createAxisGeometry method
        sceneTG.addChild(createBase(0.4f));
        sceneTG.addChild(letters3D("V", Commons.Red, new Point3f(-1f, 1f, 0f)));
        sceneTG.addChild(letters3D("R", Commons.Yellow, new Point3f(-0.4f, 1f, 0f)));
        sceneTG.addChild(letters3D("2", Commons.Blue, new Point3f(0.2f, 1f, 0f)));
        sceneTG.addChild(letters3D("1", Commons.Green, new Point3f(0.8f, 1f, 0f)));
//        Appearance app = myApp(0.75f, Commons.White);
//        ColorCube colorCube = new ColorCube(0.4);
//        colorCube.setAppearance(app);
//        sceneBG.addChild(colorCube);

        rotationInterpolator = Commons.rotateBehavior(15000, sceneTG); // add roation interpolator to 15000
        rotationInterpolator.setMaximumAngle((float) (-Math.PI * 2)); // set max angle
        rotationInterpolator.getAlpha().pause();
        sceneTG.addChild(rotationInterpolator); // add rotation interpolator to sceneTG

        sceneBG.addChild(sceneTG);                             // add TG to the scene BranchGroup
        return sceneBG; // return sceneBG
    }

    /* a constructor to add 'sceneBG' to SimpleUniverse and to allow mouse listening */
    public CodeFinal(BranchGroup sceneBG) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);

        canvas.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point3d point3d = new Point3d(), center = new Point3d();
                canvas.getPixelLocationInImagePlate(e.getX(), e.getY(), point3d);   // obtain AWT pixel in ImagePlate coordinates
                canvas.getCenterEyeInImagePlate(center);              // obtain eye's position in IP coordinates

                Transform3D transform3D = new Transform3D();          // matrix to relate ImagePlate coordinates~
                canvas.getImagePlateToVworld(transform3D);            // to Virtual World coordinates
                transform3D.transform(point3d);                       // transform 'point3d' with 'transform3D'
                transform3D.transform(center);                        // transform 'center' with 'transform3D'

                Vector3d mouseVec = new Vector3d();
                mouseVec.sub(point3d,center);
                mouseVec.normalize();
                pickTool.setShapeRay(point3d,mouseVec);              // send a PickRay for intersection

                if (pickTool.pickClosest() != null) { // if pickRay is not null
                    PickResult pickResult = pickTool.pickClosest(); // get closest node
                    Shape3D pickPiece = (Shape3D) pickResult.getNode(PickResult.SHAPE3D); // grab the Shape3D
                    rotationInterpolator.getAlpha().resume();
                    TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.FASTEST, 0);
                    pickPiece.getAppearance().setTransparencyAttributes(new TransparencyAttributes());
                    if (pickPiece != null && pickPiece.getUserData() != null) {
                        if ((int) pickPiece.getUserData() == 0) {

                            if(pickPiece.getName().equalsIgnoreCase("v")) { // if pick v then set transparency to red side and v
                                shape3D[1].setAppearance(myApp(0.75f, Commons.Red));
                                pickPiece.setAppearance(myApp(0.75f, Commons.Red));
                                System.out.println("v");
                                pickPiece.setUserData(1);
                            }
                            else if(pickPiece.getName().equalsIgnoreCase("r")) { // if pick R then set transparency to tellow side and R
                                shape3D[0].setAppearance(myApp(0.75f, Commons.Yellow));
                                pickPiece.setAppearance(myApp(0.75f, Commons.Yellow));
                                pickPiece.setUserData(1);
                            }
                            else if(pickPiece.getName().equalsIgnoreCase("2")) { // if pick 2 then set transparency to blue side and 2
                                shape3D[3].setAppearance(myApp(0.75f, Commons.Blue));
                                pickPiece.setAppearance(myApp(0.75f, Commons.Blue));
                                pickPiece.setUserData(1);
                            }
                            else if(pickPiece.getName().equalsIgnoreCase("1")) { // if pick 1 then set green side to transparent and 1
                                transparencyAttributes.setTransparency(0.75f);
                                shape3D[2].setAppearance(myApp(0.75f, Commons.Green));
                                pickPiece.setAppearance(myApp(0.75f, Commons.Green));
                                pickPiece.setUserData(1);
                            }
                        }
                    }
                    
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        SimpleUniverse su = new SimpleUniverse(canvas);       // create a SimpleUniverse
        Commons.setEye(new Point3d(1.35, 0.75, 2.0));
        Commons.defineViewer(su);                             // set the viewer's location

        sceneBG.compile();
        su.addBranchGraph(sceneBG);                           // attach the scene to SimpleUniverse

        setLayout(new BorderLayout());
        add("Center", canvas);
        frame.setSize(600, 600);                              // set the size of the JFrame
        frame.setVisible(true);
    }

    /* the main entrance of the application */
    public static void main(String[] args) {
        frame = new JFrame("KL's Final Exam");
        frame.getContentPane().add(new CodeFinal(createScene()));
    }
}