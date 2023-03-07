package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * The Class that deal with the Event that the ball hit brick ,part of Bricker game
 *
 * @author Yoni Ankri
 */
public class CollisionStrategy {

    protected final GameObjectCollection gameObjects;

    /**
     * The constructor of the Strategy object.
     * @param gameObjects An object which holds all game objects of the game running.
     */
    public CollisionStrategy(danogl.collisions.GameObjectCollection gameObjects){

        this.gameObjects = gameObjects;
    }

    /**
     * When a brick detects a collision, this method is called and activates the current strategy.
     * It decrements the number of active bricks on the screen and removes the current brick from the screen.
     * @param collidedObj the object that was collided (the brick)
     * @param colliderObj the object that collided with the brick (the ball).
     * @param bricksCounter the brick's counter, shows how much bricks are on the board.
     */
    public void onCollision(GameObject collidedObj, GameObject colliderObj,
                            Counter bricksCounter){

        if(this.gameObjects.removeGameObject(collidedObj, Layer.DEFAULT))
            bricksCounter.decrement();
    }
}
