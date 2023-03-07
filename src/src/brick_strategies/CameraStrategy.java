package src.brick_strategies;


import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;
import src.gameobjects.CollisionCounter;

/**
 * A type of collision strategy, this strategy turn on a camera when the brick get break.
 */
public class CameraStrategy extends CollisionStrategy{
    private final Ball mainBall;
    private final WindowController windowController;
    private final BrickerGameManager gameManager;
    private static CollisionCounter collisionCounter;

    /**
     * The constructor of the Camera strategy.
     * @param gameObjects the game objects collection of the current game.
     * @param ball the ball for the camera to follow
     * @param windowController the window controller of the game's window
     * @param gameManager the game manager (for starting the camera)
     */
    public CameraStrategy(GameObjectCollection gameObjects, Ball ball,
                          WindowController windowController, BrickerGameManager gameManager) {
        super(gameObjects);
        this.mainBall = ball;
        this.windowController = windowController;
        this.gameManager = gameManager;

        collisionCounter = new CollisionCounter(this.mainBall,gameManager);

        this.gameObjects.addGameObject(collisionCounter);
    }

    /**
     * When a brick detects a collision, this method is called and activates the current strategy.
     * It decrements the number of active bricks on the screen and removes the current brick from the screen,
     * and activate the camera if the main ball hit it and if it's not turned on already.
     * @param collidedObj the object that was collided (the brick)
     * @param colliderObj the object that collided with the brick (the ball).
     * @param bricksCounter the brick's counter, shows how much bricks are on the board.
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);

        if ((gameManager.getCamera() == null) && (colliderObj == this.mainBall))
        {
            collisionCounter.initializeCounter();
            gameManager.setCamera(
                    new Camera(
                            mainBall, //object to follow
                            Vector2.ZERO, //follow the center of the object
                            windowController.getWindowDimensions().mult(1.2f), //widen the frame a bit
                            windowController.getWindowDimensions() //share the window dimensions
                    )
            );
        }
    }
}
