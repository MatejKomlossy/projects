package Menu.Buttons;


import javafx.application.Platform;

public class ExitButton extends MenuButton {


    public ExitButton(String text) {
        super(text);
        setOnAction(e -> Platform.exit());
    }

}
