package codesKL280;

/* *********************************************************
 * For use by students to work on assignments and project.
 * Permission required material. Contact: xyuan@uwindsor.ca
 **********************************************************/

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.*;

public class CodeLab9 extends JPanel {
    private static final long serialVersionUID = 1L;
    private static SoundUtilityJOAL soundJOAL;
    private static String snd_pt = "cow";

    public class BehaviorArrowKey extends Behavior {
        private TransformGroup navigatorTG;
        private WakeupOnAWTEvent wEnter;

        public BehaviorArrowKey(ViewingPlatform targetVP, TransformGroup chasedTG) {
            navigatorTG = chasedTG;
            targetVP.getViewPlatformTransform();
        }

        public void initialize() {
            wEnter = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
            wakeupOn(wEnter);                              // decide when behavior becomes live
        }

        public void processStimulus(Iterator<WakeupCriterion> criteria) {
            Transform3D navigatorTF = new Transform3D();   // get Transform3D from 'navigatorTG'
            navigatorTG.getTransform(navigatorTF);
            Vector3d vct = new Vector3d();
            navigatorTF.get(vct);                          // get position of 'navigatorTG'
            soundJOAL.setPos(snd_pt, (float)vct.x, (float)vct.y, (float)vct.z); // set position and cast floats to vct x,y, and z
            wakeupOn(wEnter);                              // decide when behavior becomes live
        }
    }

    private static Appearance setApp(Color3f clr) {
        Appearance app = new Appearance();
        app.setMaterial(CodeLab6.setMaterial(clr));
        ColoringAttributes colorAtt = new ColoringAttributes();
        colorAtt.setColor(clr);
        app.setColoringAttributes(colorAtt);
        return app;
    }


    public BranchGroup soundObject() {
        BranchGroup objectBG = new BranchGroup();
        TransformGroup objectTG = new TransformGroup();
        objectTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//        objectTG.addChild(new Box(0.35f,0.35f,0.35f, setApp(CommonsKL.Orange)));
        objectTG.addChild(loadShape()); // load shape
        soundJOAL = new SoundUtilityJOAL();
        if (!soundJOAL.load(snd_pt, 0f, 0f, 10f, true))     // fix 'snd_pt' at cow location
            System.out.println("Could not load " + snd_pt);
        else
            soundJOAL.play(snd_pt);                         // start 'snd_pt'

        ViewingPlatform ourView = CommonsKL.getSimpleU().getViewingPlatform();
        KeyNavigatorBehavior  myRotationbehavior = new KeyNavigatorBehavior(objectTG);
        BehaviorArrowKey myViewRotationbehavior = new BehaviorArrowKey(ourView, objectTG);
        myRotationbehavior.setSchedulingBounds(new BoundingSphere());
        objectTG.addChild(myRotationbehavior);
        myViewRotationbehavior.setSchedulingBounds(new BoundingSphere());
        objectTG.addChild(myViewRotationbehavior);

        objectBG.addChild(objectTG);

        return objectBG;
    }

    private TransformGroup loadShape() {
        TransformGroup tg = new TransformGroup();
        int flags = ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY; // create flags for Object File
        ObjectFile f = new ObjectFile(flags);
        File file = new File("src/codesKL280/CowTray.obj"); // file path for obj
        Scene s = null;
        try{
            s = f.load(file.toURI().toURL());
        }catch (FileNotFoundException | MalformedURLException e) {
            System.err.println(e);
            System.exit(1);
        }

        BranchGroup objBG = s.getSceneGroup(); // get scene group for objBG
        Shape3D cowTray = (Shape3D) objBG.getChild(0); // cast Shpae3D to objBG's child at index 0
        cowTray.setAppearance(setApp(CommonsKL.Orange)); // set cowTrays color to orange

        objBG.removeAllChildren(); // remove all children from objBG
        Transform3D rotate = new Transform3D(); // create a transfrom3d for rotations
        rotate.rotX(-Math.cos(Math.PI * 2) * Math.PI / 2); // rotate it 90 degrees
        Transform3D transform3D = new Transform3D(); // create a new transform3D to rotate
        transform3D.mul(rotate);

        transform3D.setScale(0.45); // size cow down too scale 0.45
        tg.setTransform(transform3D);

        tg.addChild(cowTray); // add cow tray to transform group
        return tg; // return tg
    }

    /* a function to create and return the scene BranchGroup */
    public BranchGroup createScene() {
        CommonsKL.createUniverse();

        BranchGroup scene = new BranchGroup();            // create 'scene' as content branch
        TransformGroup scene_TG = new TransformGroup();   // create 'scene_TG' TransformGroup

        scene_TG.addChild(soundObject());
        scene.addChild(scene_TG); // add scene_TG to scene BG
        scene.addChild(threeAxes(CommonsKL.Yellow, 0.5f));    // add axes
        CodeAssign3.addLights(scene, CommonsKL.White);                    // point+amb

        return scene;
    }

    /* the main entrance of the application with specified window dimension */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CommonsKL.setEye(new Point3d(1.35, 0.35, 2.0));
                CodeLab9 codeLab9 = new CodeLab9();
                new CommonsKL.MyGUI(codeLab9.createScene(), "KL's Lab 9");
            }
        });
    }

    /* a function to create and return a Shape3D */
    public static Shape3D threeAxes(Color3f yColor, float ln) {
        Point3f pts[] = { new Point3f(0, 0, 0),              // use 4 points for axices
                new Point3f(0, 0, ln),
                new Point3f(ln, 0, 0),   new Point3f(0, ln, 0) };
        int[] indices = {0, 1, 0, 2, 0, 3};                  // the Z-, X-, Y-axis

        IndexedLineArray lines = new IndexedLineArray(4,
                LineArray.COORDINATES | LineArray.COLOR_3, 6);

        lines.setCoordinates(0, pts);
        lines.setCoordinateIndices(0, indices);

        Color3f[] line_clr = {CommonsKL.Red, CommonsKL.Green, yColor};
        int[] c_indices = {0, 0, 1, 1, 2, 2};
        lines.setColors(0,  line_clr);                       // set color for each axis
        lines.setColorIndices(0, c_indices);

        return new Shape3D(lines);                           // return the Shape3D
    }

}
