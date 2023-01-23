import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Matej Komlóssy
 */
public class RevenuesStatistic {
    /**
     * Vypíše zárobky za filmy zvlášť pre každý žaner.
     * Sumy sú za aktuálny mesiac, zároveň vypíše rozdiel súm voči predchádzajúcemu mesiacu
     * @throws SQLException 
     */
    public static void monthlyRevenues() throws SQLException{
        String sql = "with tento_mesiac as (select extract(month from current_timestamp))," +
            "predch_mesiac as (select extract(month from current_timestamp - interval '1 month'))," +

            "tab1 as " +
            "(select sum(t.sold_for) as zarobok, extract(month from s.start) as mesiac, mg.genre_id as zaner " +
            "from movies_genres mg LEFT JOIN movies m ON mg.movie_id = m.id " +
            "LEFT JOIN screenings s ON s.movie_id = m.id " +
            "LEFT JOIN tickets t ON t.screening_id = s.id " +
            "LEFT JOIN orders o on t.order_id = o.id AND o.is_confirmed = true " +
            "group by zaner, mesiac " +
            "having extract(month from s.start) = (select * from tento_mesiac) " +
            "or extract(month from s.start) = (select * from predch_mesiac))," +

            "tab11 as (select * from tab1 where mesiac = (select * from tento_mesiac)), " +
            "tab12 as (select * from tab1 where mesiac = (select * from predch_mesiac)) " +

            "select tab11.zarobok, tab11.zarobok - tab12.zarobok as vs_last_month, tab11.mesiac, tab11.zaner " +
            "from tab11 join tab12 on tab11.zaner = tab12.zaner ";                                           
        Connection c = DbContext.getConnection();
        try(Statement s = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
               try(ResultSet rs = s.executeQuery(sql)){
                   ResultSetPrinter.getInstance().print(rs,"mesacne trzby");
               }
        }
    }
}
