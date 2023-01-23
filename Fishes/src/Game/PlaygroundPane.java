package Game;

import Game.Fishes.GenericFish;
import Game.Fishes.PlayersFish;
import Settings.Settings;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

/**pane kde sa vykresluju ryby*/
public class PlaygroundPane extends Pane implements Component{
    FishMediator mediator;

    /**sirka a vyska*/
    private int w = 1200, h = 750; //kvoli listenerom
    /**zoznam ryb*/
    List<GenericFish> fishes = new ArrayList<>();
    /**hracova ryba*/
    PlayersFish playersFish;  //hrac
    /**pozadie*/
    ImageView background;
    /**referencia na rodicovsky pane*/
    FishesPane parentPane;

    FishesGame fishesGame;


    /**maximalna velkost ryby pri spawne*/
    final static int MAX_FISH_SIZE = 10;
    /**minimalna sírka ryby*/
    final static int MIN_FISH_WIDTH = 70;
    /**minimalna vyska ryby*/
    final static int MIN_FISH_HEIGHT = 35;

    /**
     * nastaví referenciu na rodicovsky pane, vytvorí hracovu rybu a nakreslí pozadie
     * @param parentPane rodicovsky pane
     */
    public PlaygroundPane(FishesPane parentPane) {
        this.parentPane = parentPane;
        fishesGame = getFishesGame();

        playersFish = new PlayersFish(w / 2, h / 2, this);
        setOnMouseMoved(e -> {
            playersFish.setX(e.getX());
            playersFish.setY(e.getY());
        });

        //nakresli pozadie
        Settings settings = Settings.getInstance();
        background = new ImageView(new Image("images/level" + settings.getLevel() + "background.jpg"));
        background.setFitWidth(w);
        background.setFitHeight(h);
        getChildren().add(background);
    }

    /**nakreslí vsetky ryby*/
    public void paint(){
        getChildren().clear();
        //pozadie
        background.setFitWidth(w);
        background.setFitHeight(h);
        getChildren().add(background);

        //hracova ryba paint
        ImageView player = new ImageView(playersFish.getImg());
        player.setX(playersFish.getImageX());
        player.setY(playersFish.getImageY());
        player.setFitWidth(playersFish.getWidth());
        player.setFitHeight(playersFish.getHeight());
        player.setRotationAxis(Rotate.Y_AXIS);
        if(playersFish.isFacingRight()) player.setRotate(180);
        getChildren().add(player);

        //ostatne ryby paint
        for(GenericFish f : fishes){
            if(f.isVisible() == false) continue;

            f.paint(this);
        }
    }

    /**zavola update kazdej zivej ryby*/
    public void updateAll(){
        playersFish.update();
        for(GenericFish f : fishes){
            if(f.isVisible()) f.update();
        }
    }

    /**skontroluje ci niektore ryby do seba narazili*/
    public void checkCollisions(){
        /**player collisions*/
        List<GenericFish> fishesCopy = new ArrayList<>(fishes);
        for(GenericFish f : fishesCopy){
            if(f.isVisible() == false) {  //mrtve zahodim
                fishes.remove(f);
                continue;
            }
            if(areColliding(f, playersFish)){
                if(f.getSize() > playersFish.getSize()){ //ak je hrac mensi
                    try {
                        mediator.notify(this, "gameEnd");
                    } catch (EventException e) {
                        e.printStackTrace();
                    }
                }
                else if(f.getSize() < playersFish.getSize()){  //ak je hrac vacsi
                    feedPlayer(f);
                }
                else{                                 //ak su rovnaki
//                    Random rnd = new Random();
//                    int c = rnd.nextInt(2); //nahodne
//                    if(c == 0){
//                        score /= 2;
//                        endGameScreen();
//                    }
//                    else{
//                        feedPlayer(f);
//                    }
                }
            }
        }

        /**other fish collisions*/
        for (int i = 0; i < fishes.size(); i++) {
            for (int j = i+1; j < fishes.size(); j++) {
                GenericFish f1 = fishes.get(i);
                GenericFish f2 = fishes.get(j);
                if(f1.isVisible() == false || f2.isVisible() == false) continue;
                if(areColliding(f1,f2) == false) continue;
                if(f1.getSize() > f2.getSize()){
                    f2.makeInvisible();
                    f1.increaseSize();
                }
                else if(f2.getSize() > f1.getSize()){
                    f1.makeInvisible();
                    f2.increaseSize();
                }
                else{
//                    Random rnd = new Random();
//                    int c = rnd.nextInt(2);
//                    if(c == 0){
//                        f2.makeInvisible();
//                        f1.increaseSize();
//                    }
//                    else{
//                        f1.makeInvisible();
//                        f2.increaseSize();
//                    }
                }
            }
        }
    }

    /**
     * nakrmi hraca inou rybou, zvacsí jeho velkost, vola sa ked hrac zje inu rybu
     * @param f ryba ktorú hrac zje
     */
    private void feedPlayer(GenericFish f){
        f.makeInvisible();
        playersFish.incEatenFish();
        playersFish.increaseSize();
        //fishesGame.setScore(fishesGame.getScore() + f.getSize() * 2 + 1);
        try {
            mediator.notify(this, "incScore", f.getSize() * 2 + 1);
        } catch (EventException e) {
            e.printStackTrace();
        }
    }

    /**
     * zistí, ci ryby do seba narazili
     * @param f1 prva ryba
     * @param f2 druha ryba
     * @return
     */
    private boolean areColliding(GenericFish f1, GenericFish f2){
            /*return (Math.abs(f1.x - f2.x) <= f1.width/2 + f2.width/2)
                    && (Math.abs(f1.y - f2.y) <= f1.height/2 + f2.height/2);*/
        return Math.sqrt(Math.pow(f1.getX() - f2.getX(),2) + Math.pow(f1.getY() - f2.getY(),2)) <= f1.getHeight()/2 + f2.getHeight()/2;
    }

    public void addFish(GenericFish genericFish){
        this.fishes.add(genericFish);
    }

    /**
     * @return sirka
     */
    public int getW() { return w; }

    /**
     * @return vyska
     */
    public int getH() { return h; }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }

    public FishesPane getParentPane() {
        return parentPane;
    }

    public int getMaxFishSize() {
        return MAX_FISH_SIZE;
    }

    public int getMinFishWidth() {
        return MIN_FISH_WIDTH;
    }

    public int getMinFishHeight() {
        return MIN_FISH_HEIGHT;
    }

    public FishesGame getFishesGame(){
        return parentPane.getFishesGame();
    }

    public void setMediator(FishMediator mediator) {
        this.mediator = mediator;
    }

}