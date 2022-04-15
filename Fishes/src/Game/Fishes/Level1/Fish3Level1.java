package Game.Fishes.Level1;

import Game.Fishes.FishContext;
import Game.Fishes.FishWithContext;
import Game.Fishes.Numbered;
import Game.Fishes.Shaky;
import Game.PlaygroundPane;
import javafx.scene.image.Image;

public class Fish3Level1 extends FishWithContext {

    public Fish3Level1(PlaygroundPane parentPane, FishContext context) {
        super(parentPane, context);
    }

    public Fish3Level1(PlaygroundPane parentPane, FishContext context, double x, double y, double speed) {
        super(parentPane, context, x, y, speed);
    }


    @Override
    public void setImg() {
        img = new Image("images/level1ryba3.png");
    }

    @Override
    public void setSize() {
        size = 2;
    }


    @Override
    public void increaseSize() {
        Numbered newState = new Numbered(new Shaky(new Fish4Level1(parentPane, context, x, y, speed)));
        context.changeState(newState);
    }
}