import java.math.BigDecimal;
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
public class TicketType {
    Integer id;
    String type;
    BigDecimal price;
    
    public void setId(Integer id){
        if(id == null){
            return;
        }
        this.id = id;
    }
    
    public void setType(String s){
        if(s == null){
           return;
        }
        type = s; 
    }
    
    public void setPrice(BigDecimal n){
        if(n == null || n.compareTo(new BigDecimal(0)) < 0){
            return;
        }
        price = n;
    }
    
    /**
     * nájde typ lístka podľa id
     * @param id id typu lístka
     * @return nájdený typ lístka
     * @throws SQLException 
     */
    public static TicketType find(int id) throws SQLException{
        String sql = "SELECT * FROM ticket_types WHERE id = ?";
        try(PreparedStatement ps = DbContext.getConnection().prepareStatement(sql)){ //connection z dbcontext
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    TicketType t = new TicketType();
                    t.setType(rs.getString("type"));
                    t.setPrice(rs.getBigDecimal("price"));

                    return t;
                }
            }
            return null;
        }
    }
    
    /**
     * nájde všetky typy lístkov
     * @return všetky typy lístkov
     * @throws SQLException 
     */
    public static List<TicketType> findAll() throws SQLException{ //najde vsetky take zaznamy
        String sql = "SELECT * FROM ticket_types";
        Connection c = DbContext.getConnection();
        try(PreparedStatement s = c.prepareStatement(sql)){
            return loadAll(s);
        }
    }
    
    /**
     * načíta všetky riadky z ResultSetu
     * @return načítané riadky
     * @throws SQLException 
     */
    private static List<TicketType> loadAll(PreparedStatement s) throws SQLException{
        try(ResultSet rs = s.executeQuery()){
            List<TicketType> zaznamy = new ArrayList<>();
            while(rs.next()){
                zaznamy.add(load(rs));
            }  
            return zaznamy;
        }
    }
    
    /**
     * načíta riadok z ResultSetu
     * @return načítaný riadok 
     * @throws SQLException 
     */
    private static TicketType load(ResultSet rs) throws SQLException{
        TicketType t = new TicketType();
        t.setId(rs.getInt("id"));
        t.setType(rs.getString("type"));
        t.setPrice(rs.getBigDecimal("price"));
        return t;
    }
    
    /**
     * vloží typ lístka do tabuľky ticket_types
     * @throws SQLException 
     */
    public void insert() throws SQLException{ //nie je staticka lebo bereme data z tej instancie
        String sql = "INSERT INTO ticket_types(type,price)"+
                " VALUES(?,?)";
        Connection c = DbContext.getConnection();
        try(PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,type);
            ps.setBigDecimal(2, price);
            
            ps.executeUpdate();

            try (ResultSet r = ps.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }
    
    /**
     * aktualizuje typ lístka
     * @throws SQLException 
     */
    public void update() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "UPDATE ticket_types SET type = ?, price = ? WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,type);
            ps.setBigDecimal(2, price);
            ps.setInt(3,id);
            
            ps.executeUpdate();
        }
    }
    
    /**
     * vymaže typ lístka
     * @throws SQLException 
     */
    public void delete() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "DELETE FROM ticket_types WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, id);
            
            ps.executeUpdate();
        }
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }
    
    
}
