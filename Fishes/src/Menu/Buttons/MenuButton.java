package Menu.Buttons;


import javafx.scene.control.Button;

public class MenuButton extends Button {

    /**
     * nastaví dizajn buttonu, text ktory sa na nom zobrazí
     * @param text text ktory sa zobrazí na buttone
     */
    public MenuButton(String text){
        super(text);
        setMaxWidth(1000);
        setMaxHeight(1000);
        setStyle("-fx-border-color: #00007f;  " +
                "-fx-font: 40px 'Bauhaus 93';" +   //Arial Rounded MT Bold //System.out.println(javafx.scene.text.Font.getFamilies());
                "-fx-background-color: #0059ff;" +
                "-fx-cursor: hand;" +
                "-fx-border-radius: 10;"+
                "-fx-background-radius: 10;"+
                "-fx-wrap-text: true;");
        setPrefSize(150,70);
    }
}
