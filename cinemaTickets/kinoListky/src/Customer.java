import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

/**
 * 
 * @author Matej Komlóssy
 */
public class Customer {
    Integer id;
    String first_name;
    String last_name;
    String email;
    Integer age;
    
    public void setId(Integer id){
        if(id == null){
            return;
        }
        this.id = id;
    }
    
    public void setFirstName(String s){
        if(s == null){
           return;
        }
        first_name = s; 
    }
    
    public void setLastName(String s){
        if(s == null){
           return;
        }
        last_name = s; 
    }
    
    public void setEmail(String email) {
        if(email == null){
            return;
        }
        this.email = email;
    }

    public void setAge(Integer age) {
        if(age == null){
            return;
        }
        this.age = age;
    }
    
    /**
     * nájde zákazníka podľa id
     * @param id id zákazníka
     * @return nájdený zákazník
     * @throws SQLException 
     */
    public static Customer find(int id) throws SQLException{
        String sql = "SELECT * FROM customers WHERE id = ?";
        try(PreparedStatement ps = DbContext.getConnection().prepareStatement(sql)){ //connection z dbcontext
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    Customer c = new Customer();
                    c.setId(rs.getInt("id"));
                    c.setFirstName(rs.getString("first_name"));
                    c.setLastName(rs.getString("last_name"));
                    c.setEmail(rs.getString("email"));
                    c.setAge(rs.getInt("age"));

                    return c;
                }
            }
            return null;
        }
    }
    
    /**
     * vloží zákazníka do tabuľky customers
     * @throws SQLException 
     */
    public void insert() throws SQLException{ 
        String sql = "INSERT INTO customers(first_name,last_name,email,age)"+
                " VALUES(?,?,?,?)";
        Connection c = DbContext.getConnection();
        try(PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,first_name);
            ps.setString(2, last_name);
            ps.setString(3,email);
            ps.setInt(4,age);
            
            ps.executeUpdate();

            try (ResultSet r = ps.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }
    
    /**
     * aktualizuje zákazníka
     * @throws SQLException 
     */
    public void update() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "UPDATE customers SET first_name = ?, last_name = ?, email = ?, age = ? WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,first_name);
            ps.setString(2, last_name);
            ps.setString(3,email);
            ps.setInt(4,age);
            
            ps.executeUpdate();
        }
    }
    
    /**
     * vymaže záakazníka
     * @throws SQLException 
     */
    public void delete() throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "DELETE FROM customers WHERE id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, id);
            
            ps.executeUpdate();
        }
    }
    
    /**
     * 
     * @return true,ak je email v správnom tvare. False ak nie je
     * @throws SQLException 
     */
    public boolean isEmailOk() throws SQLException{
        Connection c = DbContext.getConnection();
        boolean res = true;
        String sql = "SELECT ? SIMILAR TO '[a-z0-9.]+@[a-z.0-9]+.[a-z]+' as res";
        try(PreparedStatement s = c.prepareStatement(sql)){ 
           s.setString(1,email);
           try(ResultSet rs = s.executeQuery()){
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

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    } 
    
}

