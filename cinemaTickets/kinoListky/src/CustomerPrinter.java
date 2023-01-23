/**
 * 
 * @author Matej Komlóssy
 */
public class CustomerPrinter {
    private static final CustomerPrinter INSTANCE = new CustomerPrinter();
    
    /**
     * 
     * @return vráti inštanciu tejto triedy
     */
    public static CustomerPrinter getInstance() { return INSTANCE; }
    
    private CustomerPrinter() { }
        
    /**
     * vypíše údaje zákazníka
     * @param customer zákazník na vypísanie
     */
    public void print(Customer customer) {
        if (customer == null) {
            throw new NullPointerException("customer cannot be null");
        }
        
        System.out.print("id :          ");
        System.out.println(customer.getId());
        System.out.print("first name:   ");
        System.out.println(customer.getFirstName());
        System.out.print("last name:    ");
        System.out.println(customer.getLastName());
        System.out.print("email:        ");
        System.out.println(customer.getEmail());
        System.out.print("age:          ");
        System.out.println(customer.getAge());
        System.out.println();
    }
}
