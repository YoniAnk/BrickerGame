package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * The class of the ball, part of Bricker game.
 *
 * @author Yoni Ankri
 */
public class Ball extends GameObject {
    public static final int INIT_VALUE_COUNTER = 0;
    private final Sound collisionSound;
    private final Counter counter;

    /**
     * This is the ball object constructor. It uses it's super's constructor and saves the sound file.
     *
     * @param topLeftCorner position of the top left corner of the ball in the window.
     * @param dimensions the dimensions of the ball
     * @param renderable the image object of the ball
     * @param collisionSound the sound file object of the ball's collision.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.counter = new Counter(INIT_VALUE_COUNTER);
    }

    /**
     * This method overwrites the OnCollisionEnter of GameObject.
     * When it collides with another object, it flips its direction.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        this.counter.increment();

        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        this.collisionSound.play();

    }

    /**
     * Function that returns the amount of the collisions that the ball made from the moment that he was
     * created.
     * @return the number of the collisions.
     */
    public int getBallCollisions()
    {
        return this.counter.value();
    }
}
