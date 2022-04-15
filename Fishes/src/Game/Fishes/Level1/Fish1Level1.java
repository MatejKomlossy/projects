package Game.Fishes.Level1;

import Game.Fishes.*;
import Game.PlaygroundPane;
import javafx.scene.image.Image;

public class Fish1Level1 extends FishWithContext {

    public Fish1Level1(PlaygroundPane parentPane, FishContext context) {
        super(parentPane, context);
    }

    public Fish1Level1(PlaygroundPane parentPane, FishContext context, double x, double y, double speed) {
        super(parentPane, context, x, y, speed);
    }


    @Override
    public void setImg() {
        img = new Image("images/level1ryba1.png");
    }

    @Override
    public void setSize() {
        size = 0;
    }

    @Override
    public void increaseSize() {
        //super.increaseSize();
        FishState newState = new Numbered(new Shaky(new Fish2Level1(parentPane, context, x, y, speed)));
        context.changeState(newState);
    }

}
