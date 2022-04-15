/**
 * Použitia výnimky : keď zadaný zákazník neexistuje, keď sú zadané údaje
 * týkajúce sa zákazníka v zlom tvare
 * @author Matej Komlóssy
 */
public class CustomerException extends Exception{
    
     /**
     * 
     * @param message správa ktorú výnimka vypíše 
     */
    public CustomerException(String message) {
        super(message);
    }
    
}
