package Game;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FishesPane extends BorderPane implements Component, Subscriber{
    FishMediator mediator;

    /**maximalna velkost hracovej ryby*/
    final static int PLAYER_MAX_SIZE = 10;

    FishesGame fishesGame;  //hra v ktorej je vykresleny tento pane
//    /**pane kde sa vykreslujú ryby*/
//    PlaygroundPane playground;
    /**tu sa vykresluje skore, velkost hraca, highscore, uplynuty cas*/
    HBox top;

    /**bar ktory sa naplna ked hrac rastie*/
    Rectangle sizeBar;
    /**obdlznik znazornujuci velkost hracovej ryby*/
    Rectangle sizeRectangle;
    /**velkost hraca*/
    Label sizeLabel;
    /**skore*/
    Label scoreLabel;
    /**highscore*/
    Label highscoreLabel;
    /**cas*/
    Label timeLabel;

    /**skonstruuje cely pane s vrchnym panelom aj pane-om kde su ryby*/
    public FishesPane(FishesGame fishesGame) {
        this.fishesGame = fishesGame;

        //createCenter();
        createTop();
    }

//    private void createCenter(){
//        /**center*/
//        playground = new PlaygroundPane(this);
//        playground.setPrefWidth(getWidth());
//        playground.setPrefHeight(getHeight());
//        setCenter(playground);
//    }

    private void createTop(){
        /**top*/
        top = new HBox(10);
        //sizeLabel
        sizeLabel = new Label("size");
        //sizebar
        sizeBar = new Rectangle(200,30);
        sizeBar.setStroke(Color.DARKBLUE);
        sizeBar.setStrokeWidth(1);
        sizeBar.setFill(Color.TRANSPARENT);
        sizeRectangle = new Rectangle(0,sizeBar.getHeight());
        sizeRectangle.setFill(Color.CHARTREUSE);
        StackPane stack = new StackPane(sizeBar, sizeRectangle);
        stack.setAlignment(Pos.BASELINE_LEFT);
        //timeLabel
        timeLabel = new Label("time: " + (59));
        //spacer
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        //scoreLabel
        scoreLabel = new Label(" score: " );
        //highscoreLabel
        highscoreLabel = new Label(" highscore: ");
        //style
        top.setStyle("-fx-border-color: #000000;  " +
                "-fx-background-color: #5e6fe0;" +
                "-fx-font: 20px 'Bauhaus 93';");
        top.getChildren().addAll(sizeLabel, stack, timeLabel, spacer, scoreLabel, highscoreLabel);
        setTop(top);
    }

    /**zavola paint pane-u s rybami*/
    public void paint(){
        //playground.playersFish.size++; //test
        try {
            mediator.notify(this, "paintPlayground");
        } catch (EventException e) {
            e.printStackTrace();
        }
    }

    /**aktualizuje vrchny panel*/
    public void update(){
//        if(!highscoreloaded){
//            highscoreLabel.setText("highscore: " + mediator.getHighscore());
//        }
        sizeLabel.setText(" size : " + mediator.getPlayerSize());
        sizeRectangle.setWidth(mediator.getPlayerSize() * sizeBar.getWidth() / PLAYER_MAX_SIZE);
//        timeLabel.setText("time: " + (60 - 1-mediator.getElapsedTime()));
//        scoreLabel.setText(" score: " + mediator.getScore() + " ");
        try {
            mediator.notify(this, "updateFishes");
        } catch (EventException e) {
            e.printStackTrace();
        }
    }

    /**zavola checkCollisions pane-u s rybami*/
    public void checkCollisions(){
        mediator.checkFishCollisions();
    }

    public FishesGame getFishesGame() {
        return fishesGame;
    }

    public static int getPlayerMaxSize() {
        return PLAYER_MAX_SIZE;
    }

    public void setMediator(FishMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void update(String event, int value) {
        if(event.equals("timeChange")){
            timeLabel.setText("time: " + (60 - 1 - value));
        }
        else if(event.equals("scoreChange")){
            scoreLabel.setText(" score: " + value + " ");
        }
        else if(event.equals("highscoreChange")){
            highscoreLabel.setText("highscore: " + value);
        }
    }
}

