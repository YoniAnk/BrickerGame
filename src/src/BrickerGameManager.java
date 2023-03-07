package src;


import danogl.components.CoordinateSpace;
import src.brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.factories.CollisionStrategyFactory;
import src.gameobjects.*;

import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * The game Manager of the bricker game.
 */
public class BrickerGameManager extends GameManager{

    /* Game properties */
    public static final String WIN_MESSAGE = "You Win!";
    public static final String LOSE_MESSAGE = "You Lose!";
    public static final String PLAY_AGAIN_QUESTION = " Play again ?";
    public static final String WINDOWS_NAME = "Bricker Game";
    private static final int BOARD_LENGTH = 500; //500
    private static final int BOARD_WIDTH = 700; //700
    public static final int INIT_LIVES = 3;
    public static final int NUM_OF_OPTIONS_FOR_STRATEGIES = 6; //-1 means the factory will randomize a number

    public static final int MAX_LIVES = 4;
    public static final int MIN_DIST_FROM_EDGE = 5;

    /* Paddle properties */
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    public static final int PADDLE_WIDTH = 150;
    public static final int PADDLE_LENGTH = 20;

    /* Ball properties */
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String BALL_SOUND_PATH = "assets/blop_cut_silenced.wav";
    private static final int BALL_VELOCITY = 175; //200
    public static final int BALL_SIZE = 20;

    /* Background properties */
    public static final int WALL_WIDTH = 20;
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";

    /* Bricks properties */
    public static final double BRICKSCREEN_RATIO = 0.4; //0.4
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final int NUM_OF_BRICKS_ROWS = 8; //8
    private static final int NUM_OF_BRICKS_COLS = 7; //7


    /* Graphic Life Counter properties */
    public static final String HEART_IMAGE_PATH = "assets/heart.png";



    /* Numeric Life Counter properties */
    public static final int TEXT_NUMERIC_SIZE = 13;


    /* Parameters */
    private Vector2 windowDimensions;
    private CollisionStrategy collisionStrategy;
    private Counter brickCounter;
    private static Counter livesCounter;
    private WindowController windowController;
    private Ball ball;
    private UserInputListener inputListener;
    private CollisionStrategyFactory collisionStrategyFactory;


    /**
     * This is the constructor of a brick game, which calls its super (GameManager)'s constructor
     * @param windowTitle the title of the window
     * @param windowDimensions a 2d vector representing the height and width of the window
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions){
        super(windowTitle,windowDimensions);
    }

    /**
     * This method initializes a new game. It creates all game objects, sets their values and initial positions and allow the start of a game.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {

        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.windowDimensions = windowController.getWindowDimensions();
        this.windowController = windowController;
        this.inputListener = inputListener;

        this.brickCounter = new Counter(NUM_OF_BRICKS_ROWS*NUM_OF_BRICKS_COLS);
        livesCounter = new Counter(INIT_LIVES);


        ballCreator(imageReader, soundReader);

        this.collisionStrategyFactory = new CollisionStrategyFactory(this.gameObjects(),imageReader,
                soundReader, windowController, inputListener,this.ball,this, livesCounter);

        backgroundCreator(imageReader);
        paddleCreator(imageReader, inputListener);
        bricksCreator(imageReader);
        wallsCreator();
        numericLifeCounterCreator();
        graphicLifeCounterCreator(imageReader,INIT_LIVES);



    }

    /**
     * This method overrides the GameManager update method. It checks for game status,
     * and triggers a new game popup.
     * @param deltaTime used in the super's update method
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        isGameEnded();
    }

    /**
     * The function that creates the graphic life counter object.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *      *       *           See its documentation for help.
     * @param numOfLives the number of init lives.
     */
    private void graphicLifeCounterCreator(ImageReader imageReader, int numOfLives) {

        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH,true);

        Vector2 leftTopCorner = new Vector2(this.windowDimensions.x()*(float) 0.035,
                this.windowDimensions.y()*(float) 0.95);

        GameObject graphicHearts = new GraphicLifeCounter(leftTopCorner, Vector2.ZERO, livesCounter,
                heartImage, gameObjects(), numOfLives);

        gameObjects().addGameObject(graphicHearts,Layer.UI);
    }

    /**
     * The function that creates the numeric life counter object.
     */
    private void numericLifeCounterCreator(){

        Vector2 leftTopCorner = new Vector2(this.windowDimensions.x()*(float) 0.035,
                this.windowDimensions.y()*(float)0.9);

        Vector2 numericLifeCounterDimensions = new Vector2(TEXT_NUMERIC_SIZE,TEXT_NUMERIC_SIZE);

        GameObject numericLifeCounter = new NumericLifeCounter(livesCounter,
                leftTopCorner,numericLifeCounterDimensions);

        gameObjects().addGameObject(numericLifeCounter,Layer.UI);
    }

    /**
     * The function that creates the background of the window (as an object)
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *      *      *                 See its documentation for help.
     */
    private void backgroundCreator(ImageReader imageReader) {

        Renderable backgroundImage = imageReader.readImage(BACKGROUND_IMAGE_PATH,false);
        GameObject background = new GameObject(Vector2.ZERO, this.windowDimensions,backgroundImage);

        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     * The function that creates the walls of the game.
     */
    private void wallsCreator() {

        Vector2 wallsSizes = new Vector2(WALL_WIDTH,this.windowDimensions.y());
        Vector2 ceilingSizes = new Vector2(this.windowDimensions.x(),WALL_WIDTH);

        GameObject ceiling = new GameObject(Vector2.ZERO,ceilingSizes,null);
        GameObject rightWall = new GameObject(Vector2.ZERO,wallsSizes,null);
        GameObject leftWall = new GameObject(Vector2.ZERO,wallsSizes,null);

        rightWall.setCenter(new Vector2(this.windowDimensions.x()-(WALL_WIDTH/(float)2),
                this.windowDimensions.y()/2));
        leftWall.setCenter(new Vector2((WALL_WIDTH/(float)2),this.windowDimensions.y()/2));

        gameObjects().addGameObject(ceiling, Layer.DEFAULT);
        gameObjects().addGameObject(rightWall,Layer.DEFAULT);
        gameObjects().addGameObject(leftWall,Layer.DEFAULT);

    }

    /**
     * The function that creates the bricks if the game.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *      *      *      *                 See its documentation for help.
     */
    private void bricksCreator(ImageReader imageReader){

        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH,false);
        GameObject[] allBricks = new Brick[NUM_OF_BRICKS_ROWS*NUM_OF_BRICKS_COLS];

        float XBrickSize = ((this.windowDimensions.x()-2*WALL_WIDTH) -
                (NUM_OF_BRICKS_COLS+1))/(float) (NUM_OF_BRICKS_COLS);

        float YBrickSize = ((this.windowDimensions.y()-WALL_WIDTH)*(float) BRICKSCREEN_RATIO -
                (NUM_OF_BRICKS_ROWS+1))/(float) (NUM_OF_BRICKS_ROWS);

        Vector2 brickSize = new Vector2(XBrickSize,YBrickSize);

        for (int row = 0; row < NUM_OF_BRICKS_ROWS; row++) {
            for (int cols = 0; cols < NUM_OF_BRICKS_COLS; cols++) {
                allBricks[row*NUM_OF_BRICKS_COLS + cols] = new Brick(new Vector2(WALL_WIDTH +
                            cols*(XBrickSize+1)+1, WALL_WIDTH + row*(YBrickSize+1)+1),brickSize,
                            brickImage,this.collisionStrategyFactory.getRandomStrategy(NUM_OF_OPTIONS_FOR_STRATEGIES,true),this.brickCounter);

                gameObjects().addGameObject(allBricks[row*NUM_OF_BRICKS_COLS + cols], Layer.DEFAULT);
            }
        }
    }

    /**
     * The function that creates the paddle of the game.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *      *      *      *                 See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                            a given key is currently pressed by the user or not. See its
     *                            documentation.
     */
    private void paddleCreator(ImageReader imageReader, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH,false);

        GameObject userPaddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_LENGTH),
                paddleImage, inputListener,this.windowDimensions, MIN_DIST_FROM_EDGE);

        userPaddle.setCenter(new Vector2(this.windowDimensions.x()/2,
                this.windowDimensions.y()*(float) 0.90));

        gameObjects().addGameObject(userPaddle, Layer.DEFAULT);
    }

    /**
     * The function that creates the ball of the game.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *      *      *      *      *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *      *                    disk. See its documentation for help.
     */
    private void ballCreator(ImageReader imageReader, SoundReader soundReader) {

        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH,true);
        Sound collisionSound = soundReader.readSound(BALL_SOUND_PATH);

        this.ball = new Ball(Vector2.ZERO, new Vector2(BALL_SIZE,BALL_SIZE),ballImage, collisionSound);

        setBallVelocityPlace();

        gameObjects().addGameObject(this.ball, Layer.DEFAULT);
    }

    /**
     * The function that sets the ball's velocity and place when the game starts or when it get out of the
     * game's border.
     */
    private void setBallVelocityPlace() {
        ball.setCenter(this.windowDimensions.mult((float) 0.5));

        float ballVelX = BALL_VELOCITY;
        float ballVelY = BALL_VELOCITY;

        Random random = new Random();
        if (random.nextBoolean())
            ballVelX *= (-1);
        if (random.nextBoolean())
            ballVelY *= (-1);

        ball.setVelocity(new Vector2(ballVelX,ballVelY));
    }

    /**
     * function that checks if the game ended, and also is in charge of decrement life when the ball
     * gets out of the window.
     */
    private void isGameEnded() {
        double ballHeight = this.ball.getCenter().y();

        String prompt = "";

        if ((this.brickCounter.value() == 0) || (this.inputListener.isKeyPressed(KeyEvent.VK_W))){
            prompt = WIN_MESSAGE;
        }

        if (ballHeight >= this.windowDimensions.y())
        {
            livesCounter.decrement();
            if (livesCounter.value() <= 0) {
                prompt = LOSE_MESSAGE;
            }
            setBallVelocityPlace();
        }

        if (!prompt.isEmpty()){
            prompt += PLAY_AGAIN_QUESTION;

            if (this.windowController.openYesNoDialog(prompt)) {
                this.windowController.resetGame();
            }
            else{
                this.windowController.closeWindow();
            }
        }
    }

    /**
     * The main driver of the program
     * @param args unused
     */
    public static void main(String[] args) {
        new BrickerGameManager(WINDOWS_NAME, new Vector2(BOARD_WIDTH, BOARD_LENGTH)).run();
    }
}
