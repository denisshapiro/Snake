package denis.snake;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class MenuBoard {
    BorderPane pane;
    public int score;
    Label scoreLabel;
    public static int totalScore;
    Label totalScoreLabel;
    HBox contents;
    Label timerDisplay;
    Color bgColor = Color.rgb(74,117,44);
    Timeline timer;
    int timeLeft;
    int currLevel;
    boolean paused = false;

    public MenuBoard(){
        score = 0;
        totalScore = 0;
        pane = new BorderPane();

        Image apple = new Image("apple.png", denis.snake.Game.sideLength, denis.snake.Game.sideLength, true, true);
        ImageView imageViewApple = new ImageView(apple);
        StackPane imgApple = new StackPane(imageViewApple);
        imgApple.setStyle("-fx-padding: 0 0 3 30;");

        Image trophy = new Image("trophy.png", denis.snake.Game.sideLength, denis.snake.Game.sideLength, true, true);
        ImageView imageViewTrophy = new ImageView(trophy);
        StackPane imgTrophy = new StackPane(imageViewTrophy);
        imgTrophy.setStyle("-fx-padding: 0 0 0 60;");

        scoreLabel = new Label(Integer.toString(score));
        scoreLabel.setStyle("-fx-font: 30 arial; -fx-padding: 0 20 0 5;");
        scoreLabel.setTextFill(Color.WHITE);
        totalScoreLabel = new Label(Integer.toString(totalScore));
        totalScoreLabel.setStyle("-fx-font: 30 arial; -fx-padding: 0 0 0 5;");
        totalScoreLabel.setTextFill(Color.WHITE);

        timerDisplay = new Label("");
        timerDisplay.setStyle("-fx-font: 40 arial; -fx-padding: 0 0 0 390;");
        timerDisplay.setTextFill(Color.WHITE);

        contents = new HBox(imgApple, scoreLabel, imgTrophy, totalScoreLabel, timerDisplay);
        contents.setAlignment(Pos.CENTER_LEFT);
        contents.setBackground(new Background(new BackgroundFill(bgColor, new CornerRadii(20), Insets.EMPTY)));
        contents.setMinHeight(60);
        contents.setMinWidth(1270);
        pane.setStyle("-fx-padding: 5 5 0 5;");
        pane.setTop(contents);
    }

    public void incrementScore(){
        score++;
        totalScore++;
        scoreLabel.setText(Integer.toString(score));
    }

    public void newLevel(){
        score = 0;
        scoreLabel.setText(Integer.toString(score));
        totalScoreLabel.setText(Integer.toString(totalScore));
    }

    void setTimer(){
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e->updateTime()));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    void updateTime(){
        if(timeLeft > 0) timeLeft--;
        timerDisplay.setText(Integer.toString(timeLeft));
    }

    void pauseTimer(){
        if(paused){
            timer.play();
            paused = false;
        }
        else{
            timer.pause();
            paused = true;
        }
    }

    public void setLevel(int newLevel){
        this.currLevel = newLevel;
        if (currLevel == 1 || currLevel == 2){
            newLevel();
            timeLeft = 31;
            setTimer();
        }
        else{
            if(timer != null) {
                timerDisplay.setText("");
                timer.stop();
            }
        }
    }

    void reset(){
        score = 0;
        scoreLabel.setText(Integer.toString(score));
        totalScoreLabel.setText(Integer.toString(totalScore));
        if(timer != null) {
            timerDisplay.setText("");
            timer.stop();
        }
    }
}
