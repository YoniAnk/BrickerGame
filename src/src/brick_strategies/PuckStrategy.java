package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Ball;

import java.util.Random;

/**
 * A type of collision strategy, this strategy create extra balls when the brick get break.
 */
public class PuckStrategy extends CollisionStrategy{

    public static final String MOCK_BALL_IMAGE_PATH = "assets/mockBall.png";
    private static final String BALL_SOUND_PATH = "assets/blop_cut_silenced.wav";
    public static final float MOCK_BALL_RATIO = (float) 0.25;
    private static final int MOCK_BALL_VELOCITY = 175; //175
    public static final int NUMBER_OF_MOCKS = 1; // changed to 1 so the game won't finish fast
    private final SoundReader soundReader;
    private final ImageReader imageReader;
    private Ball[] balls;

    /**
     * The constructor of the extra balls collision strategy.
     * @param gameObjects the game objects collection of the current game.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                    See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     */
    public PuckStrategy(GameObjectCollection gameObjects, ImageReader imageReader, SoundReader soundReader) {
        super(gameObjects);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
    }

    /**
     * When a brick detects a collision, this method is called and activates the current strategy.
     * It decrements the number of active bricks on the screen and removes the current brick from the screen,
     * and creates more extra balls.
     * @param collidedObj the object that was collided (the brick)
     * @param colliderObj the object that collided with the brick (the ball).
     * @param bricksCounter the brick's counter, shows how much bricks are on the board.
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);

        puckBallsCreator(collidedObj);

    }


    /**
     * The function that creates the balls.
     * @param collidedObj the object that balls will get out of.
     */
    private void puckBallsCreator(GameObject collidedObj) {
        Renderable ballImage = imageReader.readImage(MOCK_BALL_IMAGE_PATH,true);
        Sound collisionSound = soundReader.readSound(BALL_SOUND_PATH);

        Vector2 mockBallSize = new Vector2(collidedObj.getDimensions().x()*MOCK_BALL_RATIO,
                collidedObj.getDimensions().y());
        Vector2 mockBallLeftTop = new Vector2(collidedObj.getTopLeftCorner());

        this.balls = new Ball[NUMBER_OF_MOCKS];

        for (int i = 0; i < NUMBER_OF_MOCKS; i++) {
            this.balls[i] = new Ball (new Vector2(mockBallLeftTop.
                    add(new Vector2(mockBallSize.mult((float)i)))), mockBallSize, ballImage, collisionSound);

            setBallVelocityPlace(this.balls[i]);

            this.gameObjects.addGameObject(this.balls[i], Layer.DEFAULT);
        }
    }


    /**
     * a function that sets the velocity and direction of the balls.
     * @param ball the ball to set his velocity and direction.
     */
    private void setBallVelocityPlace(Ball ball) {

        float ballVelX = MOCK_BALL_VELOCITY;
        float ballVelY = MOCK_BALL_VELOCITY;

        Random random = new Random();
        if (random.nextBoolean())
            ballVelX *= (-1);
        if (random.nextBoolean())
            ballVelY *= (-1);

        ball.setVelocity(new Vector2(ballVelX,ballVelY));
    }
}
