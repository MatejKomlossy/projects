import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.sql.Time;
import java.util.Arrays;
import java.util.HashSet;

/**
 * 
 * @author Matej Komlóssy
 */
public class MovieMenu extends Menu{
    @Override
    public void print() {
        System.out.println("*********************************");
        System.out.println("* 1. vypis udaje filmu          *");
        System.out.println("* 2. vytvor film                *");
        System.out.println("* 3. aktualizuj udaje filmu     *");
        System.out.println("* 4. vymaz film                 *");
        System.out.println("* 5. exit                       *");
        System.out.println("*********************************");
    }

    @Override
    public void handle(String option) {
        try {
            switch (option) {
                case "1":   printMovieAndItsScreenings(); break;
                case "2":   createMovie(); break;
                case "3":   updateMovie(); break;
                case "4":   deleteMovie(); break;
                case "5":   exit(); break;
                default:    System.out.println("Unknown option"); break;
            }
        } catch(IOException | MovieException | SQLException e){// | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * vypíše údaje zadaného filmu a jeho predstavení
     * @throws IOException
     * @throws MovieException
     * @throws SQLException 
     */
    public void printMovieAndItsScreenings() throws IOException, MovieException, SQLException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("zadajte id filmu: ");
        Integer movieId = Integer.parseInt(in.readLine());
        Movie m = Movie.find(movieId);
        if(m == null) throw new MovieException("neexistujuci film");
        
        MoviePrinter.getInstance().print(m);
        System.out.println("zanre filmu:");
        m.printGenres();
        System.out.println("");
        
        List<Screening> screenings = Screening.findByMovieId(movieId);
        if(screenings.size() == 0) System.out.println("film nema predstavenia"); //to nie je chyba
        else{
            System.out.println("");
            System.out.println("predstavenia: ");
            for(Screening s : screenings){
                ScreeningPrinter.getInstance().print(s);
                System.out.println("");
            }
        }
    }
    
    /**
     * vytvorí nový film podľa zadaných údajov
     * @throws IOException
     * @throws SQLException
     * @throws MovieException 
     */
    public void createMovie() throws IOException, SQLException, MovieException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("zadaj meno filmu(max 40 znakov): ");
        String meno = in.readLine();
        if(meno.length() > 40) throw new MovieException("prilis dlhy nazov");
        
        System.out.println("zadaj trvanie filmu ");
        System.out.println("hodiny:");
        Integer hod = Integer.parseInt(in.readLine());
        if(hod < 0 ) throw new MovieException("zla dlzka filmu");
        System.out.println("minuty:");
        Integer min = Integer.parseInt(in.readLine());
        if(min < 0 || min > 59) throw new MovieException("zla dlzka filmu");
        System.out.println("sekundy");
        Integer sek = Integer.parseInt(in.readLine());
        if(sek < 0 || sek > 59) throw new MovieException("zla dlzka filmu");
        Time trvanie = new Time(hod,min,sek);
        
        System.out.println("zadaj rok vydania: ");
        Integer rok = Integer.parseInt(in.readLine());
        
        Movie m = new Movie();
        
        m.setName(meno);
        m.setLength(trvanie);
        m.setYearOfRelease(rok);
        
        m.insert();
        System.out.println("film bol vytvoreny");  
        System.out.print("id filmu je: ");
        System.out.println(m.getId());
    }
    
    /**
     * aktualizuje názov a žánre zadaného filmu 
     * @throws IOException
     * @throws MovieException
     * @throws SQLException 
     */
    public void updateMovie() throws IOException, MovieException, SQLException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("zadaj id filmu: ");
        Integer movieId = Integer.parseInt(in.readLine());
        Movie m = Movie.find(movieId);
        if(m == null) throw new MovieException("neexistujuci film");
        MoviePrinter.getInstance().print(m);
        
        System.out.println("zanre filmu:");
        m.printGenres();
        
        System.out.println("\n");
        System.out.println("zadajte novy nazov(max 40 znakov): ");
        String newName = in.readLine();
        if(newName.trim().length() > 40 || newName.trim().length() == 0) throw new MovieException("zla dlzka nazvu");
        m.setName(newName);
        m.update();
        System.out.println("nazov bol aktualizovany");
        
        System.out.println("zadajte nove zanre oddelene medzerou(mena maju max 30 znakov):");
        List<String> genreNames = Arrays.asList(in.readLine().split(" "));
        if(genreNames.size() != new HashSet(genreNames).size()){
            throw new MovieException("film nemoze mat 2x ten isty zaner");
        }
        if(genreNames.get(0).length() == 0) throw new MovieException("nic ste nezadali");
        for(String name : genreNames){ //skontroluj dlzky mien zanrov
            if(name.length() > 30 || name.length() == 0) throw new MovieException("prilis dlhe meno zanru");
        }
        //vymaz stare zanre
        Movies_genres.deleteByMovieId(movieId);
        //pridaj nove zanre
        for(String name : genreNames){
            Genre g = Genre.findByName(name);
            if(g == null){ //pridaj zanre ak tam take nie su
                g = new Genre();
                g.setName(name);
                g.insert();
            }
            Movies_genres mg = new Movies_genres();
            mg.setMovieId(movieId);
            mg.setGenreId(g.getId());
            mg.insert();
        }

        System.out.println("zanre boli aktualizovane");
    }
    
    /**
     * vymaže zadaný film
     * @throws IOException
     * @throws SQLException
     * @throws MovieException 
     */
    public void deleteMovie() throws IOException, SQLException, MovieException{
        System.out.println("zadaj id filmu na vymazanie: ");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int id = Integer.parseInt(in.readLine());
        
        Movie m = Movie.find(id);
        if(m == null){
            throw new MovieException("neexistujuci film");
        }
         m.delete();  
         System.out.println("film bol zmazany");
    }
}
