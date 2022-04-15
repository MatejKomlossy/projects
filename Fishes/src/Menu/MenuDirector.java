package Menu;

import Game.FishesGame;

public class MenuDirector {  //TODO make singleton

    Menu mainMenu;
    Menu levelMenu;
    private MenuBuilder builder;

    public MenuDirector(MenuBuilder builder) {
        this.builder = builder;
    }

    public Menu makeMainMenu(){
        if(mainMenu != null){
            return mainMenu;
        }
        builder.reset();
        builder.addPlayButton();
        builder.addExitButton();
        //TODO prirad product do mainMenu, nizsie to iste
        return builder.getProduct();
    }

    public Menu makeLevelMenu(){
        if(levelMenu != null){
            return levelMenu;
        }
        builder.reset();
        builder.addLevel1Button();
        builder.addLevel2Button();
        builder.addLevel3Button();
        builder.addMainMenuButton();
        return builder.getProduct();
    }

    public Menu makeEndGameScreen(int score, int highscore, FishesGame game){
        builder.reset();
        builder.addScoreText(score);
        if(score > highscore){
            builder.addNewHighscoreText();
        }
        builder.addLevelMenuButton();
        builder.addRestartLevelButton(game);
        builder.addExitButton();
        return builder.getProduct();
    }

    public void setBuilder(MenuBuilder builder){
        this.builder = builder;
    }
}
