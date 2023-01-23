/**
 * Použitia výnimky : ak zadaná objednávka neexistuje alebo je neplatná,
 * alebo sa objednávka z rôznych dôvodov nedá vytvoriť
 * @author Matej Komlóssy
 */
public class OrderException extends Exception{
    
    /**
     * 
     * @param message správa ktorú výnimka vypíše 
     */
    OrderException(String message){
        super(message);
    }
    
}
