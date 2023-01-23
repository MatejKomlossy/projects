package Game.Fishes;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.Random;

public class Shaky implements FishState{

    FishWithContext wrappee;
    FishContext context;
    Random rnd;

    public Shaky(FishWithContext wrappee) {
        this.wrappee = wrappee;
        rnd = new Random();
    }

    @Override
    public void paint(Pane pane) {
        wrappee.paint(pane);
    }

    @Override
    public void update() { ;
        wrappee.update();
        int i = rnd.nextInt(2);
        if(i == 0) wrappee.setY(wrappee.getY() + wrappee.getSpeed()/2);
        else wrappee.setY(wrappee.getY() - wrappee.getSpeed()/2);
    }

    @Override
    public void setContext(FishContext context) {
        this.context = context;
    }

    @Override
    public void setImg() {
        wrappee.setImg();
    }

    @Override
    public void setSize() {
        wrappee.setSize();
    }

    @Override
    public void increaseSize() {
        wrappee.increaseSize();
    }

    @Override
    public void incWidth() {
        wrappee.incWidth();
    }

    @Override
    public void incHeight() {
        wrappee.incHeight();
    }

    @Override
    public void makeInvisible() {
        wrappee.makeInvisible();
    }

    @Override
    public void incEatenFish() {
        wrappee.incEatenFish();
    }

    @Override
    public double getWidth() {
        return wrappee.getWidth();
    }

    @Override
    public double getHeight() {
        return wrappee.getHeight();
    }

    @Override
    public int getSize() {
        return wrappee.getSize();
    }

    @Override
    public int getEatenFish() {
        return wrappee.getEatenFish();
    }

    @Override
    public boolean isVisible() {
        return wrappee.isVisible();
    }

    @Override
    public Image getImg() {
        return wrappee.getImg();
    }

    @Override
    public double getImageX() {
        return wrappee.getImageX();
    }

    @Override
    public double getImageY() {
        return wrappee.getImageY();
    }

    @Override
    public double getX() {
        return wrappee.getX();
    }

    @Override
    public double getY() {
        return wrappee.getY();
    }

    @Override
    public double getSpeed() {
        return wrappee.getSpeed();
    }

    @Override
    public void setX(double x) {
        wrappee.setX(x);
    }

    @Override
    public void setY(double y) {
        wrappee.setY(y);
    }


}

