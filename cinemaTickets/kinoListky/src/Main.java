
import java.io.IOException;
import java.sql.SQLException;
import org.postgresql.ds.PGSimpleDataSource;

/**
 * 
 * @author Matej Komlóssy
 */
public class Main {

    public static void main(String[] args) throws IOException, SQLException, OrderException {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setServerName("db.dai.fmph.uniba.sk");
        ds.setPortNumber(5432);
        ds.setDatabaseName("playground");
        ds.setUser("komlossy2@uniba.sk");
        ds.setPassword("abcabc");
        
        try{
            DbContext.setConnection(ds.getConnection());
            
            MainMenu mainMenu = new MainMenu();
            mainMenu.run();
            
        } finally{
           DbContext.clear();
        }
    }
    
}
