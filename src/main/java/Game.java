package denis.snake;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import java.util.*;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

public class Game {
    Image image = new Image("apple.png", sideLength-2, sideLength-2, true, true);
    public Scene scene;
    Color[] colors = {Color.rgb(170,215,81), Color.rgb(162,209,73), Color.rgb(87,138,52)};
    static int rowNum = 20;
    static int colNum = 35;
    public static final int sideLength = 35;
    Pane root = new Pane();
    public Rectangle bg;
    GridPane grid;
    Group apples = new Group();
    Stage stage;
    denis.snake.MenuBoard menuBoard;
    denis.snake.Snake snake;
    int level;
    denis.snake.Main main;

    public static float xOffset, yOffset;

    public Game(Stage stage, String levelStr, int level, denis.snake.MenuBoard menuBoard, denis.snake.Snake snake, denis.snake.Main main){
        this.stage = stage;
        this.main = main;
        menuBoard.setLevel(level);
        bg = new Rectangle(0, 0, denis.snake.Main.WIDTH, denis.snake.Main.HEIGHT);
        this.level = level;
        bg.setFill(colors[2]);
        createGrid();
        createApples();

        this.menuBoard = menuBoard;
        root.getChildren().add(bg);
        root.getChildren().add(menuBoard.pane);
        root.getChildren().add(grid);
        root.getChildren().add(apples);

        this.snake = snake;
        snake.setLevel(level, apples, this);
        root.getChildren().add(snake.g);

        scene = new Scene(root, denis.snake.Main.WIDTH, denis.snake.Main.HEIGHT);

        snake.startAnimation();

        stage.setTitle(levelStr);
        stage.setScene(scene);
        stage.show();
        keyStroke();
    }

    private void createApples(){
        List<Point2D> l1 = new ArrayList<>();
        l1.add(new Point2D(3, 4));
        l1.add(new Point2D(27, 7));
        l1.add(new Point2D(0, 8));
        l1.add(new Point2D(32, 6));
        l1.add(new Point2D(20, 9));

        List<Point2D> l2 = new ArrayList<>();
        l2.add(new Point2D(21, 3));
        l2.add(new Point2D(18, 5));
        l2.add(new Point2D(1, 11));
        l2.add(new Point2D(12, 12));
        l2.add(new Point2D(19, 19));

        List<Point2D> l3 = new ArrayList<>();
        l3.add(new Point2D(13, 13));
        l3.add(new Point2D(8, 15));
        l3.add(new Point2D(4, 0));
        l3.add(new Point2D(11, 1));
        l3.add(new Point2D(2, 3));

        for(Point2D point: l1){
            ImageView imageView = new ImageView(image);
            Point2D coord = genCoord(point.getX(), point.getY());
            imageView.setX(coord.getX());
            imageView.setY(coord.getY());
            apples.getChildren().add(imageView);
        }

        if(level >= 2){
            for(Point2D point: l2){
                ImageView imageView = new ImageView(image);
                Point2D coord = genCoord(point.getX(), point.getY());
                imageView.setX(coord.getX());
                imageView.setY(coord.getY());
                apples.getChildren().add(imageView);
            }
        }

        if(level >= 3){
            for(Point2D point: l3){
                ImageView imageView = new ImageView(image);
                Point2D coord = genCoord(point.getX(), point.getY());
                imageView.setX(coord.getX());
                imageView.setY(coord.getY());
                apples.getChildren().add(imageView);
            }
        }
    }

    Point2D genCoord(double x, double y){
        x = xOffset + (sideLength*x)+1;
        y = yOffset + (sideLength*y)+1;
        return new Point2D(x, y);
    }

    void addRandomApple(Group snakeBody){
        Random random = new Random();
        float x,y;
        boolean loop = true;
        while(loop){
            x = random.nextInt(colNum);
            y = random.nextInt(rowNum);
            for(Node part: snakeBody.getChildren()){
                if(!part.contains(x, y)){
                    loop = false;
                    ImageView imageView = new ImageView(image);
                    Point2D coord = genCoord(x, y);
                    imageView.setX(coord.getX());
                    imageView.setY(coord.getY());
                    apples.getChildren().add(imageView);
                    break;
                }
            }
        }
    }

    private void createGrid(){
        grid = new GridPane();
        int colorIDX = 0;
        for(int row = 0; row < rowNum; row++){
            for(int col = 0; col < colNum; col++){
                Rectangle rec = new Rectangle();
                rec.setWidth(sideLength);
                rec.setHeight(sideLength);
                rec.setFill(colors[colorIDX]);
                colorIDX = (colorIDX == 0)? 1 : 0;
                GridPane.setRowIndex(rec, row);
                GridPane.setColumnIndex(rec, col);
                grid.getChildren().addAll(rec);
            }
        }
        xOffset = (denis.snake.Main.WIDTH - (sideLength * colNum))/2;
        yOffset = denis.snake.Main.HEIGHT - (sideLength * rowNum) - xOffset;
        grid.setTranslateX(xOffset);
        grid.setTranslateY(yOffset);
    }

    void proceedLevel(){
        if(level == 1){
            this.main.setScene(stage, denis.snake.Main.SCENES.LEVEL2 );
        }
        else if(level == 2){
            this.main.setScene(stage, denis.snake.Main.SCENES.LEVEL3 );
        }
    }

    void keyStroke(){
        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.UP) { snake.directionSetter(denis.snake.Snake.DIRECTIONS.NORTH); }
            else if(event.getCode() == KeyCode.DOWN) { snake.directionSetter(denis.snake.Snake.DIRECTIONS.SOUTH); }
            else if(event.getCode() == KeyCode.LEFT) { snake.directionSetter(denis.snake.Snake.DIRECTIONS.WEST); }
            else if(event.getCode() == KeyCode.RIGHT) { snake.directionSetter(denis.snake.Snake.DIRECTIONS.EAST); }
            else if(event.getCode() == KeyCode.P) { snake.pauseGame(); menuBoard.pauseTimer(); }
            else if(event.getCode() == KeyCode.R) { snake.stopAnimation(); this.main.setScene(stage, denis.snake.Main.SCENES.SPLASHSCREEN); }
            else if(event.getCode() == KeyCode.Q) { snake.stopAnimation(); this.main.setScene(stage, denis.snake.Main.SCENES.FINALSCREEN); }
            else if(event.getCode() == KeyCode.DIGIT1 && level != 1) { this.main.setScene(stage, denis.snake.Main.SCENES.LEVEL1 ); }
            else if(event.getCode() == KeyCode.DIGIT2 && level != 2) { this.main.setScene(stage, denis.snake.Main.SCENES.LEVEL2 ); }
            else if(event.getCode() == KeyCode.DIGIT3 && level != 3) { this.main.setScene(stage, denis.snake.Main.SCENES.LEVEL3 ); }
        });

    }
}
