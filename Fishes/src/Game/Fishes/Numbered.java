package Game.Fishes;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Numbered implements FishState{

    FishState wrappee;
    Text number;
    Ellipse ellipse;
    FishContext context;
    double radius = 10;

    public Numbered(FishState wrappee) {
        this.wrappee = wrappee;
        number = new Text(wrappee.getSize() + 1 + "");
        number.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        ellipse = new Ellipse(radius, radius);
        ellipse.setFill(Color.MAGENTA);
    }

    @Override
    public void paint(Pane pane) {
        wrappee.paint(pane);
        pane.getChildren().addAll(ellipse, number);
    }

    @Override
    public void update() {
        wrappee.update();
        updateNumber();
    }

    private void updateNumber() {
        number.setText(wrappee.getSize() + "");
        number.setX(wrappee.getX() - radius/2 - 3);
        number.setY(wrappee.getY() + radius + 2);
        ellipse.setCenterX(wrappee.getX());
        ellipse.setCenterY(wrappee.getY() + 5);
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
