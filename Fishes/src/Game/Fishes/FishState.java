package Game.Fishes;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public interface FishState {

    void setContext(FishContext context);

    void paint(Pane pane);

    void update();

    double getWidth();

    double getHeight();

    int getSize();

    boolean isVisible();

    Image getImg();

    void setImg();

    double getImageX();

    double getImageY();

    double getY();

    void increaseSize();

    void makeInvisible();

    void incEatenFish();

    int getEatenFish();

    double getX();

    double getSpeed();

    void setX(double x);

    void setY(double y);

    void setSize();

    void incWidth();

    void incHeight();
}
