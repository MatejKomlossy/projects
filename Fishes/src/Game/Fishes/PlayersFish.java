package Game.Fishes;

import Game.FishesPane;
import Game.PlaygroundPane;
import javafx.scene.image.Image;

/**hracova ryba*/
public class PlayersFish extends GenericFish { //x,y su to suradnice stredu
    /**predchadzajúca x-ova suradnica*/
    double prevX;
    /**ci je ryba otocena doprava*/
    boolean facingRight;

    /**
     * nastaví parametre ryby
     * @param x x-ova suradnica
     * @param y y-ova suradnica
     * @param parentPane rodicovsky pane
     */
    public PlayersFish(double x, double y, PlaygroundPane parentPane) {
        this.parentPane = parentPane;
        setSize();
        width = parentPane.getMinFishWidth();
        height = parentPane.getMinFishHeight();
        increaseSize();
        this.x = x;
        this.y = y;
        setImg();
    }

    @Override
    public void setImg() {
        img = new Image("images/playersfish.png");
    }

    @Override
    public void setSize() {
        size = 0;
    }

    /**aktualizuje polohu ryby*/
    public void update(){
        if(x > prevX) facingRight = true;  //is facing right
        else if (x < prevX) facingRight = false;
        prevX = x;
    }

    /**zvacsí velkost*/
    public void increaseSize(){
        if(size == FishesPane.getPlayerMaxSize()) return;
        size++;
        width += 25 + size*3;
        height += 12.5 + size*1.5;
    }

    /**
     * @return ci je ryba otocena doprava
     */
    public boolean isFacingRight() {
        return facingRight;
    }

    /**
     * nastaví x-ovú suradnicu
     * @param x x-ova suradnica
     */
    public void setX(double x) {
        if(x + width/2 <= parentPane.getW() && x - width/2 >= 0){ //pouzivam parentPane h,w kvoli listenerom
            this.x = x;
        }
    }

    /**
     * nastaví y-ovu suradnicu
     * @param y y-ova suradnica
     */
    public void setY(double y) {
        if(y + height/2 <= parentPane.getH() - 35 && y - height/2 >= 0){
            this.y = y;
        }
    }
}