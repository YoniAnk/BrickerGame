package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;

/**
 * The class of the Graphic life counter, the images of the heart, part of Bricker game.
 *
 * @author Yoni Ankri
 */
public class GraphicLifeCounter extends GameObject {
    public static final int HEART_SIZE = 15;
    private final Renderable renderable;
    private int numOfLives;
    private final Counter livesCounter;
    private final GameObjectCollection gamesObjectCollection;
    private final GameObject[] hearts;

    private final Vector2 topLeftCorner;

    /**
     * This is the constructor for the graphic lives counter. It creates a 0x0 sized object
     * (to be able to call its update method in game), Creates numOfLives hearts, and adds them to the game.
     * @param topLeftCorner the top left corner of the left most heart
     * @param dimensions the dimension of each heart
     * @param livesCounter the counter which holds current lives count
     * @param renderable the image renderable of the hearts
     * @param gameObjectCollection the collection of all game objects currently in the game
     * @param numOfLives number of current lives
     */
    public GraphicLifeCounter(Vector2 topLeftCorner,
                              Vector2 dimensions,
                              Counter livesCounter,
                              Renderable renderable,
                              GameObjectCollection gameObjectCollection,
                              int numOfLives) {

        super(Vector2.ZERO, Vector2.ZERO, null);

        this.numOfLives = numOfLives;
        this.livesCounter = livesCounter;
        this.gamesObjectCollection = gameObjectCollection;
        this.topLeftCorner = topLeftCorner;
        this.renderable = renderable;

        Vector2 adder = Vector2.ZERO;


        this.hearts = new GameObject[BrickerGameManager.MAX_LIVES];
        for (int i = 0; i < this.numOfLives; i++) {
            hearts[i] = new GameObject(topLeftCorner.add(adder),
                    new Vector2(HEART_SIZE,HEART_SIZE), renderable);
            this.gamesObjectCollection.addGameObject(hearts[i], Layer.UI);
            adder = adder.add(new Vector2(HEART_SIZE*(float)1.15,0));
        }

    }

    /**
     * This method is overwritten from GameObject It removes hearts from the screen
     * if there are more hearts than there are lives left
     * @param deltaTime unused
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (this.livesCounter.value() < this.numOfLives)
        {
            this.numOfLives -= 1;
            this.gamesObjectCollection.removeGameObject(hearts[this.numOfLives],Layer.UI);
        }

        if ((this.livesCounter.value() > this.numOfLives) &&
                (this.numOfLives < BrickerGameManager.MAX_LIVES)) {

            hearts[this.numOfLives] = new GameObject(topLeftCorner.add(
                    new Vector2(this.numOfLives*HEART_SIZE*(float)1.15,0)),
                    new Vector2(HEART_SIZE,HEART_SIZE),
                    this.renderable);
            this.gamesObjectCollection.addGameObject(hearts[this.numOfLives], Layer.UI);
            this.numOfLives +=1 ;
        }

    }
}
