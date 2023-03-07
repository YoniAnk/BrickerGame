package src.factories;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Counter;
import src.BrickerGameManager;
import src.brick_strategies.*;
import src.gameobjects.Ball;

import java.util.Random;

/**
 * A factory that creates a collision strategy for the bricks in the game.
 */
public class CollisionStrategyFactory {

    public static final int INIT_SECONDARY_PADDLE_LIVES = 3;
    public static final int MAX_OPTIONS = 6;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final WindowController windowController;
    private final GameObjectCollection gameObjects;
    private final UserInputListener userInputListener;
    private final Ball mainBall;
    private final BrickerGameManager gameManager;
    private final Counter lifeCounter;

    /**
     * The constructor of the factory, it holds all the necessarily paramets to create all available
     * collisions strategies
     * @param gameObjects the game objects collection of the current game.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                    See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     * @param userInputListener The input listener which waits for user inputs and acts on them.
     * @param mainBall The main ball of the game
     * @param gameManager The game manager (the class that manage the game).
     * @param lifeCounter the life counter of the game (the counter that id responsible for counting life
     *                    that left)
     */
    public CollisionStrategyFactory(GameObjectCollection gameObjects,
                                    ImageReader imageReader,
                                    SoundReader soundReader,
                                    WindowController windowController,
                                    UserInputListener userInputListener,
                                    Ball mainBall,
                                    BrickerGameManager gameManager,
                                    Counter lifeCounter)
    {
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.windowController = windowController;
        this.userInputListener = userInputListener;
        this.mainBall = mainBall;
        this.gameManager = gameManager;
        this.lifeCounter = lifeCounter;
    }

    /**
     * The function that creates the randomized collision strategy, there are two option to create a
     * randomized collision strategy -
     * # if toRandomize is True - the function will randomize a strategy.
     * # if toRandomize is False - the function will assume that a number is already randomized and will
     *                              return the appropriate selection.
     * @param numOfOptions the randomized number (if it was randomized, else any number will be accepted)
     * @param toRandomize a boolean that tell if the function will randomize a number of the number is
     *                    already randomized.
     * @return a randomized collision strategy.
     */
    public CollisionStrategy getRandomStrategy(int numOfOptions, boolean toRandomize)
    {
        // if we want to randomize from factory and do not get a randomized number
        if ((toRandomize) || ((numOfOptions > MAX_OPTIONS || numOfOptions < 0) && !toRandomize)) {
            Random random = new Random();
            numOfOptions = random.nextInt(MAX_OPTIONS);
        }

        CollisionStrategy collisionStrategy = null;

        switch (Strategies.values()[numOfOptions]){

            case ANOTHER_LIFE_STRATEGY:
                collisionStrategy = new AnotherLifeStrategy(this.gameObjects, this.imageReader,
                        this.windowController, this.lifeCounter);
                break;
            case PUCK_STRATEGY:
                collisionStrategy = new PuckStrategy(this.gameObjects, this.imageReader,
                        this.soundReader);
                break;
            case PADDLE_STRATEGY:
                collisionStrategy = new PaddleStrategy(this.gameObjects, this.imageReader,
                        this.windowController, this.userInputListener,
                        BrickerGameManager.MIN_DIST_FROM_EDGE, INIT_SECONDARY_PADDLE_LIVES);
                break;
            case CAMERA_STRATEGY:
                collisionStrategy = new CameraStrategy(this.gameObjects, this.mainBall,
                        this.windowController, this.gameManager);
                break;
            case DOUBLE_STRATEGY:
                collisionStrategy = new DoubleStrategy(this.gameObjects,this);
                break;
            case NORMAL_STRATEGY:
                collisionStrategy = new CollisionStrategy(this.gameObjects);
                break;
        }

        return collisionStrategy;
    }
}
