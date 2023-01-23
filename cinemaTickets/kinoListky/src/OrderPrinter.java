/**
 * 
 * @author Matej Komlóssy
 */

public class OrderPrinter {
    private static final OrderPrinter INSTANCE = new OrderPrinter();
    
    /**
    * 
    * @return vráti inštanciu tejto triedy
    */
    public static OrderPrinter getInstance() { return INSTANCE; }
    
    private OrderPrinter() { }
        
    /**
     * vypíše údaje objednávky
     * @param order objednávka na vypísanie
     */
    public void print(Order order) {
        if (order == null) {
            throw new NullPointerException("order cannot be null");
        }
        
        System.out.print("id :          ");
        System.out.println(order.getId());
        System.out.print("created at:   ");
        System.out.println(order.getCreatedAt());
        System.out.print("is confirmed: ");
        System.out.println(order.getIsConfirmed());
        System.out.print("screening id: ");
        System.out.println(order.getScreeningId());
        System.out.print("customer id:  ");
        System.out.println(order.getCustomerId());
        System.out.println();
    }
}
