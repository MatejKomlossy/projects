import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.Set;

/**
 * 
 * @author Matej Komlóssy
 */
public class ScreeningMenu extends Menu{
    @Override
    public void print() {
        System.out.println("***********************************");
        System.out.println("* 1. vypis udaje predstavenia     *");
        System.out.println("* 2. vytvor predstavenie pre film *");
        System.out.println("* 3. vymaz predstavenie           *");
        System.out.println("* 4. kompenzacia predstavenia     *");
        System.out.println("* 5. exit                         *");
        System.out.println("***********************************");
    }

    @Override
    public void handle(String option) {
        try {
            switch (option) {
                case "1":   printScreeningInfo(); break;
                case "2":   createScreeningForAMovie(); break;
                case "3":   deleteScreening(); break;
                case "4":   compensateAScreening(); break;
                case "5":   exit(); break;
                default:    System.out.println("Unknown option"); break;
            }
        } catch(IOException | SQLException | CustomerException | OrderException | CouponException | ScreeningException | MovieException e){// | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * vypíše údaje zadaného predstavenia
     * @throws SQLException
     * @throws ScreeningException
     * @throws IOException 
     */
    public void printScreeningInfo() throws SQLException, ScreeningException, IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("zadajte id predstavenia: ");
        Integer screeningId = Integer.parseInt(in.readLine());
        Screening s = Screening.find(screeningId);
        if(s == null) throw new ScreeningException("neexistujuce predstavenie");
        
        ScreeningPrinter.getInstance().print(s);
    }
    
    /**
     * vytvorí predstavenie pre zadaný film
     * @throws IOException
     * @throws SQLException
     * @throws MovieException
     * @throws ScreeningException 
     */
    public void createScreeningForAMovie() throws IOException, SQLException, MovieException, ScreeningException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("zadajte id filmu: ");
        Integer movieId = Integer.parseInt(in.readLine());
        Movie m = Movie.find(movieId);
        if(m == null) throw new MovieException("neexistujuci film");
        
        System.out.println("zadajte id kino saly:");
        Integer srId = Integer.parseInt(in.readLine());
        ScreeningRoom sr = ScreeningRoom.find(srId);
        if(sr == null) throw new ScreeningException("neexistujuca kinosala");
        
        Screening s = new Screening();
        s.setMovieId(movieId);
        s.setScreeningRoomId(srId);
        
        s.insert();
        System.out.println("predstavenie bolo vytvorene");  
        System.out.print("id predstavenia je: ");
        System.out.println(s.getId());
    }
    
    /**
     * zmaže zadané predstavenie
     * @throws SQLException
     * @throws IOException
     * @throws ScreeningException 
     */
    public void deleteScreening() throws SQLException, IOException, ScreeningException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("zadajte id predstavenia: ");
        Integer screeningId = Integer.parseInt(in.readLine());
        Screening s = Screening.find(screeningId);
        if(s == null) throw new ScreeningException("neexistujuce predstavenie");
        
        s.delete();
        System.out.println("predstavenie bolo vymazane");
    }
    
    /**
     * Odporučí zadanému zákazníkovi predstavenia namiesto toho, ktoré sa zrušilo.
     * Zákazník si môže na jedno z predstavení vytvoriť objednávku alebo si vybrať zľavový kupón.
     * @throws IOException
     * @throws ScreeningException
     * @throws SQLException
     * @throws CustomerException
     * @throws OrderException
     * @throws CouponException 
     */
    //ked chcem najst take s ktorym to ma prejst
    //select * from orders o where (select start from screenings s where s.id = o.screening_id) > current_timestamp
    public void compensateAScreening() throws IOException, ScreeningException, SQLException, CustomerException, OrderException, CouponException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("zadaj id predstavenia: ");
        
        int screeningId = Integer.parseInt(in.readLine());
        Screening s = Screening.find(screeningId);
        if(s == null) throw new ScreeningException("neexistujuce predstavenie");
        if(s.getStart().before(new Timestamp(System.currentTimeMillis()))){ //ak uz bolo to predstavenie
            throw new ScreeningException("predstavenie uz bolo");
        }
        
        System.out.println("zadaj id zakaznika ktoremu sa bude kompenzovat: ");
        int customerId = Integer.parseInt(in.readLine());  
        Customer cu = Customer.find(customerId);
        if(cu == null) throw new CustomerException("neexistujuci zakaznik");
        
        ScreeningCompensator.customerAndScreeningCheck(customerId, screeningId); //ci ma zakaznik objednavku na to predstavenie
        int movieId = s.getMovieId();
        
        Set<Integer> rcmdScreeningIds = ScreeningCompensator.recommend(movieId, screeningId, s.getStart()); //odporuc predstavenia
        
        System.out.println("zadajte id predstavenia, ak chcete vytvorit objednavku. ");
        System.out.println("ak nie, napiste 'nechcem'.");
        Scanner scanner = new Scanner(in);
        if(scanner.hasNextInt()){
            int selectedScreeningId = scanner.nextInt();
            if(rcmdScreeningIds.contains(selectedScreeningId) == false){
                throw new ScreeningException("predstavenie nie je medzi odporucanymi");
            }
            
            try{
               //ScreeningCompensator.createOrder(cu, selectedScreeningId); 
                Screening s2 = Screening.find(screeningId);

                List<TicketType> typyListkov = TicketType.findAll();
                List<Integer> pocty = new ArrayList<>();

                for(TicketType t : typyListkov){
                    System.out.println("zadaj pocet listkov typu " + t.getType());
                    int pocet = Integer.parseInt(in.readLine());
                    pocty.add(pocet);
                }

                Order o = OrderOperations.createOrder(cu, s2, typyListkov, pocty);
                OrderMenu.confirmOrder(s2, pocty, o);
                
            }catch(OrderException e){ //ak sa neda vytvorit objednavka
                System.out.println(e.getMessage());
                CouponOperations.createCoupon(customerId, screeningId);
            }
        }
        else{
            String input = scanner.nextLine();
            if(input.equals("nechcem")) CouponOperations.createCoupon(customerId, screeningId); 
            else System.out.println("neznamy vstup");
        }
    }
}
