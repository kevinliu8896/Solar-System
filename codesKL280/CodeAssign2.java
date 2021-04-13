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

public class CodeAssign2 extends JPanel {
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

    public static Shape3D rectangleTexturedBackground(float w, float h, float s, float z) {
        QuadArray quadArray = new QuadArray(4, QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2);
        Point3f[] verts = new Point3f[4]; // create 4 point3f's called verts
        float[][] points = {{0f, 0f}, {1f, 0f}, {1f, 1f}, {0f, 1f}}; // initializing point positions
        // declaring index of each point3f named verts
        verts[0] = new Point3f(-w * s, -h * s, z);
        verts[1] = new Point3f(w * s, -h * s, z);
        verts[2] = new Point3f(w * s, h * s, z);
        verts[3] = new Point3f(-w * s, h * s, z);
        Appearance appearance = new Appearance(); // create an appearance
        appearance.setTexture(texturedApp("background")); // apply texture

        for (int i = 0; i < 4; i++) { // loop through all quad array coordinates to set the points and apply the texture
            quadArray.setCoordinate(i, verts[i]);
            quadArray.setTextureCoordinate(0, i, points[i]);
        }
        return new Shape3D(quadArray, appearance); // return Shape3D of quadArray and appearance
    }

    private static QuadArray createRectangleGeometry(Color3f color, Point3f size, Vector2f scale) { // method to create a rectangle to create base for pavillion
        QuadArray quadArray = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.COORDINATES); // initialize a QuadArray with a vertexCount of 4

        quadArray.setCoordinate(0, new Point3f(size.x * scale.x, size.y * scale.y, size.z)); // set coordinate for quad array at size.x * scale.x, size.y * scale.y, size.z
        quadArray.setCoordinate(1, new Point3f(-size.x * scale.x, size.y * scale.y, size.z)); // set coordinate for quad array at -size.x * scale.x, size.y * scale.y, size.z
        quadArray.setCoordinate(2, new Point3f(-size.x * scale.x, -size.y * scale.y, size.z)); // set coordinate for quad array at -size.x * scale.x, -size.y * scale.y, size.z
        quadArray.setCoordinate(3, new Point3f(size.x * scale.x, -size.y * scale.y, size.z)); // set coordinate for quad array at size.x * scale.x, -size.y * scale.y, size.z

        for (int i = 0; i < 4; i++) { // iterate through all indexes of the QuadArray to:
            quadArray.setColor(i, color); // set each coordinates index to the color that was passed in the method call
        }

        return quadArray; // return quadArray
    }

    private static TransformGroup createBase(float scale) { // function to create the base for pavillion
        TransformGroup transformGroupBase = new TransformGroup(); // create a transform group
        Color3f[] color3fs = {CommonsKL.Red, CommonsKL.Yellow, CommonsKL.Cyan, CommonsKL.Green, CommonsKL.Blue, CommonsKL.Magenta}; //color array
        Vector3f[] vector3fs = new Vector3f[4]; // create vector3f array with 4 index
        Vector3f[] vector3fs2 = {new Vector3f(0, -0.5f, 0), new Vector3f(0, -0.5f - (0.08f * scale), 0)}; // initialize vector3fs2

        // declaring variables for x and z in reference to unit circle for creating base
        float x;
        float z;
        // this will make the 4 sides of the base:
        for (int i = 0; i < 4; i++) { // iterate through 4 times to set x and z values
            x = (float) Math.cos(Math.PI / 2 * i) * scale;
            z = (float) Math.sin(Math.PI / 2 * i) * scale;
            vector3fs[i] = new Vector3f(x, -0.5f - (0.08f * scale / 2), z); // set vector3fs at index i's iteration and set it to x, y:-0.5 - (0.08f * scale/2), z
            QuadArray quadArray = createRectangleGeometry(color3fs[i], new Point3f(1f, -0.08f * scale, 0f), new Vector2f(scale, scale)); // use quad array to create rectangles using function createRectangleGeometry
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
            transformGroup.addChild(new Shape3D(quadArray)); // add child to transformGroup using new Shape3D quadArray
            transformGroupBase.addChild(transformGroup); // add child to transformGroupBase using transformGroup
        }

        // create the top and bottom of the base:
        for (int j = 0; j < 2; j++) { // iterate 2 times for top and bottom
            QuadArray quadArray = createRectangleGeometry(color3fs[j + 4], new Point3f(1, 1, 0), new Vector2f(scale, scale)); // call createRectangleGeometry set colors, point3f and vector2f
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
            transformGroup.addChild(new Shape3D(quadArray)); // add child to transformGroup using quadArray
            transformGroupBase.addChild(transformGroup); // add child to transformGroupBase
        }

        return transformGroupBase; // return transformGroupBase
    }


    private static Shape3D createPavillion(int n) { // create a parameter called n it represents the number of sides a 2D shape will have
        float r = 0.6f, x, y;                             // change r to a constant distance of 0.6f to the side corners
        Point3f coor[] = new Point3f[n + 2]; // make Point3f[n + 2] to represent the amount of sides passed in

        Appearance pavillionApp = new Appearance(); // create an Appearance called pavillionApp
        pavillionApp.setTexture(texturedApp("MarbleTexture")); // set it to the MarbleTexture.jpg texture image

        LineArray lineArr = new LineArray(n * 2, LineArray.COLOR_3 | LineArray.COORDINATES); // doubling vertex count by using n * 2
        for (int i = 0; i < n; i++) { // iterate through n number of sides
            x = (float) Math.cos(Math.PI / 180 * (90 + 360 / n * i)) * r; // take 360 degrees and divide it by the number passed in by n for x and y
            y = (float) Math.sin(Math.PI / 180 * (90 + 360 / n * i)) * r;
            coor[i] = new Point3f(x, 0.5f, -y); // set y to 0.5f for the center of the base
        }

        TriangleArray triArr = new TriangleArray(n * 6, TriangleArray.COORDINATES | TriangleArray.TEXTURE_COORDINATE_2); // create triangle array with texture coordiante 2

        for (int i = 0; i < n; i++) { // iterate through n number of sides
            triArr.setCoordinate(i * 3, new Point3f(0, 0.65f, 0)); // set y to 0.65 on the y axis
            triArr.setCoordinate(i * 3 + 1, coor[i]); // set a coor[i] at index i * 3 + 1
            triArr.setCoordinate(i * 3 + 2, coor[(i + 1) % n]); // set a coor[(i + 1) % n] at index i * 3 + 2

            // setting texture to the texture coordiantes with tex coord  2f setting coordiantes at index i * 3, i * 3 + 1, i * 3 + 2
            triArr.setTextureCoordinate(0, i * 3, new TexCoord2f(0f, 0f));
            triArr.setTextureCoordinate(0, i * 3 + 1, new TexCoord2f(1f, 1f));
            triArr.setTextureCoordinate(0, i * 3 + 2, new TexCoord2f(1f, 0f));

            triArr.setCoordinate(3 * n + i * 3, new Point3f(0, 0.5f, 0)); // set y to 0.5f
            triArr.setCoordinate(3 * n + i * 3 + 2, coor[i]); // set a coor[i] at index 3 * n + i * 3 + 2
            triArr.setCoordinate(3 * n + i * 3 + 1, coor[(i + 1) % n]); // set a coor[(i + 1) & n] at index 3 * n * i * 3 + 1

            // setting texture to the texture coordiantes with tex coord 2f setting coordinates at index 3 * n + i * 3, 3 * n + i * 3 + 2, 3 * n + i * 3 + 1
            triArr.setTextureCoordinate(0, 3 * n + i * 3, new TexCoord2f(0f, 0f));
            triArr.setTextureCoordinate(0, 3 * n + i * 3 + 2, new TexCoord2f(1f, 1f));
            triArr.setTextureCoordinate(0, 3 * n + i * 3 + 1, new TexCoord2f(1f, 0f));
        }

        if (n < 3) { // if a number less than 3 is passed in a coloured cube will be generated
            return new ColorCube(0.35); // returning the color cube with scale 0.35
        } else {
            return new Shape3D(triArr, pavillionApp); // return triangle array and pavillionApp
        }
    }


    private static BranchGroup cylinders(int n) {
        float r = 0.5f, x, y;  // vertex at 0.5f away from origin
        Point3f coor[] = new Point3f[n];

        for (int i = 0; i < n; i++) { // iterate through n number of sides
            x = (float) Math.cos(Math.PI / 180 * (90 + 360 / n * i)) * r; // take 360 degrees and divide it by the number passed in by n for x and y
            y = (float) Math.sin(Math.PI / 180 * (90 + 360 / n * i)) * r;
            coor[i] = new Point3f(x, 0.0f, -y); // set coordinate 0.5f on y axis
        }

        BranchGroup bg = new BranchGroup(); // create branch group
        Transform3D transform3D = new Transform3D(); // create Transform3d
        Primitive[] priShape = new Primitive[n]; // make an array called priShape of type Primitive storing n amount

        for (int i = 0; i < n; i++) { // iterate through number passed into n
            Appearance app = new Appearance(); // create appearance called app

            app.setTexture(texturedApp("MarbleTexture"));// set texture to MarbleTexture.jpg

            priShape[i] = new Cylinder(0.04f, 1, Primitive.GENERATE_TEXTURE_COORDS, app); // index of i creates a new shape of type Cylinder with a radius of 0.04f height 1 and appearance that has been set above

            transform3D.setTranslation(new Vector3f(coor[i].x, coor[i].y, coor[i].z)); // putting coordinates using translation
            TransformGroup tg = new TransformGroup(); // create a transform group
            tg.setTransform(transform3D); // set transform group to transform3D
            bg.addChild(tg); // add tf to branch group
            tg.addChild(priShape[i]); // add priShape[i] to transform group
        }

        if (n < 3) { // return cube
            return new BranchGroup();
        }

        return bg; // return branch group
    }

    private static Texture texturedApp(String name) { // function to render Marble Texture
        String filename = name + ".jpg"; // file name is marbleTexture.jpg
        TextureLoader loader = new TextureLoader(filename, null); // load file name
        ImageComponent2D image = loader.getImage(); // loader gets image
        if (image == null) { // if image file not found display:
            System.out.println("Cannot load file " + filename);
        }
        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight()); // get texture width and height
        texture.setImage(0, image); // sets image to texture

        return texture; // return texture
    }

    /* a function to create and return the scene BranchGroup */
    public static BranchGroup createScene() {
        int n = 6; // pass in n for cube(n < 3) or n = 6 for pavillion as a hexagon
        BranchGroup sceneBG = new BranchGroup();             // create 'objsBG' for content
        TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
        sceneBG.addChild(sceneTG);                           // add TG to the scene BranchGroup
        sceneBG.addChild(CommonsKL.rotateBehavior(10000, sceneTG));
        // make sceneTG continuously rotating
        sceneBG.addChild(createAxisGeometry(CommonsKL.Yellow, 0.5f)); // calling createAxisGeometry method
        sceneBG.addChild(rectangleTexturedBackground(15f, 15f, 1f, -17.0f));
        sceneTG.addChild(createBase(0.6f));
        sceneTG.addChild(createPavillion(n)); // call createPyramidGeometry method
        sceneTG.addChild(cylinders(n)); // attach "n" cylinders

        sceneBG.compile(); // optimize objsBG
        return sceneBG;
    }

    /* the main entrance of the application via 'MyGUI()' of "CommonXY.java" */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CommonsKL.setEye(new Point3d(0.05, 0.35, 2.5));
                new CommonsKL.MyGUI(createScene(), "KL's Assignment 2");
            }
        });
    }
}


