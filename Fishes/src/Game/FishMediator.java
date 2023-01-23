package Game;

import Game.Fishes.FishContext;

public class FishMediator implements Mediator {

    FishesGame fishesGame;
    FishesPane fishesPane;
    PlaygroundPane playgroundPane;

    public FishMediator(FishesGame fishesGame, FishesPane fishesPane, PlaygroundPane playgroundPane) {
        this.fishesGame = fishesGame;
        this.fishesPane = fishesPane;
        this.playgroundPane = playgroundPane;
    }

    public void increaseScore(int value){
        fishesGame.setScore(fishesGame.getScore() + value);
    }

    public void createEndGameScreen(){
        fishesGame.endGameScreen();
    }

    public int getPlayerSize(){
        return playgroundPane.playersFish.getSize();
    }

    public void paintPlayground(){
        playgroundPane.paint();
    }

    public void updateFishes(){
        playgroundPane.updateAll();
    }

    public void addNewFish(){
        playgroundPane.addFish(new FishContext(playgroundPane));
    }

    public void checkFishCollisions(){
        playgroundPane.checkCollisions();
    }

    //tieto 3 observer?  fishesPane observes fishesGame
//    public int getElapsedTime(){
//        return fishesGame.getElapsedTime();
//    }
//
//    public int getScore(){
//        return fishesGame.getScore();
//    }
//
//    public int getHighscore(){
//        return fishesGame.getHighscore();
//    }

    public void animateFishes(){
        fishesPane.paint();
        fishesPane.checkCollisions();
        fishesPane.update();
    }

    @Override
    public <T extends Component> void notify(T sender, String event) throws EventException {
        if(sender == playgroundPane && event.equals("gameEnd")){
            createEndGameScreen();
        }

        else if(sender == fishesPane) {
            if (event.equals("updateFishes")) {
                updateFishes();
            } else if (event.equals("paintPlayground")) {
                paintPlayground();
            }
            else if(event.equals("checkFishCollisions")){
                checkFishCollisions();
            }
            else{
                throw new EventException("unknown combination of sender and event name");
            }
        }

        else if(sender == fishesGame){
            if(event.equals("animateFishes")){
                animateFishes();
            }
            else if(event.equals("addNewFish")){
                addNewFish();
            }
            else{
                throw new EventException("unknown combination of sender and event name");
            }
        }
    }

    public <T extends Component> void notify(T sender, String event, int value) throws EventException {
        if(sender == playgroundPane && event.equals("incScore")){
            increaseScore(value);
        }
        else{
            throw new EventException("unknown combination of sender and event name");
        }
    }
}
