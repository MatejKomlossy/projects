
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Scanner;

/**
 * 
 * @author Matej Komlóssy
 */
public class OrderMenu extends Menu{
    @Override
    public void print() {
        System.out.println("*********************************");
        System.out.println("* 1. vytvor objednavku          *");
        System.out.println("* 2. vypis objednavky zakaznika *");
        System.out.println("* 3. vypis udaje objednavky     *");
        System.out.println("* 4. zmaz neplatne objednavky   *");
        System.out.println("* 5. exit                       *");
        System.out.println("*********************************");
    }

    @Override
    public void handle(String option) {
        try {
            switch (option) {
                case "1":   createOrder(); break;
                case "2":   printOrdersOfACustomer(); break;
                case "3":   printInfoAboutAnOrder(); break;
                case "4":   deleteInvalidOrders(); break;
                case "5":   exit(); break;
                default:    System.out.println("Unknown option"); break;
            }
        } catch(SQLException | IOException | CouponException | OrderException e){// | InterruptedException e) {
            System.out.println(e.getMessage());
        } 
    }
    
    /**
     * vytvorí objednávku na predstavenie pre zákazníka
     * @throws IOException
     * @throws SQLException
     * @throws OrderException
     * @throws CouponException 
     */
    public static void createOrder()throws IOException, SQLException, OrderException, CouponException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("zadaj id zakaznika: ");
        int customerId = Integer.parseInt(in.readLine());
        
        Customer cu = Customer.find(customerId);
        if(cu == null){
            throw new OrderException("zakaznik s danym id neexistuje");
        }
        
        System.out.println("zadaj id predstavenia: ");
        int screeningId = Integer.parseInt(in.readLine());

        Screening s = Screening.find(screeningId);
        if(s == null){
            throw new OrderException("predstavenie s danym id neexistuje");
        }
        
        List<TicketType> typyListkov = TicketType.findAll();
        List<Integer> pocty = new ArrayList<>();

        for(TicketType t : typyListkov){
            System.out.println("zadaj pocet listkov typu " + t.getType());
            int pocet = Integer.parseInt(in.readLine());
            pocty.add(pocet);
        }
        
        Order o = OrderOperations.createOrder(cu, s, typyListkov, pocty);
        
        confirmOrder(s, pocty, o);
    }
    
    /**
     * Zákazník si vyberie miesta, na ktoré si chce lístky objednať
     * a potvrdí tým objednávku.
     * Ak chce,môže na objednávku uplatniť svoj zľavový kupón.
     * @param s predstavenie, na ktoré je objednávka
     * @param pocty počty lístkov
     * @param o objednávka, ktorú treba potvrdiť
     * @throws SQLException
     * @throws IOException
     * @throws OrderException
     * @throws CouponException 
     */
    public static void confirmOrder(Screening s, List<Integer> pocty, Order o) throws SQLException, IOException, OrderException, CouponException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        //Set<Integer> freeSeatIds = s.setOfFreeSeats();
        int orderedTicketsCount = pocty.stream().mapToInt(Integer::intValue).sum();
        
        boolean selected = false;
        
        while(selected == false){ //alebo namiesto cyklu znova volat funkciu sa da
            System.out.println("vyberte si miesta v pocte " + orderedTicketsCount+ ". Cisla oddelte medzerou");
            System.out.println(" volne miesta:"); //vypis volne miesta
            Set<Integer> freeSeatIds = s.setOfFreeSeats();
            List temp = new ArrayList(freeSeatIds); //tu
            for (int i = 0; i < temp.size(); i++) {
                if(i % 11 == 0) System.out.println("");
                System.out.print(temp.get(i) + " ");
            }
            System.out.println("");

            String str = in.readLine();   //ziskaj zvolene miesta
            String[] tmp = str.split(" ");
            List<String> tmp2 = Arrays.asList(tmp);
            List<Integer> selectedSeatIds = new ArrayList<>();
            for(String e : tmp2){
                selectedSeatIds.add(Integer.valueOf(e));
            }

            if(orderedTicketsCount != selectedSeatIds.size()){
                //throw new OrderException("pocet zvolenych miest je iny ako pocet objednanych listkov");
                System.out.println("pocet zvolenych miest je iny ako pocet objednanych listkov");
                selected = false;
            }
            else{
                try{  
                    OrderOperations.confirmOrder(selectedSeatIds, s, o);
                    selected = true;

                } catch(SeatException e){ //ked sa medzitym obsadi nejake z miest
                    System.out.println(e.getMessage());
                    selected = false;  //tak sa vyberaju znova
                }
            }
        }
        
        //ci chce kupon pouzit
        System.out.println("ak chcete pouzit kupon, zadajte jeho kod. Ak nie, napiste 'nechcem'");
        Scanner scanner = new Scanner(in);
        if(scanner.hasNextInt()){
            int couponNumber = scanner.nextInt();
            DiscountCoupon dc = DiscountCoupon.findByNumber(couponNumber);
            if(dc == null) throw new CouponException("neexistujuci kupon");
            if(dc.getCustomerId().equals(o.getCustomerId()) == false) throw new CouponException("kupon nepatri danemu zakaznikovi");
            if(dc.getIsValid() == false) throw new CouponException("neplatny kupon");
            
            CouponOperations.useCoupon(dc, o);
            System.out.println("kupon bol pouzity");
        }
        else{
            String answer = scanner.nextLine();
            if(answer.equals("nechcem") == false) throw new CouponException("unknown option");
        }
    }
   
    /**
     * vypíše údaje o objednávkach zadaného zákazníka
     * @throws IOException
     * @throws SQLException 
     */
    public void printOrdersOfACustomer() throws IOException, SQLException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("zadaj id zakaznika: ");
        int customerId = Integer.parseInt(in.readLine());
        
        List<Order> lst = Order.findAllByCustomerId(customerId);
        System.out.println("zakaznik ma objednavky s tymito id: ");
        for(Order o : lst){
            System.out.println(o.getId());
        }
    }
    
    /**
     * vypíše údaje zadanej objednávky
     * @throws IOException
     * @throws SQLException
     * @throws OrderException 
     */
    public void printInfoAboutAnOrder() throws IOException, SQLException, OrderException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("zadaj id objednavky: ");
        int orderId = Integer.parseInt(in.readLine());
        Order order = Order.find(orderId);
        if(order == null){
            throw new OrderException("neexistujuca objednavka");
        }
        
        OrderPrinter.getInstance().print(order);
    }
    
    /**
     * zmaže neplatné objednávky
     * @throws SQLException 
     */
    public void deleteInvalidOrders() throws SQLException{
        OrderOperations.deleteInvalidOrders();
        System.out.println("stare objednavky boli zmazane");
    }
}
