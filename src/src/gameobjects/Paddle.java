package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * The class of the paddle, part of Bricker game.
 *
 * @author Yoni Ankri
 */
public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 300;
    private final Vector2 windowDimensions;
    private final int minDistFromEdge;
    private final UserInputListener inputListener;

    /**
     * This constructor creates the paddle object.
     * @param topLeftCorner the top left corner of the position of the text object
     * @param dimensions the size of the text object
     * @param renderable the image file of the paddle
     * @param inputListener The input listener which waits for user inputs and acts on them.
     * @param windowDimensions The dimensions of the screen, to know the limits for paddle movements.
     * @param minDistFromEdge Minimum distance allowed for the paddle from the edge of the walls
     */
    public Paddle(Vector2 topLeftCorner,
           Vector2 dimensions,
           Renderable renderable,
           UserInputListener inputListener,
           Vector2 windowDimensions,
           int minDistFromEdge) {

        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minDistFromEdge = minDistFromEdge;
    }

    /**
     * This method is overwritten from GameObject. If right and/or left key is recognised as pressed by the
     * input listener, it moves the paddle, and check that it doesn't move past the borders.
     * @param deltaTime unused
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);


        Vector2 movementDir = Vector2.ZERO;

        float curRightCornerPlace = (this.windowDimensions.x() - this.minDistFromEdge) -
                (this.getTopLeftCorner().x()+this.getDimensions().x());

        if ((inputListener.isKeyPressed(KeyEvent.VK_LEFT)) &&
                (this.getTopLeftCorner().x() > this.minDistFromEdge)) {

            movementDir = movementDir.add(Vector2.LEFT);
        }

        if ((inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) &&
                (curRightCornerPlace > 0)){

            movementDir = movementDir.add(Vector2.RIGHT);
        }

        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }
}
