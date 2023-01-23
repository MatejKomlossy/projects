import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

/**
 * 
 * @author Matej Komlóssy
 */
public class CouponMenu extends Menu{
    @Override
    public void print() {
        System.out.println("***********************************");
        System.out.println("* 1. vypis zakaznikove kupony     *");
        System.out.println("* 2. vytvor kupon                 *");
        System.out.println("* 3. aktualizuj stav kuponu       *");
        System.out.println("* 4. vymaz kupon                  *");
        System.out.println("* 5. exit                         *");
        System.out.println("***********************************");
    }

    @Override
    public void handle(String option) {
        try {
            switch (option) {
                case "1":   printCustomersCoupons(); break;
                case "2":   createCoupon(); break;
                case "3":   updateCoupon(); break;
                case "4":   deleteCoupon(); break;
                case "5":   exit(); break;
                default:    System.out.println("Unknown option"); break;
            }
        } catch(IOException | SQLException | CouponException | CustomerException e){
            System.out.println(e.getMessage());
        }    
    }
    
    public void printCustomersCoupons() throws IOException, SQLException, CustomerException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("zadajte id zakaznika: ");
        int customerId = Integer.parseInt(in.readLine());
        Customer c = Customer.find(customerId);
        if(c == null) throw new CustomerException("neexistujuci zakaznik");
        
        List<DiscountCoupon> lst = DiscountCoupon.findByCustomer(customerId);
        if(lst.size() == 0) throw new CustomerException("zakaznik nema ziadne kupony.");
        
        System.out.println("zakaznik ma tieto kupony: ");
        for(DiscountCoupon dc : lst){
            CouponPrinter.getInstance().print(dc);
        }
    }
    
    public void createCoupon() throws IOException, CustomerException, SQLException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("zadajte id zakaznika, pre ktoreho bude kupon: ");
        int customerId = Integer.parseInt(in.readLine());
        Customer c = Customer.find(customerId);
        if(c == null) throw new CustomerException("neexistujuci zakaznik");
        
        DiscountCoupon dc = new DiscountCoupon();
        dc.setCustomerId(customerId);
        dc.setIsValid(true);
        dc.setOrderId(null);
        dc.insert();
        
        System.out.println("kupon bol vytvoreny.");
        System.out.println("id kuponu: " + dc.getId());
        System.out.println("cislo kuponu: " + dc.getNumber());
    }
    
    public void updateCoupon() throws IOException, CustomerException, SQLException, CouponException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("zadajte id kuponu: ");
        int couponId = Integer.parseInt(in.readLine());
        DiscountCoupon dc = DiscountCoupon.find(couponId);
        if(dc == null) throw new CouponException("neexistujuci kupon");
        
        CouponPrinter.getInstance().print(dc);
        System.out.println("zadajte novy stav kuponu ('platny' / 'neplatny'):");
        String stav = in.readLine();
        if(stav.equals("platny")){
            if(dc.getIsValid()) throw new CouponException("zadany stav je rovnaky ako aktualny.");
            dc.setIsValid(true);
        }
        else if(stav.equals("neplatny")){
            if(dc.getIsValid() == false) throw new CouponException("zadany stav je rovnaky ako aktualny.");
            dc.setIsValid(false);
        }
        else throw new CouponException("neznamy vstup");
        
        dc.update();
        System.out.println("stav kuponu bol aktualizovany");
    }
    
    public void deleteCoupon() throws IOException, CustomerException, SQLException, CouponException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("zadajte id kuponu: ");
        int couponId = Integer.parseInt(in.readLine());
        DiscountCoupon dc = DiscountCoupon.find(couponId);
        if(dc == null) throw new CouponException("neexistujuci kupon");
        
        dc.delete();
        System.out.println("kupon bol vymazany");
    }
}
    

