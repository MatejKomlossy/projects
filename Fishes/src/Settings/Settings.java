package Settings;

public class Settings {

    private static Settings instance = null;

    /**aktualny level*/
    private int level = 1;
    /**maximalna rychlost ryb*/
    private int maxspeed;
    /**interval spawnovania ryb*/
    private int spawnInterval;

    private Settings(){

    }

    public static Settings getInstance()
    {
        if (instance == null){
            instance = new Settings();
        }

        return instance;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxspeed() {
        return maxspeed;
    }

    public int getSpawnInterval() {
        return spawnInterval;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setMaxspeed(int maxspeed) {
        this.maxspeed = maxspeed;
    }

    public void setSpawnInterval(int spawnInterval) {
        this.spawnInterval = spawnInterval;
    }
}
