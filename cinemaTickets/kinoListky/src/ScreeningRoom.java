import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

/**
 * 
 * @author Matej Komlóssy
 */
public class ScreeningRoom {
    Integer id;
    Integer number_of_rows;
    Integer row_length;
    
    public void setId(Integer id){
        if(id == null){
            return;
        }
        this.id = id;
    }
    
    public void setNumberOfRows(Integer n){
        if(n == null || n <= 0){
           return;
        }
        number_of_rows = n; 
    }
    
    public void setRowLength(Integer rl){
        if(rl == null || rl <= 0){
            return;
        }
        row_length = rl;
    }

    
    public static ScreeningRoom find(int id) throws SQLException{
        String sql = "SELECT * FROM screening_rooms WHERE id = ?";
        try(PreparedStatement ps = DbContext.getConnection().prepareStatement(sql)){ //connection z dbcontext
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    ScreeningRoom sr = new ScreeningRoom();
                    sr.setNumberOfRows(rs.getInt("number_of_rows"));
                    sr.setRowLength(rs.getInt("row_length"));

                    return sr;
                }
            }
            return null;
        }
    }
    
    public void insert() throws SQLException{ //nie je staticka lebo bereme data z tej instancie
        String sql = "INSERT INTO screening_rooms(number_of_rows,row_length)"+
                " VALUES(?,?)";
        Connection c = DbContext.getConnection();
        try(PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,number_of_rows);
            ps.setInt(2, row_length);
            
            ps.executeUpdate();

            try (ResultSet r = ps.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }
    
    public void update() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "UPDATE screening_rooms SET number_of_rows = ?,  row_length = ?  WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, number_of_rows);
            ps.setInt(2,row_length);
            ps.setInt(3,id);
            
            ps.executeUpdate();
        }
    }
    
    public void delete() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "DELETE FROM screening_rooms WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, id);
            
            ps.executeUpdate();
        }
    }
    
    public int capacity() {
        return number_of_rows * row_length;
    }

    public Integer getId() {
        return id;
    }

    public Integer getNumberOfRows() {
        return number_of_rows;
    }

    public Integer getRowLength() {
        return row_length;
    }
    
    
}
