package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.SecondaryPaddle;

/**
 * A type of collision strategy, this strategy create an extra paddle in the middle of the screen when the
 * brick get break.
 */
public class PaddleStrategy extends CollisionStrategy{

    private static final String SECONDARY_PADDLE_IMAGE_PATH = "assets/botGood.png";
    private final ImageReader imageReader;
    private final WindowController windowsController;
    private final int minDistFromEdge;
    private final int initLives;
    private final UserInputListener inputListener;
    private static boolean paddleOnScreen = false;
    private static SecondaryPaddle secondaryPaddle;

    /**
     * The constructor of the extra paddle collision strategy
     * @param gameObjects the game objects collection of the current game.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                    See its documentation for help.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param minDistFromEdge the minimum distance of the paddle from the edges of the window.
     * @param initLives the life that the extra paddle has.
     */
    public PaddleStrategy(GameObjectCollection gameObjects, ImageReader imageReader,
                          WindowController windowController, UserInputListener inputListener,
                          int minDistFromEdge, int initLives)
    {
        super(gameObjects);
        this.imageReader = imageReader;
        this.windowsController = windowController;
        this.minDistFromEdge = minDistFromEdge;
        this.initLives = initLives;
        this.inputListener = inputListener;
    }

    /**
     * When a brick detects a collision, this method is called and activates the current strategy.
     * It decrements the number of active bricks on the screen and removes the current brick from the screen,
     * and create an extra paddle (if there is not one on screen already).
     * @param collidedObj the object that was collided (the brick)
     * @param colliderObj the object that collided with the brick (the ball).
     * @param bricksCounter the brick's counter, shows how much bricks are on the board.
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);


        if (secondaryPaddle != null)
        {
            paddleOnScreen = (secondaryPaddle.getCollisionCounter() > 0);
        }

        if (!paddleOnScreen)
        {
            secondaryPaddle = SecondaryPaddleCreator();
            paddleOnScreen = true;
        }

    }

    /**
     * the function that creates the extra paddle and add it to the game objects.
     * @return the paddle that was created
     */
    private SecondaryPaddle SecondaryPaddleCreator() {
        Renderable paddleImage = imageReader.readImage(SECONDARY_PADDLE_IMAGE_PATH,
                false);

        SecondaryPaddle secondaryPaddle = new SecondaryPaddle(Vector2.ZERO,
                new Vector2(BrickerGameManager.PADDLE_WIDTH, BrickerGameManager.PADDLE_LENGTH),
                paddleImage, this.inputListener, this.windowsController.getWindowDimensions(),
                this.minDistFromEdge, this.initLives, gameObjects);

        Vector2 windowsDims = new Vector2(this.windowsController.getWindowDimensions());

        secondaryPaddle.setCenter(new Vector2((float)(windowsDims.x()*0.5),
                (float)(windowsDims.y()*0.5)));

        gameObjects.addGameObject(secondaryPaddle, Layer.DEFAULT);

        return secondaryPaddle;
    }
}
