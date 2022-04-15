package Menu;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Menu {

    VBox menuBox = new VBox(20);

    public Menu() {
        menuBox.setMinWidth(300);
    }

    public void putOnStage(Stage primaryStage){
        if(primaryStage == null){
            return;
        }
        Scene menuScene = new Scene(menuBox);
        primaryStage.setScene(menuScene);
    }

    public void addChild(Node e){
        menuBox.getChildren().add(e);
    }

    public VBox getMenuBox() {
        return menuBox;
    }

    public void setMenuBox(VBox menuBox) {
        this.menuBox = menuBox;
    }
}
