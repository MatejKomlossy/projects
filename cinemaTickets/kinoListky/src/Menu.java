import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * @author Alexander Šimko
 */
public abstract class Menu { //prevzate zo vzoroveho projektu

    private boolean exit;

    public void run() throws IOException {
        exit = false;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (exit == false) {
            System.out.println();
            print();
            System.out.println();

            String line = br.readLine();
            if (line == null) {
                return;
            }

            System.out.println();

            handle(line);
        }
    }

    public void exit() {
        exit = true;
    }

    /**
     * vypíše menu
     */
    public abstract void print();

    /**
     * spustí funckiu podľa zadanej možnosti
     * @param option možnosť vybraná používateľom
     */
    public abstract void handle(String option);	
}