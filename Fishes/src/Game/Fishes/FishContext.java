package Game.Fishes;

import Game.Fishes.Level1.*;
import Game.PlaygroundPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.Random;

public class FishContext extends GenericFish {

    FishState state;
    PlaygroundPane parentPane;

    public FishContext() {

    }

    public FishContext(PlaygroundPane parentPane) {     //init. state moze byt ako parameter.
        this.parentPane = parentPane;
        Random rnd = new Random();        //potom by sa muselo riesit to nahodne vyberanie vo FishesGame
        int randomSize = rnd.nextInt(parentPane.getMaxFishSize());  //0-9
        setInitialState(randomSize);
    }

    private void setInitialState(int randomSize){
        switch (randomSize) {
            case 1 -> state = new Numbered(new Fish2Level1(parentPane, this));
            case 2 -> state = new Numbered(new Fish3Level1(parentPane, this));
            case 3 -> state = new Numbered(new Fish4Level1(parentPane, this));
            case 4 -> state = new Numbered(new Fish5Level1(parentPane, this));
            case 5 -> state = new Numbered(new Fish6Level1(parentPane, this));
            case 6 -> state = new Numbered(new Fish7Level1(parentPane, this));
            case 7 -> state = new Numbered(new Fish8Level1(parentPane, this));
            case 8 -> state = new Numbered(new Fish9Level1(parentPane, this));
            default -> state = new Numbered(new Fish1Level1(parentPane, this));  // 0
        }
    }

    public void changeState(FishState state){
        this.state = state;
        //System.out.println("new size " + state.getSize());
    }

    @Override
    public void setImg() { }

    @Override
    public void setSize() { }

    @Override
    public void update() {
        state.update();
    }

    @Override
    public double getWidth() {
        return state.getWidth();
    }

    @Override
    public double getHeight() {
        return state.getHeight();
    }

    @Override
    public int getSize() {
        return state.getSize();
    }

    @Override
    public boolean isVisible() {
        return state.isVisible();
    }

    @Override
    public Image getImg() {
        return state.getImg();
    }

    @Override
    public double getImageX() {
        return state.getImageX();
    }

    @Override
    public double getImageY() {
        return state.getImageY();
    }

    @Override
    public double getY() {
        return state.getY();
    }

    @Override
    public void increaseSize() {
        state.increaseSize();
    }

    @Override
    public void makeInvisible() {
        state.makeInvisible();
    }

    @Override
    public void incEatenFish() {
        state.incEatenFish();
    }

    @Override
    public int getEatenFish() {
        return state.getEatenFish();
    }

    @Override
    public double getX() {
        return state.getX();
    }

    @Override
    public double getSpeed() {
        return state.getSpeed();
    }

    @Override
    public void setX(double x) {
        state.setX(x);
    }

    @Override
    public void setY(double y) {
        state.setY(y);
    }

    @Override
    public void paint(Pane pane) {
        //super.paint(pane);
        state.paint(pane);
    }
}
