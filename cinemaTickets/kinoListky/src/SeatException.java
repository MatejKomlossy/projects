/**
 * Použitia výnimky : pri obsadení miest, ktoré boli záakazníkom zvolené
 * @author Matej Komlóssy
 */
public class SeatException extends Exception{
    
    /**
     * @param message správa ktorú výnimka vypíše
     */
    SeatException(String message){
        super(message);
    }
}
