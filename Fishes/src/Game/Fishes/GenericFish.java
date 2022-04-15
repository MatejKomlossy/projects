package Game.Fishes;

import Game.PlaygroundPane;
import Settings.Settings;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Random;

public abstract class GenericFish {        //State
    /**velkost*/
    protected int size = 0;
    /**pozícia x,y*/
    protected double x, y;
    /**sírka, vyska*/
    protected double width, height;
    /**rýchlosť*/
    protected double speed;
    /**obrazok ryby*/
    protected Image img;
    /**pocet zjedených ryb*/
    protected int eatenFish = 0; //kolko uz zjedol
    /**ci ma byť ryba viditelna*/
    protected boolean visible = true;
    /**referencia na rodicovský pane*/
    protected PlaygroundPane parentPane;

    public GenericFish(){ }

    /**
     * nastavi parametre ryby
     * @param parentPane referencia na rodicovský pane
     */
    public GenericFish(PlaygroundPane parentPane) {
        this.parentPane = parentPane;

        width = parentPane.getMinFishWidth();
        height = parentPane.getMinFishHeight();

        setSize();
        for (int i = 0; i < size; i++) {
            incWidth();
            incHeight();
        }

        x = -width/2;
        Random rnd = new Random();
        y = rnd.nextInt((int) parentPane.getH());

        Settings settings = Settings.getInstance();
        speed = rnd.nextInt(settings.getMaxspeed())+0.5;
//        img = new Image("images/level" + settings.getLevel() + "ryba" + size + ".png");
        setImg();
    }

    public GenericFish(PlaygroundPane parentPane, double x, double y, double speed) {
        this.parentPane = parentPane;

        width = parentPane.getMinFishWidth();
        height = parentPane.getMinFishHeight();
        this.x = x;
        this.y = y;
        this.speed = speed;

        setSize();
        for (int i = 0; i < size; i++) {
            incWidth();
            incHeight();
        }

        setImg();
    }

    public abstract void setImg();

    public abstract void setSize();

    public void paint(Pane pane){
        ImageView fish = new ImageView(getImg());
        fish.setX(getImageX());
        fish.setY(getImageY());
        fish.setFitWidth(getWidth());
        fish.setFitHeight(getHeight());
        pane.getChildren().add(fish);
    }

    /**aktualizuje polohu ryby*/
    public void update(){
        x += speed;

        if(x >= parentPane.getW() + width/2){ //odplavanie z plochy
            visible = false;
        }
    }

    /**zvacsi velkost ryby*/
    public void increaseSize(){
        if(size == parentPane.getMaxFishSize()+1) return;
        size++;
        incWidth();
        incHeight();
    }

    public void incWidth(){
        width += 25 + size*3;
    }

    public void incHeight(){
        height += 12.5 + size*1.5;
    }

    public void makeInvisible(){
        visible = false;
    }

    public void incEatenFish(){
        eatenFish++;
    }

    /**
     * vrati sirku ryby
     * @return sirka
     */
    public double getWidth() {
        return width;
    }

    /**
     * vrati vysku ryby
     * @return vyska
     */
    public double getHeight() {
        return height;
    }

    /**
     * vrati velkost ryby
     * @return velkost
     */
    public int getSize() { return size; }

    /**
     * vrati pocet zjedenych ryb
     * @return pocet zjedenych ryb
     */
    public int getEatenFish() { return eatenFish; }

    /**
     * @return ci je ryba viditelna
     */
    public boolean isVisible() { return visible; }

    /**
     * @return obrazok ryby
     */
    public Image getImg(){ return img; }

    /**
     * @return x-ovua suradnicu obrazka
     */
    public double getImageX(){
        return x - width/2;
    }

    /**
     * @return y-ova suradnicu obrazka
     */
    public double getImageY(){
        return y - height/2;
    }

    /**
     * @return x-ova suradnica stredu
     */
    public double getX() { return x; }

    /**
     * @return y-ova suradnica stredu
     */
    public double getY() { return y; }

    /**
     * @return rychlost
     */
    public double getSpeed() { return speed; }

    public void setSpeed(double speed) {this.speed = speed;}

    /**
     * nastavi x-ovu suradnicu
     * @param x x-ova suradnica
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * nastavi y-ovu suradnicu
     * @param y y-ova suradnica
     */
    public void setY(double y) {
        this.y = y;
    }


}
