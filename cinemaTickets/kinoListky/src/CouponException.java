/**
 * Použitia výnimky : ak je kupón zadaný používateľom neplatný alebo neexistuje.
 * Tiež keď chce používateľ použiť kupón, ktorý mu nepatrí.
 * @author Matej Komlóssy
 */
public class CouponException extends Exception{

    /**
     * 
     * @param message správa ktorú výnimka vypíše
     */
    public CouponException(String message) {
        super(message);
    }
    
}
