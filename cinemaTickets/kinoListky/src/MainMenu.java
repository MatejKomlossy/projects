import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * 
 * @author Matej Komlóssy
 */
public class MainMenu extends Menu { //hlavna kostra prevzata zo vzoroveho projektu

    @Override
    public void print() {
        System.out.println("*********************************");
        System.out.println("* 1. pridaj zakaznika           *");
        System.out.println("* 2. vypis udaje zakaznika      *");
        System.out.println("* 3. vymaz zakaznika            *");
        System.out.println("* 4. objednavkove menu          *");
        System.out.println("* 5. ukaz top filmy             *");
        System.out.println("* 6. ukaz mesacne trzby         *");
        System.out.println("* 7. filmove menu               *");
        System.out.println("* 8. menu predstaveni           *");
        System.out.println("* 9. menu kuponov               *");
        System.out.println("* 10. exit                      *");
        System.out.println("*********************************");
    }

    @Override
    public void handle(String option) {
        try {
            switch (option) {
                case "1":   createCustomer(); break;
                case "2":   printCustomer(); break;
                case "3":   deleteCustomer(); break;
                case "4":   goToOrderMenu(); break;
                case "5":   TopMoviesStatistic.topMovies();break;
                case "6":   RevenuesStatistic.monthlyRevenues(); break;
                case "7":   goToMovieMenu(); break;
                case "8":   goToScreeningMenu(); break;
                case "9":   goToCouponMenu(); break;
                case "10":   exit(); break;
                default:    System.out.println("Unknown option"); break;
            }
        } catch(SQLException | CustomerException | IOException e){// | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * vytvorí zákazníka podľa zadaných údajov
     * @throws IOException
     * @throws SQLException
     * @throws CustomerException 
     */
    public void createCustomer() throws IOException, SQLException, CustomerException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("zadaj meno(max 40 znakov): ");
        String firstName = in.readLine();
        if(firstName.length() > 40) throw new CustomerException("prilis dlhe meno");
        
        System.out.println("zadaj priezvisko(max 40 znakov): ");
        String lastName = in.readLine();
        if(lastName.length() > 40) throw new CustomerException("prilis dlhe meno");
        
        System.out.println("zadaj email adresu: ");
        String email = in.readLine();
        
        Customer c1 = new Customer();
        c1.setFirstName(firstName);
        c1.setLastName(lastName);
        c1.setEmail(email);
        if(c1.isEmailOk() == false) throw new CustomerException("email nie je v spravnom tvare");
        
        System.out.println("zadaj vek: ");
        Integer age = Integer.parseInt(in.readLine());
        if(age < 1) throw new CustomerException("vek musi byt aspon 1");
        
        c1.setAge(age);
        
        c1.insert();
        System.out.println("zakaznik bol pridany");  
        System.out.print("zakaznikove id je: ");
        System.out.println(c1.getId());
    }
    
    /**
     * vymaže zákazníka podľa zadaného id
     * @throws IOException
     * @throws SQLException
     * @throws CustomerException 
     */
    public void deleteCustomer() throws IOException, SQLException, CustomerException{ 
        System.out.println("zadaj id zakaznika na vymazanie: ");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int id = Integer.parseInt(in.readLine());
        
        Customer c1 = Customer.find(id);
        if(c1 == null){
            throw new CustomerException("neexistujuci zakaznik");
        }
         c1.delete();  
         System.out.println("zakaznik bol zmazany");
    }
    
    /**
     * vypíše údaje zadaného zákazníka
     * @throws IOException
     * @throws SQLException
     * @throws CustomerException 
     */
    public void printCustomer() throws IOException, SQLException, CustomerException{ 
        System.out.println("zadaj id zakaznika na vypisanie: ");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int id = Integer.parseInt(in.readLine());
        
        Customer c1 = Customer.find(id);
        if(c1 == null){
            throw new CustomerException("neexistujuci zakaznik");
        }
        else{
          CustomerPrinter.getInstance().print(c1);  
        }
    }
    
    /**
     * spustí menu objednávok
     * @throws IOException 
     */
    public void goToOrderMenu() throws IOException{
        OrderMenu orderMenu = new OrderMenu();
        orderMenu.run();
    }
    
    /**
     * spustí menu filmov
     * @throws IOException 
     */
    public void goToMovieMenu() throws IOException{
        MovieMenu movieMenu = new MovieMenu();
        movieMenu.run();
    }
    
    /**
     * spustí menu predstavenní
     * @throws IOException 
     */
    public void goToScreeningMenu() throws IOException{
        ScreeningMenu screeningMenu = new ScreeningMenu();
        screeningMenu.run();
    }
    
    /**
     * spustí menu kupónov
     * @throws IOException 
     */
    public void goToCouponMenu() throws IOException{
        CouponMenu couponMenu = new CouponMenu();
        couponMenu.run();
    }
 }
