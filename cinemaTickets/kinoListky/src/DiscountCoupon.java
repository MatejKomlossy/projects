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
public class DiscountCoupon {
    Integer id;
    boolean is_valid;
    Integer number;
    Integer order_id;
    Integer customer_id;

    public void setId(Integer id) {
        if(id == null){
            return;
        }
        this.id = id;
    }

    public void setIsValid(boolean is_valid) {
        this.is_valid = is_valid;
    }

    public void setNumber(Integer number) {
        if(number == null){
            return;
        }
        this.number = number;
    }

    public void setOrderId(Integer order_id) {
        this.order_id = order_id;
    }

    public void setCustomerId(Integer customer_id) {
        if(customer_id == null){
            return;
        }
        this.customer_id = customer_id;
    }
    
    /**
     * nájde kupón podľa id
     * @param id id kupónu
     * @return nájdený kupón
     * @throws SQLException 
     */
    public static DiscountCoupon find(int id) throws SQLException{
        String sql = "SELECT * FROM discount_coupons WHERE id = ?";
        try(PreparedStatement ps = DbContext.getConnection().prepareStatement(sql)){ //connection z dbcontext
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    DiscountCoupon dc = new DiscountCoupon();
                    dc.setId(rs.getInt("id"));
                    dc.setIsValid(rs.getBoolean("is_valid"));
                    dc.setNumber(rs.getInt("number"));
                    dc.setOrderId(rs.getInt("order_id"));
                    dc.setCustomerId(rs.getInt("customer_id"));

                    return dc;
                }
            }
            return null;
        }
    }
    
    /**
     * nájde kupón podľa jeho kódu
     * @param number kód kupónu
     * @return DiscountCoupon s parametrami kupónu, ktorý má kód number
     * @throws SQLException 
     */
    public static DiscountCoupon findByNumber(int number) throws SQLException{
        String sql = "SELECT * FROM discount_coupons WHERE id = ?";
        try(PreparedStatement ps = DbContext.getConnection().prepareStatement(sql)){ //connection z dbcontext
            ps.setInt(1,number);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    DiscountCoupon dc = new DiscountCoupon();
                    dc.setId(rs.getInt("id"));
                    dc.setIsValid(rs.getBoolean("is_valid"));
                    dc.setNumber(rs.getInt("number"));
                    dc.setOrderId(rs.getInt("order_id"));
                    dc.setCustomerId(rs.getInt("customer_id"));

                    return dc;
                }
            }
            return null;
        }
    }
    
    /**
     * nájde všetky kupóny patriace zákazníkovi s id customerId
     * @param customerId id zákazníka,ktorého kupóny hľadáme
     * @return zoznam kupónov zákazníka
     * @throws SQLException 
     */
    public static List<DiscountCoupon> findByCustomer(int customerId) throws SQLException{
        List<DiscountCoupon> res = new ArrayList<>();
        String sql = "SELECT * FROM discount_coupons WHERE customer_id = ?";
        try(PreparedStatement ps = DbContext.getConnection().prepareStatement(sql)){ //connection z dbcontext
            ps.setInt(1,customerId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    DiscountCoupon dc = new DiscountCoupon();
                    dc.setId(rs.getInt("id"));
                    dc.setIsValid(rs.getBoolean("is_valid"));
                    dc.setNumber(rs.getInt("number"));
                    dc.setOrderId(rs.getInt("order_id"));
                    dc.setCustomerId(rs.getInt("customer_id"));

                    res.add(dc);
                }
            }
            return res;
        }
    }
    
    /**
     * vloží kupón do tabuľky discount_coupons
     * @throws SQLException 
     */
    public void insert() throws SQLException{ //nie je staticka lebo bereme data z tej instancie
        String sql = "INSERT INTO discount_coupons(is_valid,order_id,customer_id)"+
                " VALUES(?,?,?)";
        Connection c = DbContext.getConnection();
        try(PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setBoolean(1, is_valid);
            //ps.setString(2, number);
            if(order_id == null) ps.setNull(2,java.sql.Types.INTEGER);
            else ps.setInt(2, order_id);
            ps.setInt(3, customer_id);
            
            ps.executeUpdate();

            try (ResultSet r = ps.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
                number = r.getInt(1);
            }
        }
    }
    
    /**
     * aktualizuje kupón
     * @throws SQLException 
     */
    public void update() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "UPDATE discount_coupons SET is_valid = ?, number = ?, customer_id = ? WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setBoolean(1, is_valid);
            ps.setInt(2, number);
            //if(order_id == null) ps.setNull(3,java.sql.Types.INTEGER);
            //else ps.setInt(3, order_id);
            ps.setInt(3,customer_id);
            ps.setInt(4,id);
            
            ps.executeUpdate();
        }
    }
    
    /**
     * vymaže kupón
     * @throws SQLException 
     */
    public void delete() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "DELETE FROM discount_coupons WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, id);
            
            ps.executeUpdate();
        }
    }

    public Integer getId() {
        return id;
    }

    public boolean getIsValid() {
        return is_valid;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getOrderId() {
        return order_id;
    }

    public Integer getCustomerId() {
        return customer_id;
    }

    
    
    
}
