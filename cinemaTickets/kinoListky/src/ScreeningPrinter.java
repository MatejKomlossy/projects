/**
 * 
 * @author Matej Komlóssy
 */
public class ScreeningPrinter {
    private static final ScreeningPrinter INSTANCE = new ScreeningPrinter();
    
    /**
    * 
    * @return vráti inštanciu tejto triedy
    */
    public static ScreeningPrinter getInstance() { return INSTANCE; }
    
    private ScreeningPrinter() { }
        
    /**
     * vypíše údaje predstavenia
     * @param screening predstavenie na vypísanie
     */
    public void print(Screening screening) {
        if (screening == null) {
            throw new NullPointerException("screening cannot be null");
        }
        
        System.out.print("id :         ");
        System.out.println(screening.getId());
        System.out.print("zaciatok:    ");
        System.out.println(screening.getStart());
        System.out.print("id filmu:    ");
        System.out.println(screening.getMovieId());
        System.out.print("sala:        ");
        System.out.println(screening.getScreeningRoomId());
  
    }
}
