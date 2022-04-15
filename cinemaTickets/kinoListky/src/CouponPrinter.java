/**
 * 
 * @author Matej Komlóssy
 */
public class CouponPrinter {

    private static final CouponPrinter INSTANCE = new CouponPrinter();
    
    /**
     * 
     * @return vráti inštanciu tejto triedy
     */
    public static CouponPrinter getInstance() { return INSTANCE; }
    
    private CouponPrinter() { }
        
    /**
     * vypíše parametre kupónu
     * @param coupon kupón na vypísanie
     */
    public void print(DiscountCoupon coupon) {
        if (coupon == null) {
            throw new NullPointerException("coupon cannot be null");
        }
        
        System.out.print("id :           ");
        System.out.println(coupon.getId());
        System.out.println("kupon je " +(coupon.getIsValid()?"":"ne")+ "platny   ");
        System.out.print("kod kuponu:    ");
        System.out.println(coupon.getNumber());
        System.out.print("id objednavky: ");
        System.out.println(coupon.getOrderId());
        System.out.print("id zakaznika:  ");
        System.out.println(coupon.getCustomerId());
        System.out.println();
    }
}

