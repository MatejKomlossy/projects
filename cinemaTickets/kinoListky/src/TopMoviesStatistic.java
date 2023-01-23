import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

/**
 * 
 * @author Matej Komlóssy
 */
public class TopMoviesStatistic {
    
    /**
     * vypíše pre každý žáner a každý mesiac za posledných 6 mesiacov tri filmy 
     * s týmto žánrom, ktoré zarobili najviac peňazí
     * @throws SQLException 
     */
    public static void topMovies() throws SQLException{
        String sql = "with mesiace as (select mesiac from generate_series(1,12) as seq(mesiac) where mesiac >= extract(month from current_timestamp - INTERVAL '5 months') or mesiac <= extract(month from current_timestamp)), "+

        "tab1 as (SELECT m.name as film, mg.genre_id,extract(month from s.start) as mesiac, "+
                //"case WHEN sum(tt.price) IS NULL then 0 else sum(tt.price) end as zarobok "+
              "case when sum(t.sold_for) IS NULL then 0 else sum(t.sold_for) end as zarobok "+
        "FROM movies_genres mg LEFT JOIN movies m ON mg.movie_id = m.id "+
        "LEFT JOIN screenings s ON s.movie_id = m.id "+
        "LEFT JOIN tickets t ON t.screening_id = s.id "+ //iba predane listky
        "LEFT JOIN orders o on t.order_id = o.id AND o.is_confirmed = true "+ //iba predane listky
        //"LEFT JOIN ticket_types tt ON t.ticket_type_id = tt.id "+

        "WHERE s.start BETWEEN current_timestamp - INTERVAL '5 months' AND current_timestamp "+
        "GROUP BY m.id,mg.genre_id, mesiac "+
        "ORDER BY genre_id DESC, mesiac DESC, zarobok DESC NULLS LAST), "+
                                                            //nulls last nemusi byt
        "tab2 as (select g.id as genre_id, mesiac from genres g cross join mesiace) "+

        "select tab2.genre_id,tab2.mesiac,tab1.film, tab1.zarobok from tab2 JOIN LATERAL "+
        "(SELECT film, genre_id, zarobok, mesiac from tab1  WHERE tab2.genre_id = genre_id AND mesiac = tab2.mesiac LIMIT 3) as tab1 "+
        "ON tab2.genre_id = tab1.genre_id AND tab2.mesiac = tab1.mesiac";                                           //3 filmy z kazdeho
        Connection c = DbContext.getConnection();
        try(Statement s = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
               try(ResultSet rs = s.executeQuery(sql)){
                   ResultSetPrinter.getInstance().print(rs,"top filmy");
               }
        }
    }
        
   
}
