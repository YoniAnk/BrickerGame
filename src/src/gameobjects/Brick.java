package src.gameobjects;

import src.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * The class of the Bricks, part of Bricker game.
 *
 * @author Yoni Ankri
 */
public class Brick extends GameObject {
    private CollisionStrategy strategy;
    private Counter counter;

    /**
     * This constructor extends the super's GameObject constructor, and also saves the strategy given.
     * @param topLeftCorner the position in the window the top left corner of the object will be placed.
     * @param dimensions the 2d dimensions of the object on the screen.
     * @param renderable the image object to display on the screen.
     * @param strategy the strategy that will be used when the brick breaks.
     * @param counter the counter of the bricks, tells how much bricks are in the game.
     */
    public Brick(Vector2 topLeftCorner,
                 Vector2 dimensions,
                 Renderable renderable, CollisionStrategy strategy,
                 Counter counter) {

            super(topLeftCorner, dimensions, renderable);
            this.strategy = strategy;
            this.counter = counter;
        }

    /**
     * function that decides what happen in collision
     *
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        this.strategy.onCollision(this,other,this.counter);
    }
}

