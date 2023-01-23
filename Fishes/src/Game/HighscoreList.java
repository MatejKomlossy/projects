package Game;

import Settings.Settings;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HighscoreList {
    List<Integer> highscores = new ArrayList<>();
    String fileName = "highscores.txt";

    /**ulozi highscore pre kazdy level*/
    public void saveHighscore(int highscore, int level) {
        int index = level - 1;
        while(highscores.size()-1 < index){
            highscores.add(0);
        }
        highscores.set(index, highscore);
        try (BufferedWriter br = new BufferedWriter(new FileWriter(fileName))) {
            for(Integer h : highscores){
                br.write(h.toString());
                br.newLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**nacíta highscore pre kazdy level do zoznamu highscores*/
    public void loadHighscores() {
        highscores.clear();
        Settings settings = Settings.getInstance();
        try (Scanner sc = new Scanner(new FileReader(fileName))) {
            while(sc.hasNextLine()) {
                highscores.add(Integer.valueOf(sc.nextLine().strip()));
            }
            //highscore = highscores.get(settings.getLevel()-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getHighscore(int level){
        int index = level - 1;
        if(highscores.size()-1 < index) return 0;
        return highscores.get(index);
    }
}
