package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.FallingHeart;
import src.gameobjects.GraphicLifeCounter;

/**
 * A type of collision strategy, this strategy create a falling heart (extra life) when the brick get break.
 */
public class AnotherLifeStrategy extends CollisionStrategy{
    public static final int FALLING_SPEED = 100;
    private final ImageReader imageReader;
    private final WindowController windowController;
    private final Counter lifeCounter;

    /**
     * The constructor of another life strategy
     * @param gameObjects the game objects collection of the current game.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                    See its documentation for help.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     * @param lifeCounter the life counter of the game (the counter that is responsible for counting life
     *                    that left)
     */
    public AnotherLifeStrategy(GameObjectCollection gameObjects, ImageReader imageReader,
                               WindowController windowController, Counter lifeCounter) {
        super(gameObjects);

        this.imageReader = imageReader;
        this.windowController = windowController;
        this.lifeCounter = lifeCounter;
    }

    /**
     * When a brick detects a collision, this method is called and activates the current strategy.
     * It decrements the number of active bricks on the screen and removes the current brick from the screen,
     * and create a falling heart (extra life).
     * @param collidedObj the object that was collided (the brick)
     * @param colliderObj the object that collided with the brick (the ball).
     * @param bricksCounter the brick's counter, shows how much bricks are on the board.
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);

        fallingHeartCreator(collidedObj);

    }

    /**
     * a function that creates the object of the falling heart.
     * @param collidedObj the object that was collided ,we will create the falling heart from it
     *                    (from his place on screen).
     */
    private void fallingHeartCreator(GameObject collidedObj)
    {
        Renderable heartImage = imageReader.readImage(BrickerGameManager.HEART_IMAGE_PATH,true);
        FallingHeart fallingHeart = new FallingHeart(Vector2.ZERO,
                new Vector2(GraphicLifeCounter.HEART_SIZE,GraphicLifeCounter.HEART_SIZE),
                heartImage, this.gameObjects, this.windowController, this.lifeCounter);

        fallingHeart.setCenter(collidedObj.getCenter());
        fallingHeart.setVelocity(Vector2.DOWN.mult(FALLING_SPEED));

        this.gameObjects.addGameObject(fallingHeart, Layer.DEFAULT);

    }
}
