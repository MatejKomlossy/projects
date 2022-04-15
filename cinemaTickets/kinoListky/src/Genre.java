import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Matej Komlóssy
 */
public class Genre {
    Integer id;
    String name;
    
    public void setName(String name){
        if(name == null || name.length() > 20){
           return; 
        }
        this.name = name;
    }
    
    public void setId(Integer id){
        if(id == null){
            return;
        }
        this.id = id;
    }
    
    /**
     * nájde žáner podľa id
     * @param id id žánru
     * @return nájdený žáner
     * @throws SQLException 
     */
    public static Genre find(int id) throws SQLException{ //najde prvy taky zaznam
        String sql = "SELECT * FROM genres WHERE id = ?";
        Connection c = DbContext.getConnection();
        try(PreparedStatement s = c.prepareStatement(sql)){
            s.setInt(1, id);
            
            return loadOne(s);
        }
        
    }
    
    /**
     * nájde žáner podľa mena
     * @param n meno žánru
     * @return nájdený žáner
     * @throws SQLException 
     */
    public static Genre findByName(String n) throws SQLException{ 
        String sql = "SELECT * FROM genres WHERE name = ?";
        Connection c = DbContext.getConnection();
        try(PreparedStatement s = c.prepareStatement(sql)){
            s.setString(1, n);
            
            return loadOne(s);
        }
        
    }
    
    /**
     * načíta jeden riadok zo statementu do inštancie Genre 
     * @return načítaný žáner
     * @throws SQLException 
     */
    private static Genre loadOne(PreparedStatement s) throws SQLException {
        try(ResultSet r = s.executeQuery()){
           if (r.next()) { 
               return load(r); 
           }
           else {
             return null; 
           } 
        }
    }

    /**
     * načíta jeden riadok z ResultSetu do inštancie Genre 
     * @return načítaný žáner
     * @throws SQLException 
     */
    private static Genre load(ResultSet rs) throws SQLException{
        Genre g = new Genre();
        g.setId(rs.getInt("id"));
        g.setName(rs.getString("name"));
        return g;
    }
    
    /**
     * nájde všetky žánre
     * @return zoznam žánrov
     * @throws SQLException 
     */
    public static List<Genre> findAll() throws SQLException{ //najde vsetky 
        String sql = "SELECT * FROM genres";
        Connection c = DbContext.getConnection();
        try(PreparedStatement s = c.prepareStatement(sql)){
            return loadAll(s);
        }
    }
    
    /**
     * načíta všetky žánre zo statementu
     * @return zoznam žánrov
     * @throws SQLException 
     */
    private static List<Genre> loadAll(PreparedStatement s) throws SQLException{
        try(ResultSet rs = s.executeQuery()){
            List<Genre> zaznamy = new ArrayList<>();
            while(rs.next()){
                zaznamy.add(load(rs));
            }  
            return zaznamy;
        }
    }
    
    /**
     * bloží žáner do tabuľky genres
     * @throws SQLException 
     */
    public void insert() throws SQLException{ //nie je staticka lebo bereme data z tej instancie
        String sql = "INSERT INTO genres(name)"+
                " VALUES(?)";
        Connection c = DbContext.getConnection();
        try(PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,name);
            
            ps.executeUpdate();

            try (ResultSet r = ps.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }
    
    /**
     * aktualizuje žáner
     * @throws SQLException 
     */
    public void update() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "UPDATE genres SET name = ? WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, name);
            ps.setInt(2,id);
            
            ps.executeUpdate();
        }
    }
    
    /**
     * vymaže žáner
     * @throws SQLException 
     */
    public void delete() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "DELETE FROM genres WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, id);
            
            ps.executeUpdate();
        }
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    
}