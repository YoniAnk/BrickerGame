package src.gameobjects;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A class of the falling heart (that is created with Another life collision strategy)
 */
public class FallingHeart extends GameObject {
    private final GameObjectCollection gameObjects;
    private final WindowController windowController;
    private final Counter lifeCounter;

    /**
     * The constructor of the falling heart
     * @param topLeftCorner the top left corner to put the game object.
     * @param dimensions the dimensions of the falling heart.
     * @param renderable the image of the falling heart
     * @param gameObjects the game's objects collection (so the heart can delete himself when needed)
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     * @param lifeCounter the life counter of the game (the counter that is responsible for counting life
     *                    that left)
     */
    public FallingHeart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                        GameObjectCollection gameObjects, WindowController windowController,
                        Counter lifeCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjects = gameObjects;
        this.windowController = windowController;
        this.lifeCounter = lifeCounter;
    }

    /**
     * Function that tells the object with which other objects he can collide
     * @param other The other GameObject.
     * @return if it should collide with the other game object
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        boolean isPaddle = other instanceof Paddle;
        boolean isSecondaryPaddle = !(other instanceof SecondaryPaddle);
        return isPaddle && isSecondaryPaddle;
    }

    /**
     * Function that is responsible for what happen when the falling heart is collided with an item that it
     * should collide with
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (this.lifeCounter.value() < 4)
        {
            this.lifeCounter.increment();
        }
        this.gameObjects.removeGameObject(this,Layer.DEFAULT);
    }

    /**
     * The function that checks if the falling heart got out of the window and deletes it.
     * @param deltaTime unused.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (this.getTopLeftCorner().y() > this.windowController.getWindowDimensions().y())
        {
            this.gameObjects.removeGameObject(this, Layer.DEFAULT);
        }
    }
}
