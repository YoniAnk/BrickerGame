package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The class of the Numeric life counter, the text of the lives, part of Bricker game.
 *
 * @author Yoni Ankri
 */
public class NumericLifeCounter extends GameObject {
    public static final String LIVES_LEFT_TEXT = "Lives: ";
    private TextRenderable textRenderable;
    private final Counter livesCounter;


    /**
     * The constructor of the textual representation object of how many strikes are left in the game.
     * @param livesCounter The counter of how many lives are left right now.
     * @param topLeftCorner the top left corner of the position of the text object
     * @param dimensions the size of the text object
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions) {

        super(topLeftCorner, dimensions, null);

        this.livesCounter = livesCounter;
        String livesText = LIVES_LEFT_TEXT + this.livesCounter.value();
        this.textRenderable = new TextRenderable(livesText);
        this.textRenderable.setColor(Color.GREEN);
        this.renderer().setRenderable(textRenderable);
    }

    /**
     * This method is overwritten from GameObject.
     * It sets the string value of the text object to the number of current lives left.
     * @param deltaTime unused
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (this.livesCounter.value() == 2)
        {
            this.textRenderable = new TextRenderable(LIVES_LEFT_TEXT + this.livesCounter.value());
            this.textRenderable.setColor(Color.ORANGE);
            this.renderer().setRenderable(textRenderable);
        }

        else if (this.livesCounter.value() == 1)
        {
            this.textRenderable = new TextRenderable(LIVES_LEFT_TEXT + this.livesCounter.value());
            this.textRenderable.setColor(Color.RED);
            this.renderer().setRenderable(textRenderable);
        }

        else {
            this.textRenderable = new TextRenderable(LIVES_LEFT_TEXT + this.livesCounter.value());
            this.textRenderable.setColor(Color.GREEN);
            this.renderer().setRenderable(textRenderable);
        }

    }
}
