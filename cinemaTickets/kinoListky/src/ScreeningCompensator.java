import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Matej Komlóssy
 */
public class ScreeningCompensator {
    
    /**
     * vypíše najbližšie predstavenia na základe podobnosti filmu
     * s filmom, ktorého predstavenie bolo zrušené
     * @param movieId id filmu
     * @param screeningId id predstavenia
     * @param screeningStart začiatok predstavenia
     * @return zoznam id odporučených predstavení
     * @throws SQLException 
     */
    public static Set<Integer> recommend(int movieId, int screeningId, Timestamp screeningStart) throws SQLException{ //zmenit
       Connection c = DbContext.getConnection();
        String sql = "with zanre_zruseneho as (select genre_id from movies_genres mg where movie_id = ?) "+

        "(select * from screenings s join movies m on s.movie_id = m.id " +
        "where s.start > ? " +
        "and s.movie_id = ? LIMIT 5) "+
                
        "union "+
                
        "(select * from screenings s join movies m on s.movie_id = m.id " +
        "where s.start > ? "+
        "order by s.start asc, " +
        "(select count(*) from movies_genres mg where " +
        "mg.genre_id in (select * from zanre_zruseneho)) desc LIMIT 10)";
        Set<Integer> rcmdScreeningIds = new HashSet<>();
        
        try(PreparedStatement s = c.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){ //vypis odporucani
            s.setInt(1,movieId);
            s.setTimestamp(2,screeningStart);
            s.setInt(3,movieId);
            s.setTimestamp(4,screeningStart);
            
            System.out.println("odporucane predstavenia:");
            try(ResultSet rs = s.executeQuery()){
                while(rs.next()){
                   //zisti id predst., nazov filmu,vypis, ids daj do set-u a vrat
                   int screening_id = rs.getInt("id");
                   String movie_name = rs.getString("name");
                   Timestamp start = rs.getTimestamp("start");
                   rcmdScreeningIds.add(screening_id);
                   System.out.println("id: " + zarovnane(String.valueOf(screening_id),7) +"  " +zarovnane(movie_name,20) + "  " + zarovnane(start.toString(),20));
                }
            }/*
            try(ResultSet rs = s.executeQuery()){ //nerobim kvoli kontrole rcmdScreeningIds
                 SqlPrinter.getInstance().print(rs,"odporucane filmy");
             }*/
        }
        
        return rcmdScreeningIds;
    }

    /**
     * skontroluje, či má zákazník objednávku na predstavenie
     * @param customerId id zákazníka
     * @param screeningId id predstavenia
     * @throws SQLException
     * @throws CustomerException vyhodí výnimku ak zákazník nemá objednávku na predstavenie
     */
    public static void customerAndScreeningCheck(int customerId, int screeningId) throws SQLException, CustomerException{
        Connection c = DbContext.getConnection();
        String sql = "SELECT id FROM orders o WHERE o.customer_id = ? AND o.screening_id = ?";
        try(PreparedStatement s = c.prepareStatement(sql)){
            s.setInt(1,customerId);
            s.setInt(2,screeningId);
            try(ResultSet rs = s.executeQuery()){
                if(rs.next()){
                    ;
                }
                else{
                    throw new CustomerException("zakaznik nema objednavku na toto predstavenie");
                }
            }
        }
    }
    
    /**
     * vráti vstupný reťazec zarovnaný na daný počet znakov
     * @param s vstupný reťazec
     * @param kolko na koľko znakov sa má reťazec zarovnať
     * @return 
     */
    private static String zarovnane(String s, int kolko){
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < kolko-s.length(); i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
