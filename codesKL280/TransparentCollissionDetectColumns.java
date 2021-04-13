package codesKL280;

import java.util.Iterator;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.Color3f;

/* This behavior of collision detection highlights the
    object when it is in a state of collision. */
class TransparentCollisionDetectColumns extends Behavior {
    private boolean inCollision;
    private Shape3D shape;
    private Appearance shapeAppearance;
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;

    public TransparentCollisionDetectColumns(Shape3D s) {
        shape = s; // save the original color of 'shape"
        shapeAppearance = shape.getAppearance();
        inCollision = false;
    }

    public void initialize() { // USE_GEOMETRY USE_BOUNDS
        wEnter = new WakeupOnCollisionEntry(shape, WakeupOnCollisionEntry.USE_GEOMETRY);
        wExit = new WakeupOnCollisionExit(shape, WakeupOnCollisionExit.USE_GEOMETRY);
        wakeupOn(wEnter); // initialize the behavior
    }

    public void processStimulus(Iterator<WakeupCriterion> criteria) {
        TransparencyAttributes transparencyAttributes = new TransparencyAttributes();
        transparencyAttributes.setTransparencyMode(TransparencyAttributes.FASTEST);
        inCollision = !inCollision; // collision has taken place

        if (inCollision) { // change color to highlight 'shape'
            transparencyAttributes.setTransparency(0.5f); // set transparency for 0.5f
            shapeAppearance.setTransparencyAttributes(transparencyAttributes);
            wakeupOn(wExit); // keep the color until no collision
        } else { // change color back to its original
            transparencyAttributes.setTransparency(0f); // set transparency to 0f
            shapeAppearance.setTransparencyAttributes(transparencyAttributes);
            wakeupOn(wEnter); // wait for collision happens
        }
    }
}
