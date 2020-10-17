package denis.snake;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Snake {
    enum DIRECTIONS { NORTH, SOUTH, EAST, WEST};
    DIRECTIONS curDir = DIRECTIONS.EAST;

    String chompSound = getClass().getClassLoader().getResource("chomp.mp3").toString();
    AudioClip chomp = new AudioClip(chompSound);

    String endSound = getClass().getClassLoader().getResource("gameOver.mp3").toString();
    AudioClip end = new AudioClip(endSound);

    public static final int snakeDim = denis.snake.Game.sideLength;
    public static final double snakeArc = 20.0;

    Group g;
    denis.snake.Game game;
    GridPane theGrid;
    Rectangle head = new Rectangle();
    float ball_x, ball_y;
    float factor;
    float dx, dy;
    boolean paused = false;
    boolean gameOver = false;
    AnimationTimer timer;
    Color color = Color.rgb(66, 111, 228);
    Group apples = new Group();
    denis.snake.MenuBoard menuBoard;
    denis.snake.Main main;
    Stage stage;

    void setLevel(int level, Group apples, denis.snake.Game game){
        if(level == 1) this.factor = 10f;
        else if(level == 2) this.factor = 5f;
        else if(level == 3) this.factor = 3f;
        this.dx = snakeDim/factor;
        this.dy = snakeDim/factor;
        this.apples = apples;
        this.game = game;
    }

    Rectangle newPart(){
        Rectangle newRect = new Rectangle();
        Rectangle lastRect = (Rectangle) g.getChildren().get(g.getChildren().size()-1);
        newRect.setX(lastRect.getX());
        newRect.setY(lastRect.getY());
        newRect.setWidth(snakeDim);
        newRect.setHeight(snakeDim);
        newRect.setArcWidth(snakeArc);
        newRect.setArcHeight(snakeArc);
        newRect.setFill(color);
        return newRect;
    }

    public Snake(Stage stage, denis.snake.MenuBoard menuBoard, denis.snake.Main main){
        this.main = main;
        this.stage = stage;
        this.menuBoard = menuBoard;
        ball_x = (denis.snake.Game.xOffset + snakeDim*13+1);
        ball_y = (denis.snake.Game.yOffset + snakeDim*10+1);

        head.setX(ball_x);
        head.setY(ball_y);
        head.setWidth(snakeDim);
        head.setHeight(snakeDim);
        head.setArcWidth(50.0);
        head.setArcHeight(50.0);
        head.setFill(color);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                handle_animation();
            }
        };
        g = new Group(head);
        g.getChildren().add(newPart());
        g.getChildren().add(newPart());
    }

    public void startAnimation(){
        timer.start();
    }

    public void stopAnimation(){
        timer.stop();
    }

    void handle_animation() {
        if(!paused){
            checkNextLevel();
            goDir();
            if (outOfBounds() || collideSelf()) {
                stopAnimation();
                end.play();
                this.main.setScene(stage, denis.snake.Main.SCENES.FINALSCREEN);
            }
            intersectAppleManager();
        }
    }

    void intersectAppleManager(){
        int idx = 0;
        for(Node img: this.apples.getChildren()){
            if(img.intersects(head.getBoundsInLocal())){
                g.getChildren().add(newPart());
                apples.getChildren().remove(idx);
                game.addRandomApple(this.g);
                menuBoard.incrementScore();
                chomp.play();
                break;
            }
            ++idx;
        }
    }

    public void directionSetter(DIRECTIONS newDir){
        if(paused) return;
        switch(newDir){
            case SOUTH:
            case NORTH:
                if(curDir == DIRECTIONS.EAST || curDir == DIRECTIONS.WEST){
                    ball_x = calcOffset(ball_x, denis.snake.Game.xOffset);
                    head.setX(ball_x);
                    curDir = newDir;
                }
                break;
            case EAST:
            case WEST:
                if(curDir == DIRECTIONS.NORTH || curDir == DIRECTIONS.SOUTH){
                    ball_y = calcOffset(ball_y, denis.snake.Game.yOffset);
                    head.setY(ball_y);
                    curDir = newDir;
                }
                break;
        }
    }

    float calcOffset(float val, float offset){
        float tmp = val - offset;
        float result = Math.round(tmp/snakeDim)*snakeDim + offset;
        return result;
    }

    void goDir(){
        for (int i = g.getChildren().size()-1; i >= 1; i--) {
            Rectangle curr = (Rectangle) g.getChildren().get(i);
            Rectangle prev = (Rectangle) g.getChildren().get(i - 1);
            curr.setX((prev.getX()-curr.getX())/factor + curr.getX());
            curr.setY((prev.getY()-curr.getY())/factor + curr.getY());
        }

        switch(curDir){
            case NORTH:
                ball_y -= dy;
                break;
            case SOUTH:
                ball_y += dy;
                break;
            case EAST:
                ball_x += dx;
                break;
            case WEST:
                ball_x -= dx;
                break;
        }
        head.setX(ball_x);
        head.setY(ball_y);
    }

    boolean outOfBounds(){
        switch(curDir){
            case EAST:
                if(head.getX() + denis.snake.Game.sideLength > denis.snake.Game.xOffset + (denis.snake.Game.sideLength * denis.snake.Game.colNum)) return true;
                return false;
            case WEST:
                if(head.getX() < denis.snake.Game.xOffset) return true;
                return false;
            case NORTH:
                if(head.getY() < denis.snake.Game.yOffset) return true;
                return false;
            case SOUTH:
                if(head.getY() + denis.snake.Game.sideLength > denis.snake.Game.yOffset + (denis.snake.Game.sideLength * denis.snake.Game.rowNum)) return true;
                return false;
        }
        return false;
    }

    boolean collideSelf(){
        for(Node child: g.getChildren()){
            Rectangle body = (Rectangle) child;
            switch(curDir){
                case EAST:
                    if(body.contains(head.getX()+ snakeDim, head.getY() + snakeDim/2)) return true;
                    break;
                case WEST:
                    if(body.contains(head.getX(), head.getY() + snakeDim/2)) return true;
                    break;
                case NORTH:
                    if(body.contains(head.getX() + snakeDim/2, head.getY())) return true;
                    break;
                case SOUTH:
                    if(body.contains(head.getX() + snakeDim/2, head.getY()+snakeDim)) return true;
                    break;
            }
        }
        return false;
    }

    public void pauseGame(){
        paused = !paused;
    }

    void checkNextLevel(){
        if(menuBoard.timeLeft == 0){
            game.proceedLevel();
        }
    }
}
