import Menu.Menu;
import Menu.MenuBuilder;
import Menu.MenuDirector;
import javafx.application.Application;
import javafx.stage.Stage;

public class FishesMain extends Application {

    Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        MenuBuilder menuBuilder = new MenuBuilder(primaryStage);
        MenuDirector menuDirector = new MenuDirector(menuBuilder);
        Menu mainMenu = menuDirector.makeMainMenu();
        mainMenu.putOnStage(primaryStage);

        primaryStage.show();
    }
}
