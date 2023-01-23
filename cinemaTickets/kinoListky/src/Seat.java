import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

/**
 * 
 * @author Matej Komlóssy
 */
public class Seat {
    Integer id;
    Integer row_number;
    Integer seat_number;
    Integer screening_room_id;
    
    public void setId(Integer id){
        if(id == null){
            return;
        }
        this.id = id;
    }
    
    public void setRowNumber(Integer n){
        if(n == null || n < 0){
           return;
        }
        row_number = n; 
    }
    
    public void setSeatNumber(Integer n){
        if(n == null || n < 0){
            return;
        }
        seat_number = n;
    }

    public void setScreeningRoomId(Integer n){
        if(n == null || n < 0){
            return;
        }
        screening_room_id = n;
    }
    
    /**
     * nájde sedadlo podľa id
     * @param id id sedadla
     * @return nájdené seedadlo
     * @throws SQLException 
     */
    public static Seat find(int id) throws SQLException{
        String sql = "SELECT * FROM seats WHERE id = ?";
        try(PreparedStatement ps = DbContext.getConnection().prepareStatement(sql)){ //connection z dbcontext
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Seat s = new Seat();
                    s.setRowNumber(rs.getInt("row_number"));
                    s.setSeatNumber(rs.getInt("seat_number"));
                    s.setScreeningRoomId(rs.getInt("screening_room_id"));

                    return s;
                }
            }
            return null;
        }
    }
    
    /**
     * vloží sedadlo do tabuľky seats
     * @throws SQLException 
     */
    public void insert() throws SQLException{ //nie je staticka lebo bereme data z tej instancie
        String sql = "INSERT INTO seats(row_number,seat_number,screening_room_id)"+
                " VALUES(?,?,?)";
        Connection c = DbContext.getConnection();
        try(PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,row_number);
            ps.setInt(2, seat_number);
            ps.setInt(3, screening_room_id);
            
            ps.executeUpdate();

            try (ResultSet r = ps.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }
    
    /**
     * aktualizuje sedadlo
     * @throws SQLException 
     */
    public void update() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "UPDATE seats SET row_number = ?,  seat_number = ?,  screening_room_id  = ? WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, row_number);
            ps.setInt(2,seat_number);
            ps.setInt(3,screening_room_id);
            ps.setInt(4,id);
            
            ps.executeUpdate();
        }
    }
    
    /**
     * vymaže sedadlo
     * @throws SQLException 
     */
    public void delete() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "DELETE FROM seats WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, id);
            
            ps.executeUpdate();
        }
    }

    public Integer getId() {
        return id;
    }

    public Integer getRowNumber() {
        return row_number;
    }

    public Integer getSeatNumber() {
        return seat_number;
    }

    public Integer getScreeningRoomId() {
        return screening_room_id;
    }
    
    
}
