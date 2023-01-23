/**
* 
* @author Matej Komlóssy
*/
public class MoviePrinter {
    private static final MoviePrinter INSTANCE = new MoviePrinter();
    
    /**
     * 
     * @return vráti inštanciu tejto triedy
     */
    public static MoviePrinter getInstance() { return INSTANCE; }
    
    private MoviePrinter() { }
        
    /**
     * vypíše údaje filmu
     * @param movie film na vypísanie
     */
    public void print(Movie movie) {
        if (movie == null) {
            throw new NullPointerException("movie cannot be null");
        }
        
        System.out.print("id :          ");
        System.out.println(movie.getId());
        System.out.print("nazov:        ");
        System.out.println(movie.getName());
        System.out.print("dlzka:        ");
        System.out.println(movie.getLength());
        System.out.print("rok vydania:  ");
        System.out.println(movie.getYearOfRelease());
  
    }
}
