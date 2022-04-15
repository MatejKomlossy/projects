import java.sql.SQLException;
import java.sql.Connection;

/**
 * 
 * @author Matej Komlóssy
 */
public class CouponOperations {
    
    /**
     * nahradí zákazníkové lístky na dané predstavenie kupónom 
     * @param customerId id zákazníka
     * @param screeningId id predstavenia
     * @throws SQLException 
     */
    public static void createCoupon(int customerId, int screeningId) throws SQLException{
        Connection c = DbContext.getConnection();
        //nahradi listky kuponmi
        //zmaz z tickets kde cu.id = cus... , scr.id = scre..
        Ticket.deleteByCustomerAndScreeningId(customerId, screeningId); //predpokladam ze predstavenie je zrusene, uz sa nanho neda objednat

        //vytvor kupon pre zakaznika 
        DiscountCoupon dc = new DiscountCoupon();
        dc.setCustomerId(customerId);
        dc.setIsValid(true);
        dc.setOrderId(null);
        dc.insert();
        System.out.println("dostali ste kupon s cislom " + dc.getNumber());
    }
    
    /**
     * použije daný kupón, čiže zlacní lístky vztvorené na danú objednávku a zneplatní kupón
     * @param dc tento kupón sa použije
     * @param o kupón sa použije na túto objednávku
     * @throws SQLException 
     */
    public static void useCoupon(DiscountCoupon dc, Order o) throws SQLException{
        //zlacni tickets where screening_id = ? and customer_id = ?
        Ticket.cheapen(o.getId());
        dc.setIsValid(false); //uz je neplatny
        dc.setOrderId(o.getId());
        dc.update();
    }
}
