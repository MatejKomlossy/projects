import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Matej Komlóssy
 */
public class Order {
    Integer id;
    Timestamp created_at;
    boolean is_confirmed;
    Integer screening_id;
    Integer customer_id;
    
    public void setId(Integer id){
        if(id == null){
            return;
        }
        this.id = id;
    }
    
    public void setCreatedAt(Timestamp t){
        if(t == null){
           return;
        }
        created_at = t; 
    }
    
    public void setIsConfirmed(boolean b){
        is_confirmed = b;
    }
    
    public void setScreeningId(Integer i){
        if(i == null){
            return;
        }
        screening_id = i;
    }
    
    public void setCustomerId(Integer i){
        if(i == null){
            return;
        }
        customer_id = i;
    }
    
    /**
     * nájde objednávku podľa id
     * @param id id objednávky
     * @return nájdená objednávka
     * @throws SQLException 
     */
    public static Order find(int id) throws SQLException{ //by order id
        String sql = "SELECT * FROM orders WHERE id = ?";
        try(PreparedStatement ps = DbContext.getConnection().prepareStatement(sql)){ //connection z dbcontext
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Order o = new Order();
                    o.setId(rs.getInt("id"));
                    o.setCreatedAt(rs.getTimestamp("created_at"));
                    o.setIsConfirmed(rs.getBoolean("is_confirmed"));
                    o.setScreeningId(rs.getInt("screening_id"));
                    o.setCustomerId(rs.getInt("customer_id"));

                    return o;
                }
            }
            return null;
        }
    }
    
    /**
     * nájde všetky zákazníkove objednávky
     * @param customerId id zákazníka
     * @return zoznam objednávok zákazníka
     * @throws SQLException 
     */
    public static List<Order> findAllByCustomerId(int customerId) throws SQLException{ //najde vsetky 
        String sql = "SELECT * FROM orders WHERE customer_id = ?";
        Connection c = DbContext.getConnection();
        try(PreparedStatement s = c.prepareStatement(sql)){
            s.setInt(1,customerId);
            return loadAll(s);
        }
    }
    
    /**
     * načíta všetky riadky z ResultSetu
     * @return načítané riadky
     * @throws SQLException 
     */
    private static List<Order> loadAll(PreparedStatement s) throws SQLException{
        try(ResultSet rs = s.executeQuery()){
            List<Order> zaznamy = new ArrayList<>();
            while(rs.next()){
                zaznamy.add(load(rs));
            }  
            return zaznamy;
        }
    }
    
    /**
     * načíta jeden riadok z ResultSetu
     * @return načítaný riadok
     * @throws SQLException 
     */
    private static Order load(ResultSet rs) throws SQLException{
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        o.setIsConfirmed(rs.getBoolean("is_confirmed"));
        o.setScreeningId(rs.getInt("screening_id"));
        o.setCustomerId(rs.getInt("customer_id"));
        return o;
    }
    
    /**
     * vloží objednávku do tabuľky orders
     * @throws SQLException 
     */
    public void insert() throws SQLException{ //nie je staticka lebo bereme data z tej instancie
        String sql = "INSERT INTO orders (created_at,is_confirmed,screening_id,customer_id)"+
                " VALUES(?,?,?,?)";
        Connection c = DbContext.getConnection();
        try(PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setTimestamp(1,created_at);
            ps.setBoolean(2, is_confirmed);
            ps.setInt(3,screening_id);
            ps.setInt(4,customer_id);
            
            ps.executeUpdate();

            try (ResultSet r = ps.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }
    
    /**
     * aktualizuje objednávku
     * @throws SQLException 
     */
    public void update() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "UPDATE orders SET created_at = ?, is_confirmed = ?, screening_id = ?, customer_id = ? WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setTimestamp(1,created_at);
            ps.setBoolean(2, is_confirmed);
            ps.setInt(3,screening_id);
            ps.setInt(4,customer_id);
            ps.setInt(5,id);
            
            ps.executeUpdate();
        }
    }
    
    /**
     * vymaže objednávku
     * @throws SQLException 
     */
    public void delete() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "DELETE FROM orders WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, id);
            
            ps.executeUpdate();
        }
    }
    
    /**
     * kontrola, či je objednávka platná
     * @return true,ak je objednávka platná, inak false
     * @throws SQLException 
     */
    public boolean isValid() throws SQLException{
        Connection c = DbContext.getConnection();
        String sql = "SELECT current_timestamp - ? < INTERVAL '10 minutes' AS res";
        boolean res = false;
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setTimestamp(1, created_at);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    res = rs.getBoolean("res");
                }
            }
        }
        return res;
    }

    public Integer getId() {
        return id;
    }

    public Timestamp getCreatedAt() {
        return created_at;
    }

    public boolean getIsConfirmed() {
        return is_confirmed;
    }

    public Integer getScreeningId() {
        return screening_id;
    }

    public Integer getCustomerId() {
        return customer_id;
    }

    
    
    
}

