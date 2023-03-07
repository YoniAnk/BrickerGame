package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * The class that is responsible for the Secondary paddle that appear after the Paddle Collision strategy.
 * this paddle is a type of the normal paddle ,but it disappears when it gets hit a certain amount of times.
 */
public class SecondaryPaddle extends Paddle{
    private final Counter collisionCounter;
    private final GameObjectCollection gameObjects;

    /**
     * This constructor creates the paddle object.
     *
     * @param topLeftCorner    the top left corner of the position of the text object
     * @param dimensions       the size of the text object
     * @param renderable       the image file of the paddle
     * @param inputListener    The input listener which waits for user inputs and acts on them.
     * @param windowDimensions The dimensions of the screen, to know the limits for paddle movements.
     * @param minDistFromEdge  Minimum distance allowed for the paddle from the edge of the walls
     */
    public SecondaryPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                           UserInputListener inputListener, Vector2 windowDimensions, int minDistFromEdge,
                           int initLives, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistFromEdge);

        this.collisionCounter = new Counter(initLives);
        this.gameObjects = gameObjects;
    }

    /**
     * Function that returns how many times the paddle was hit.
     * @return the value of hits.
     */
    public int getCollisionCounter (){
        return this.collisionCounter.value();
    }

    /**
     * funtion that is responsible to delete the paddle when it arrives to his limit of collisions.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        if (other instanceof Ball){
            this.collisionCounter.decrement();
        }

        if (this.collisionCounter.value() <= 0)
        {
            gameObjects.removeGameObject(this, Layer.DEFAULT);
        }

    }
}
