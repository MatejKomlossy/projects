package Menu.Buttons;

import Game.FishesGame;
import Settings.Settings;
import javafx.stage.Stage;

public class Level1Button extends MenuButton {

    public Level1Button(String text, Stage primaryStage) {
        super(text);
        Settings settings = Settings.getInstance();
        setOnAction(e2 -> {
            settings.setLevel(1);
            settings.setSpawnInterval(1500);
            settings.setMaxspeed(3);
            FishesGame fishesGame = new FishesGame(primaryStage);
            fishesGame.startGame();
        });
    }
}
