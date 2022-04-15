package Menu.Buttons;

import Game.FishesGame;
import Settings.Settings;
import javafx.stage.Stage;

public class Level2Button extends MenuButton{

    public Level2Button(String text, Stage primaryStage) {
        super(text);
        Settings settings = Settings.getInstance();
        setOnAction(e2 -> {
            settings.setLevel(2);
            settings.setSpawnInterval(1100);
            settings.setMaxspeed(5);
            FishesGame fishesGame = new FishesGame(primaryStage);
            fishesGame.startGame();
        });
    }
}
