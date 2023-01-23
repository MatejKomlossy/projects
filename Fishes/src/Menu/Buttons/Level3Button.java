package Menu.Buttons;

import Game.FishesGame;
import Settings.Settings;
import javafx.stage.Stage;

public class Level3Button extends MenuButton{

    public Level3Button(String text, Stage primaryStage) {
        super(text);
        Settings settings = Settings.getInstance();
        setOnAction(e2 -> {
            settings.setLevel(3);
            settings.setSpawnInterval(700);
            settings.setMaxspeed(8);
            FishesGame fishesGame = new FishesGame(primaryStage);
            fishesGame.startGame();
        });
    }
}
