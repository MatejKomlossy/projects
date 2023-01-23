/**
 * Použitia výnimky : ak zadaný film neexistuje alebo sú zadané údaje 
 * týkajúce sa filmu nekorektné
 * @author Matej Komlóssy
 */
public class MovieException extends Exception{
    
    /**
     * 
     * @param message správa ktorú výnimka vypíše
     */
    MovieException(String message){
        super(message);
    }
}
