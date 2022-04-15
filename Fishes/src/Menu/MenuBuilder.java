package Menu;

import Game.FishesGame;
import Menu.Buttons.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class MenuBuilder implements Builder{

    private Menu menu;
    private Stage primaryStage;

    public MenuBuilder(Stage primaryStage) {
        this.primaryStage = primaryStage;
        reset();
    }

    public void reset(){
        this.menu = new Menu();
    }

    public void addPlayButton(){
        MenuButton play = new MenuButton("");
        ImageView imageView = new ImageView(new Image("images/play.png"));
        imageView.setFitWidth(140);
        imageView.setFitHeight(120);
        play.setGraphic(imageView);
        play.setOnAction(e -> {
            MenuDirector menuDirector = new MenuDirector(this);
            Menu levelMenu = menuDirector.makeLevelMenu();
            levelMenu.putOnStage(primaryStage);
        });
        menu.addChild(play);
    }

    public void addExitButton(){
        menu.addChild(new ExitButton("exit"));
    }

    public void addLevel1Button(){
        menu.addChild(new Level1Button("level 1", primaryStage));
    }

    public void addLevel2Button(){
        menu.addChild(new Level2Button("level 2", primaryStage));
    }

    public void addLevel3Button(){
        menu.addChild(new Level3Button("level 3", primaryStage));
    }

    public void addMainMenuButton(){
        MenuButton back = new MenuButton("Main Menu");
        back.setOnAction(e -> {
            MenuDirector menuDirector = new MenuDirector(this);
            Menu mainMenu = menuDirector.makeMainMenu();
            mainMenu.putOnStage(primaryStage);
        });
        menu.addChild(back);
    }

    public void addLevelMenuButton(){
        MenuButton backToMenu = new MenuButton("Choose level");
        backToMenu.setPrefSize(400,70);
        backToMenu.setOnAction(e -> {
            MenuDirector menuDirector = new MenuDirector(this);
            Menu levelMenu = menuDirector.makeLevelMenu();
            levelMenu.putOnStage(primaryStage);
        });
        menu.addChild(backToMenu);
    }

    public void addRestartLevelButton(FishesGame game){
        MenuButton restartLevel = new MenuButton("Restart level");
        restartLevel.setPrefSize(400,70);
        restartLevel.setOnAction(e -> game.startGame());
        menu.addChild(restartLevel);
    }

    public void addScoreText(int score){
        Text scoreText = new Text("score : " + score);
        scoreText.setFont(new Font("Bauhaus 93",50));
        menu.addChild(scoreText);
    }

    public void addNewHighscoreText(){
        Text newHighscoreText = new Text("new highscore!");
        newHighscoreText.setFont(new Font("Bauhaus 93",50));
        menu.addChild(newHighscoreText);
    }

    public Menu getProduct(){
        Menu product = menu;
        reset();
        return product;
    }
}
