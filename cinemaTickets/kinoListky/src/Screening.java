import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 * 
 * @author Matej Komlóssy
 */
public class Screening {
    Integer id;
    Timestamp start;
    Integer movie_id;
    Integer screening_room_id;
    
    public void setId(Integer id){
        if(id == null){
            return;
        }
        this.id = id;
    }
    
    public void setStart(Timestamp t){
        if(t == null){
           return;
        }
        start = t; 
    }
    
    public void setMovieId(Integer id){
        if(id == null){
            return;
        }
        movie_id = id;
    }
    
    public void setScreeningRoomId(Integer id){
        if(id == null){
            return;
        }
        screening_room_id = id;
    }
    
    /**
     * nájde predstavenie podľa id
     * @param id if predstavenia
     * @return nájdené predstavenie
     * @throws SQLException 
     */
    public static Screening find(int id) throws SQLException{
        String sql = "SELECT * FROM screenings WHERE id = ?";
        try(PreparedStatement ps = DbContext.getConnection().prepareStatement(sql)){ //connection z dbcontext
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Screening s = new Screening();
                    s.setId(rs.getInt("id"));
                    s.setStart(rs.getTimestamp("start"));
                    s.setMovieId(rs.getInt("movie_id"));
                    s.setScreeningRoomId(rs.getInt("screening_room_id"));
                    return s;
                }
            }
            return null;
        }
    }
    
    /**
     * nájde všetky predstavenia na daný film
     * @param id id filmu
     * @return zoznam predstavení na film
     * @throws SQLException 
     */
    public static List<Screening> findByMovieId(int id) throws SQLException{
        List<Screening> res = new ArrayList<>();
        String sql = "SELECT * FROM screenings WHERE movie_id = ?";
        try(PreparedStatement ps = DbContext.getConnection().prepareStatement(sql)){ //connection z dbcontext
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Screening s = new Screening();
                    s.setId(rs.getInt("id"));
                    s.setStart(rs.getTimestamp("start"));
                    s.setMovieId(rs.getInt("movie_id"));
                    s.setScreeningRoomId(rs.getInt("screening_room_id"));
                    res.add(s);
                }
            }
            return res;
        }
    }
    
    /**
     * vloží predstavenie do tabuľky screenings
     * @throws SQLException 
     */
    public void insert() throws SQLException{ //nie je staticka lebo bereme data z tej instancie
        String sql = "INSERT INTO screenings(start,movie_id, screening_room_id)"+
                " VALUES(?,?,?)";
        Connection c = DbContext.getConnection();
        try(PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setTimestamp(1,start);
            ps.setInt(2, movie_id);
            ps.setInt(3, screening_room_id);
            
            ps.executeUpdate();

            try (ResultSet r = ps.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }
    
    /**
     * aktualizuje predstavenie
     * @throws SQLException 
     */
    public void update() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "UPDATE screenings SET start = ?,  movie_id = ?, screening_room_id= ? WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setTimestamp(1, start);
            ps.setInt(2, movie_id);
            ps.setInt(3,screening_room_id);
            ps.setInt(4,id);
            
            ps.executeUpdate();
        }
    }
    
    /**
     * vymaže predstavenie
     * @throws SQLException 
     */
    public void delete() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "DELETE FROM screenings WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, id);
            
            ps.executeUpdate();
        }
    }

    /**
     * zistí počet voľných lístkov a či sa ešte dá na predstavenie objednávať 
     * @return dvojica,nultý prvok je 0 ak nie je neskoro na objednávanie na toto predstavenie, inak 1
     * druhý prvok je počet voľných miest
     * @throws SQLException 
     */
    public List<Integer> timeAndTicketCheck() throws SQLException{ //ci je neskoro na objednavanie a pocet volnych listkov
       Connection c = DbContext.getConnection();
       
       ScreeningRoom sr = ScreeningRoom.find(screening_room_id);
       int srCapacity = sr.capacity();
       
       int ticketCount = 0;
       int tooLateToOrder = 0;
       String sql = "SELECT "+
        "(SELECT start - current_timestamp < INTERVAL '40 minutes' AS result FROM screenings " +
        "WHERE id = ?) as too_late_to_order, " +
        "(SELECT count(*) AS pocet FROM (select * from tickets t WHERE t.screening_id = ?) as tmp " +
        "JOIN orders o ON o.id = tmp.order_id " +
        "WHERE o.is_confirmed = true " + //potvrdene
        "OR current_timestamp - o.created_at BETWEEN INTERVAL '0 minutes' AND INTERVAL '10 minutes') " + //alebo nepotvrdene platne
        "as ticket_count";
       try(PreparedStatement s = c.prepareStatement(sql)){          
           s.setInt(1,id);
           s.setInt(2, id);
           try(ResultSet rs = s.executeQuery()){
               if(rs.next()){
                   ticketCount = rs.getInt("ticket_count");
                   boolean tmp = rs.getBoolean("too_late_to_order");
                   tooLateToOrder = tmp == false? 0 : 1;
               }
           }
       }
       List<Integer> res = new ArrayList<>();
       res.add(tooLateToOrder);
       res.add(srCapacity - ticketCount);
       
       return res;
    }
    
    /**
     * nájde všetky id voľných miest z kinosály, kde sa koná toto predstavenie
     * @return množina id voľných miest
     * @throws SQLException 
     */
    public Set<Integer> setOfFreeSeats() throws SQLException{
        Connection c = DbContext.getConnection();
        Set<Integer> res = new HashSet<>();
        String sql = "SELECT id FROM seats s WHERE s.screening_room_id = ? AND NOT EXISTS "+
                "(SELECT NULL FROM tickets t WHERE t.seat_id = s.id AND t.screening_id = ?)";
        try(PreparedStatement s = c.prepareStatement(sql)){
            s.setInt(1,screening_room_id);
            s.setInt(2, id);
            try(ResultSet rs = s.executeQuery()){
                while(rs.next()){
                    int seatId = rs.getInt("id");
                    res.add(seatId);
                }
            }
        }
        return res;
    }
    

    public Integer getId() {
        return id;
    }

    public Timestamp getStart() {
        return start;
    }

    public Integer getMovieId() {
        return movie_id;
    }

    public Integer getScreeningRoomId() {
        return screening_room_id;
    }
    
    
}
