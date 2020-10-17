package denis.snake;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Main extends Application {
    public static final int HEIGHT = 800;
    public static final int WIDTH = 1280;
    enum SCENES { SPLASHSCREEN, LEVEL1, LEVEL2, LEVEL3, FINALSCREEN};
    Scene splashScreen, level1, level2, level3, finalScreen;
    Button goLevel1, goLevel2, goLevel3;
    denis.snake.MenuBoard menuBoard;
    denis.snake.Snake snake;
    final int BTN_WIDTH = 100;
    final int BTN_HEIGHT = 100;

    @Override
    public void start(Stage stage) {
        stage.setMaxHeight(HEIGHT);
        stage.setMaxWidth(WIDTH);
        stage.setResizable(false);
        this.menuBoard = new denis.snake.MenuBoard();

        setScene(stage, SCENES.SPLASHSCREEN);
        stage.show();
    }

    void makeSplashScreen(Stage stage){
        goLevel1 = new Button("Level 1");
        goLevel2 = new Button("Level 2");
        goLevel3 = new Button("Level 3");

        goLevel1.setStyle("-fx-font: 20 arial;");
        goLevel2.setStyle("-fx-font: 20 arial;");
        goLevel3.setStyle("-fx-font: 20 arial;");

        goLevel1.setMinSize(BTN_WIDTH,BTN_HEIGHT);
        goLevel2.setMinSize(BTN_WIDTH,BTN_HEIGHT);
        goLevel3.setMinSize(BTN_WIDTH,BTN_HEIGHT);

        HBox buttons = new HBox(goLevel1, goLevel2, goLevel3);
        buttons.setPadding(new Insets(30));
        buttons.setSpacing(20);
        buttons.setAlignment(Pos.CENTER);

        Label title = new Label("Snake");
        title.setStyle("-fx-font: 30 arial;");
        Label details = new Label("Denis Shapiro | 20784758");
        details.setStyle("-fx-font: 15 arial;");
        Label instructions = new Label("Arrow keys for directions\nP - Pause\nR - Reset\n1 - Level 1\n2 - Level 2\n3 - Level 3\nQ - Quit and View Score ");
        instructions.setStyle("-fx-font: 20 arial;");

        VBox content = new VBox(title, details, instructions, buttons);
        content.setPadding(new Insets(10));
        content.setSpacing(10);
        content.setAlignment(Pos.CENTER);
        content.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        splashScreen = new Scene(content, WIDTH, HEIGHT);

        splashScreen.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.DIGIT1) { setScene(stage, SCENES.LEVEL1); }
            else if(event.getCode() == KeyCode.DIGIT2) { setScene(stage, SCENES.LEVEL2); }
            else if(event.getCode() == KeyCode.DIGIT3) { setScene(stage, SCENES.LEVEL3); }
        });
        goLevel1.setOnAction(event -> { setScene(stage, SCENES.LEVEL1); });
        goLevel2.setOnAction(event -> { setScene(stage, SCENES.LEVEL2); });
        goLevel3.setOnAction(event -> { setScene(stage, SCENES.LEVEL3); });
    }

    void makeFinalScreen(Stage stage){
        Label gameOver = new Label("Game Over");
        Label score = new Label("Total Score: " + Integer.toString(denis.snake.MenuBoard.totalScore));
        Button goSplash = new Button("Play Again");
        goSplash.setMinSize(BTN_WIDTH*2,BTN_HEIGHT);
        goSplash.setOnAction(event -> { setScene(stage, SCENES.SPLASHSCREEN); });
        goSplash.setStyle("-fx-font: 20 arial;");

        gameOver.setStyle("-fx-font: 30 arial;");
        score.setStyle("-fx-font: 25 arial;");
        VBox content = new VBox(gameOver, score, goSplash);
        content.setAlignment(Pos.CENTER);
        content.setSpacing(20);
        content.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        finalScreen = new Scene(content, WIDTH, HEIGHT);
    }

    void setScene(Stage stage, SCENES scene){
        switch(scene) {
            case SPLASHSCREEN:
                makeSplashScreen(stage);
                this.menuBoard.reset();
                denis.snake.MenuBoard.totalScore = 0;
                this.snake = new denis.snake.Snake(stage, this.menuBoard, this);
                stage.setTitle("Snake");
                stage.setScene(splashScreen);
                break;
            case LEVEL1:
                this.menuBoard.reset();
                denis.snake.Game lvl1 = new denis.snake.Game(stage, "Level 1", 1, this.menuBoard, snake, this);
                level1 = lvl1.scene;
                stage.setScene(level1);
                break;
            case LEVEL2:
                this.menuBoard.reset();
                denis.snake.Game lvl2 = new denis.snake.Game(stage, "Level 2", 2, this.menuBoard, snake, this);
                level2 = lvl2.scene;
                stage.setScene(level2);
                break;
            case LEVEL3:
                this.menuBoard.reset();
                denis.snake.Game lvl3 = new denis.snake.Game(stage, "Level 3", 3, this.menuBoard, snake, this);
                level3 = lvl3.scene;
                stage.setScene(level3);
                break;
            case FINALSCREEN:
                makeFinalScreen(stage);
                stage.setTitle("Snake");
                stage.setScene(finalScreen);
                break;
        }
    }
}