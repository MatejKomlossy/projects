import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Time;

/**
 * 
 * @author Matej Komlóssy
 */
public class Movie {
    Integer id;
    String name;
    Time length;
    Integer year_of_release;
    
    public void setId(Integer id){
        if(id == null){
            return;
        }
        this.id = id;
    }
    
    public void setName(String name){
        if(name == null || name.length() > 30){
           return;
        }
        this.name = name; 
    }
    
    public void setLength(Time length){
        if(length == null){
            return;
        }
        this.length = length;
    }
    
    public void setYearOfRelease(Integer i){
        year_of_release = i;
    }
    
    /**
     * nájde film podľa id
     * @param id id filmu
     * @return nájdený film
     * @throws SQLException 
     */
    public static Movie find(int id) throws SQLException{
        String sql = "SELECT * FROM movies WHERE id = ?";
        try(PreparedStatement ps = DbContext.getConnection().prepareStatement(sql)){ //connection z dbcontext
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Movie m = new Movie();
                    m.setId(rs.getInt("id"));
                    m.setName(rs.getString("name"));
                    m.setLength(rs.getTime("length"));
                    m.setYearOfRelease(rs.getInt("year_of_release"));
                    return m;
                }
            }
            return null;
        }
    }
    
    /**
     * vloží film do tabuľky movies
     * @throws SQLException 
     */
    public void insert() throws SQLException{ //nie je staticka lebo bereme data z tej instancie
        String sql = "INSERT INTO movies(name,length,year_of_release)"+
                " VALUES(?,?,?)";
        Connection c = DbContext.getConnection();
        try(PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,name);
            ps.setTime(2, length);
            ps.setInt(3,year_of_release);
            
            ps.executeUpdate();

            try (ResultSet r = ps.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }
    
    /**
     * aktualizuje film
     * @throws SQLException 
     */
    public void update() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "UPDATE movies SET name = ?,  length = ? WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, name);
            ps.setTime(2, length);
            ps.setInt(3,id);
            
            ps.executeUpdate();
        }
    }
    
    /**
     * vymaže film
     * @throws SQLException 
     */
    public void delete() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "DELETE FROM movies WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, id);
            
            ps.executeUpdate();
        }
    }
    
    /**
     * vypíše žanre filmu
     * @throws SQLException 
     */
    public void printGenres() throws SQLException{
        String sql = "SELECT * FROM (SELECT * FROM movies_genres mg WHERE mg.movie_id = ?) as mg join genres g on mg.genre_id = g.id";
        try(PreparedStatement s = DbContext.getConnection().prepareStatement(sql)){ //connection z dbcontext
            s.setInt(1,id);
            try(ResultSet rs = s.executeQuery()){
                while(rs.next()){
                    String genreName = rs.getString("name");
                    System.out.print(genreName + " ");
                }
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Time getLength() {
        return length;
    }

    public Integer getYearOfRelease() {
        return year_of_release;
    }
    
    
}