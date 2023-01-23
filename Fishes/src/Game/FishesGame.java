package Game;

import Menu.Menu;
import Menu.MenuBuilder;
import Menu.Buttons.MenuButton;
import Menu.MenuDirector;
import Settings.Settings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FishesGame extends  Publisher implements Component{
    FishMediator mediator;

    /**hlavne javisko*/
    Stage primaryStage;

    /**kolko casu uz ubehlo*/
    int elapsedTime = 0;
    /**zoznam highscore pre kazdy level*/
    HighscoreList highscores = new HighscoreList();
    /**highscore pre aktualny level*/
    Integer highscore;
    /**skore*/
    int score = 0;

    /**hlavny timeline*/
    Timeline animation;
    /**timeline ktory spawnuje ryby*/
    Timeline adder;
    /**timeline ktory stopuje cas*/
    Timeline time;

    public FishesGame(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**nastartuje level*/
    public void startGame(){
        int paneWidth = 1200;
        int paneHeight = 750;
        FishesPane fishesPane = new FishesPane(this);

        PlaygroundPane playgroundPane = new PlaygroundPane(fishesPane);
        setUpMediator(fishesPane, playgroundPane);
        subscribe(fishesPane);
        playgroundPane.setPrefWidth(fishesPane.getWidth());
        playgroundPane.setPrefHeight(fishesPane.getHeight());
        fishesPane.setCenter(playgroundPane);

        Scene playgroundScene = new Scene(fishesPane, paneWidth, paneHeight);

        createAnimationTimeline(fishesPane);
        createAdderTimeline(fishesPane);
        createTimeTimeline();

        setScore(0);
        loadHighscores();

        //listeners
        playgroundScene.widthProperty().addListener(ov->mediator.playgroundPane.setW((int) playgroundScene.getWidth()));
        playgroundScene.heightProperty().addListener(ov -> mediator.playgroundPane.setH((int)playgroundScene.getHeight()));

        primaryStage.setScene(playgroundScene);
        primaryStage.setTitle("ryby");
    }

    private void setUpMediator(FishesPane fishesPane, PlaygroundPane playgroundPane){
        FishMediator fishMediator = new FishMediator(this, fishesPane, playgroundPane);
        setMediator(fishMediator);
        fishesPane.setMediator(fishMediator);
        playgroundPane.setMediator(fishMediator);
    }

    private void createAnimationTimeline(FishesPane fishesPane){
        animation = new Timeline(new KeyFrame(new Duration(10),
            e->{
                try {
                    mediator.notify(this, "animateFishes");
                } catch (EventException eventException) {
                    eventException.printStackTrace();
                }
            }));
        animation.setCycleCount(1000/10 * 60); //60s
        animation.play();
        animation.setOnFinished(e -> {
            //System.out.println("finished");
            setElapsedTime(elapsedTime + 1);
            endGameScreen();
        });
    }

    private void createAdderTimeline(FishesPane fishesPane){
        Settings settings = Settings.getInstance();
        adder = new Timeline(new KeyFrame(new Duration(settings.getSpawnInterval()),  //pridavanie ryb
            e-> {
                try {
                    mediator.notify(this, "addNewFish");
                } catch (EventException eventException) {
                    eventException.printStackTrace();
                }
            }));
        adder.setCycleCount(Timeline.INDEFINITE);
        adder.play();
    }

    private void createTimeTimeline(){
        time = new Timeline(new KeyFrame(new Duration(1000),
            e-> {
                setElapsedTime(elapsedTime + 1);
            }));
        time.setCycleCount(Timeline.INDEFINITE);
        time.play();
    }


    /**skonstruuje obrazovku po skonceni hry*/
    public void endGameScreen(){
        stopTimelines();
        setElapsedTime(0);

        MenuBuilder menuBuilder = new MenuBuilder(primaryStage);
        MenuDirector menuDirector = new MenuDirector(menuBuilder);
        Menu endGameScreen = menuDirector.makeEndGameScreen(score, highscore, this);
        endGameScreen.putOnStage(primaryStage);
    }

    private void stopTimelines(){
        animation.stop();
        adder.stop();
        time.stop();
    }

    /**ulozi highscore pre kazdy level*/
    private void saveHighscores() {
        highscores.saveHighscore(score, Settings.getInstance().getLevel());
    }

    /**nacíta highscore pre kazdy level do zoznamu highscores*/
    private void loadHighscores() {
        highscores.loadHighscores();
        highscore = highscores.getHighscore(Settings.getInstance().getLevel());
        notifySubscribers("highscoreChange", highscore);
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
        notifySubscribers("timeChange", elapsedTime);
    }

    public Integer getHighscore() {
        return highscore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        notifySubscribers("scoreChange", score);
    }

    public void setMediator(FishMediator mediator) {
        this.mediator = mediator;
    }
}
