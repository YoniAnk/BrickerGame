package src.gameobjects;

import danogl.GameManager;
import danogl.GameObject;
import danogl.util.Vector2;

/**
 * a class of game object that is responsible for turning off the camera (for camera strategy).
 * it counts the hits of the ball and turn off the camera when needed.
 */
public class CollisionCounter extends GameObject {
    public static final int MAX_HITS_OF_BALL = 4;
    private static int counter;
    private final GameManager gameManager;
    private final Ball ballToFollow;

    /**
     * The constructor of the collision counter.
     * @param ball the ball to check his collisions
     * @param gameManager the game manager of the game (to turn off the camera).
     */
    public CollisionCounter( Ball ball,
                            GameManager gameManager) {
        super(Vector2.ZERO, Vector2.ZERO, null);

        counter = ball.getBallCollisions();
        this.ballToFollow = ball;
        this.gameManager = gameManager;
    }

    /**
     * Function that initialize the counter to the current amount of hits of the ball.
     */
    public void initializeCounter()
    {
        counter = this.ballToFollow.getBallCollisions();
    }

    /**
     * The update function of the game object.
     * it will check every time the hits that the ball got from the moment wi initialized it.
     * @param deltaTime unused
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if ((this.ballToFollow.getBallCollisions() - counter) >= MAX_HITS_OF_BALL &&
                (this.gameManager.getCamera() != null))
        {
            this.gameManager.setCamera(null);
        }

    }
}
