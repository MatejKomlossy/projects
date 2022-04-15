/**
 * Použitia výnimky : ak zadané predstavenie neexistuje alebo neexistuje jeho kinosála,
 * alebo pri iných chybách týkajúcich sa predstavení
 * @author Matej Komlóssy
 */
public class ScreeningException extends Exception{
    
    /**
     * 
     * @param message správa ktorú výnimka vypíše
     */
    ScreeningException(String message){
        super(message);
    }
}
