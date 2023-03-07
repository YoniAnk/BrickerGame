package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;
import src.factories.CollisionStrategyFactory;

import java.util.Random;

/**
 * A type of collision strategy, this strategy deal with double option of strategies when the brick get break
 * every double strategy can end up with 3 power ups.
 */
public class DoubleStrategy extends CollisionStrategy{

    public static final int MAX_POWERUP = 3; //3
    public static final int OPTIONS_WITHOUT_NORMAL_STRATEGY = 5;
    public static final int OPTIONS_WITHOUT_NORMAL_AND_DOUBLE = 4;
    public static final int INIT_MORE_STRATEGIES_TO_MAKE = 2;
    private static final int OPTIONS_WITHOUT_DOUBLE_STRATEGY = 5;
    public static final int INIT_DOUBLES_MADE = 1;
    private int curStrategies;
    private final CollisionStrategy[] collisionStrategies;
    private final CollisionStrategyFactory strategyFactory;
    private final Random random;
    private final Counter doubleCounter;
    private int moreStrategiesToMake;

    /**
     * The constructor of the Double collision strategy
     * @param gameObjects the game objects collection of the current game.
     * @param strategyFactory a collision strategy factory that created other collisions strategies
     *                                 for the double collision strategy.
     */
    public DoubleStrategy(GameObjectCollection gameObjects,
                          CollisionStrategyFactory strategyFactory) {
        super(gameObjects);

        this.collisionStrategies = new CollisionStrategy[MAX_POWERUP];
        this.curStrategies = 0;
        this.strategyFactory = strategyFactory;
        this.random = new Random();
        this.doubleCounter = new Counter(INIT_DOUBLES_MADE);
        this.moreStrategiesToMake = INIT_MORE_STRATEGIES_TO_MAKE;
    }

    /**
     *  When a brick detects a collision, this method is called and activates the current strategy.
     *  It decrements the number of active bricks on the screen and removes the current brick from the screen,
     *  and generate the double strategy and turn on the strategies.
     * @param collidedObj the object that was collided (the brick)
     * @param colliderObj the object that collided with the brick (the ball).
     * @param bricksCounter the brick's counter, shows how much bricks are on the board.
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);

        setCollisionStrategies();

        this.curStrategies = strategiesAmount();

        for (int i = 0; i < this.curStrategies; i++)
        {
            this.collisionStrategies[i].onCollision(collidedObj,colliderObj,bricksCounter);
        }

    }

    /**
     * A function that randomize a number without the option to get a double strategy
     * @return a number that represent an option without the option to get a double strategy.
     */
    private int randomizeNumberWithoutDouble() {
        int number = this.random.nextInt(OPTIONS_WITHOUT_DOUBLE_STRATEGY);
        if (number == 4) {
            number = 5;
        }
        return number;
    }

    /**
     * A function that creates the double collision strategies.
     * It can make a maximum of 3 collision strategies and one of the strategies each time will surly be
     * one collision strategy that is not a normal one (special powers)
     */
    private void setCollisionStrategies()
    {
        this.curStrategies = 0;
        int strategyRandom = 0;
        while (this.moreStrategiesToMake > 0)
        {
            if ((this.doubleCounter.value() + 1 < MAX_POWERUP) && (this.moreStrategiesToMake > 1))
            {// The 1/5 probability
                strategyRandom = random.nextInt(OPTIONS_WITHOUT_NORMAL_STRATEGY);
                this.moreStrategiesToMake -=1;
                if (strategyRandom == Strategies.DOUBLE_STRATEGY.ordinal())
                {
                    this.doubleCounter.increment();
                    this.moreStrategiesToMake +=2;
                }
                else {
                    this.collisionStrategies[this.curStrategies] =
                            this.strategyFactory.getRandomStrategy(strategyRandom,false);
                    this.curStrategies+=1;
                    strategyRandom = random.nextInt(CollisionStrategyFactory.MAX_OPTIONS);
                    this.moreStrategiesToMake -=1;// The 1/6 probability
                    if (strategyRandom == Strategies.DOUBLE_STRATEGY.ordinal())
                    {
                        this.doubleCounter.increment();
                        this.moreStrategiesToMake +=2;
                        continue;
                    }
                    this.collisionStrategies[this.curStrategies] =
                            this.strategyFactory.getRandomStrategy(strategyRandom,false);
                    this.curStrategies+=1;
                }
            }
            else // In the case where we can't get more doubles strategies
            {
                this.collisionStrategies[this.curStrategies] =
                        this.strategyFactory.getRandomStrategy(randomizeNumberWithoutDouble(), false);
                this.moreStrategiesToMake-=1;
                this.curStrategies+=1;
            }
        }
    }

    /**
     * A function that checks how many collisions were randomized and return this amount
     * @return the number of collisions strategies that were randomized
     */
    private int strategiesAmount()
    {
        for (int i = 0; i < this.collisionStrategies.length; i++) {
            if (this.collisionStrategies[i] == null)
            {
                return i;
            }
        }
        return this.collisionStrategies.length;
    }
}
