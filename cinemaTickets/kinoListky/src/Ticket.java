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
public class Ticket {
    Integer id;
    Integer screening_id;
    Integer seat_id;
    Integer ticket_type_id;
    Integer order_id;
    Integer customer_id;
    BigDecimal sold_for;

    public void setId(Integer id) {
        if(id == null){
            return;
        }
        this.id = id;
    }

    public void setScreeningId(Integer screening_id) {
        if(screening_id == null){
            return;
        }
        this.screening_id = screening_id;
    }

    public void setSeatId(Integer seat_id) {
        this.seat_id = seat_id;
    }

    public void setTicketTypeId(Integer ticket_type_id) {
        if(ticket_type_id == null){
            return;
        }
        this.ticket_type_id = ticket_type_id;
    }

    public void setOrderId(Integer order_id) {
        if(order_id == null){
            return;
        }
        this.order_id = order_id;
    }

    public void setCustomerId(Integer customer_id) {
        if(customer_id == null){
            return;
        }
        this.customer_id = customer_id;
    }

    public void setSoldFor(BigDecimal i){
        if(i == null){
            return;
        }
        sold_for = i;
    }
    
    /**
     * nájde lístok podľa id
     * @param id id lístka
     * @return nájdený lístok
     * @throws SQLException 
     */
    public static Ticket find(int id) throws SQLException{
        String sql = "SELECT * FROM tickets WHERE id = ?";
        try(PreparedStatement ps = DbContext.getConnection().prepareStatement(sql)){ //connection z dbcontext
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Ticket t = new Ticket();
                    t.setId(rs.getInt("id"));
                    t.setScreeningId(rs.getInt("screening_id"));
                    t.setSeatId(rs.getInt("seat_id"));
                    t.setTicketTypeId(rs.getInt("ticket_type_id"));
                    t.setOrderId(rs.getInt("order_id"));
                    t.setCustomerId(rs.getInt("customer_id"));

                    return t;
                }
            }
            return null;
        }
    }
    
    /**
     * nájde lístky na dané predstavenie vytvorené danou objednávkou
     * @param screeningId id predstavenia, na ktoré lístky hľadáme
     * @param orderId id objednávky, ktorej lístky hľadáme
     * @return zoznam lístkov, ktoré patria danej objednávke a sú na dané predstavenie
     * @throws SQLException 
     */
    public static List<Ticket> findAllByScreeningAndOrder(int screeningId, int orderId) throws SQLException{
        String sql = "SELECT * FROM tickets t WHERE t.screening_id = ? AND t.order_id = ?";
        try(PreparedStatement ps = DbContext.getConnection().prepareStatement(sql)){ //connection z dbcontext
            ps.setInt(1,screeningId);
            ps.setInt(2,orderId);
            try(ResultSet rs = ps.executeQuery()){
                List<Ticket> zaznamy = new ArrayList<>();
                while(rs.next()){
                    Ticket t = new Ticket();
                    t.setId(rs.getInt("id"));
                    t.setScreeningId(rs.getInt("screening_id"));
                    t.setSeatId(rs.getInt("seat_id"));
                    t.setTicketTypeId(rs.getInt("ticket_type_id"));
                    t.setOrderId(rs.getInt("order_id"));
                    t.setCustomerId(rs.getInt("customer_id"));
                    zaznamy.add(t);
                }  
                return zaznamy;
            }
       }
    }
    
    /**
     * vloží lístok do tavuľky tickets
     * @throws SQLException 
     */
    public void insert() throws SQLException{ //nie je staticka lebo bereme data z tej instancie
        String sql = "INSERT INTO tickets(screening_id,seat_id,ticket_type_id,order_id,customer_id)"+
                " VALUES(?,?,?,?,?)";
        Connection c = DbContext.getConnection();
        try(PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,screening_id);
            if(seat_id == null) ps.setNull(2,java.sql.Types.INTEGER);
            else ps.setInt(2, seat_id);
            ps.setInt(3,ticket_type_id);
            ps.setInt(4,order_id);
            ps.setInt(5, customer_id);
            
            ps.executeUpdate();

            try (ResultSet r = ps.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }
    
    /**
     * aktualizuje lístok
     * @throws SQLException 
     */
    public void update() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "UPDATE tickets SET screening_id = ?, seat_id = ?, ticket_type_id = ?, order_id = ?, customer_id = ? WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,screening_id);
            if(seat_id == null) ps.setNull(2,java.sql.Types.INTEGER);
            else ps.setInt(2, seat_id);
            ps.setInt(3,ticket_type_id);
            ps.setInt(4,order_id);
            ps.setInt(5,customer_id);
            ps.setInt(6,id);
            
            ps.executeUpdate();
        }
    }
    
    /**
     * vymaže lístok
     * @throws SQLException 
     */
    public void delete() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "DELETE FROM tickets WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, id);
            
            ps.executeUpdate();
        }
    }
    
    /**
     * vymaže lístky patriace danému zákazníkovi, ktoré sú na dané predstavenie
     * @param customerId id zákazníka
     * @param screeningId id predstavenia
     * @throws SQLException 
     */
    public static void deleteByCustomerAndScreeningId(int customerId, int screeningId) throws SQLException{
        Connection c = DbContext.getConnection();
        String sql = "DELETE FROM tickets t WHERE t.customer_id = ? AND t.screening_id = ?";
            try(PreparedStatement ps = c.prepareStatement(sql)){
                ps.setInt(1,customerId);
                ps.setInt(2,screeningId);
                
                ps.executeUpdate();
            }
    }
    
    /**
     * zlacní lístky vytvorené danou objednávkou
     * @param orderId objednávka
     * @throws SQLException 
     */
    public static void cheapen(int orderId) throws SQLException{
        Connection c = DbContext.getConnection();
        String sql = "UPDATE tickets SET sold_for = 0.5 * sold_for "
                + "WHERE order_id = ?";
            try(PreparedStatement ps = c.prepareStatement(sql)){
                ps.setInt(1,orderId);
                
                ps.executeUpdate();
            }
    }

    public Integer getId() {
        return id;
    }

    public Integer getScreeningId() {
        return screening_id;
    }

    public Integer getSeatId() {
        return seat_id;
    }

    public Integer getTicketTypeId() {
        return ticket_type_id;
    }

    public Integer getOrderId() {
        return order_id;
    }

    public Integer getCustomerId() {
        return customer_id;
    }
    
    public BigDecimal getSoldFor(){
        return sold_for;
    }
    
}

