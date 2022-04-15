import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * 
 * @author Matej Komlóssy
 */
public class ResultSetPrinter {
    private static final ResultSetPrinter INSTANCE = new ResultSetPrinter();
    
    /**
    * 
    * @return vráti inštanciu tejto triedy
    */
    public static ResultSetPrinter getInstance() { return INSTANCE; }
    
    private ResultSetPrinter() { }
        
    /**
     * vypíše ResultSet do konzoly vo forme tabuľky.
     * @param rs ResultSet na vypísanie
     * @param tableName aký názov má mať vypísaná tabuľka
     * @throws SQLException 
     */
    public void print(ResultSet rs, String tableName) throws SQLException{
        Connection c = DbContext.getConnection();
        //try(Statement s = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)){
               //try(ResultSet rs = s.executeQuery(sql)){
                   ResultSetMetaData metaData = rs.getMetaData();
                   int[] columnLengths = new int[metaData.getColumnCount()]; //max dlzky stlpcov
                   String[] columnNames = new String[metaData.getColumnCount()]; //mena stlpcov
                   while(rs.next()){
                       for(int i = 1; i <= metaData.getColumnCount(); i++){
                           String columnName = metaData.getColumnName(i);
                           columnNames[i-1] = columnName;
                           String value = rs.getString(i);
                           if(value == null){
                                if(columnName.length() > columnLengths[i-1]){
                                    columnLengths[i-1] = columnName.length();
                                }
                                if(4 > columnLengths[i-1]){
                                    columnLengths[i-1] = 4;
                                }
                               continue;
                           }
                           if(columnName.length() > columnLengths[i-1]){
                               columnLengths[i-1] = columnName.length() +1; //+1 nech je tam medzera trochu
                           }
                           if(value.length() > columnLengths[i-1]){
                               columnLengths[i-1] = value.length() +1;
                           }
                       }
                    }

                   rs.beforeFirst(); //pred zaciatok

                   int tableLength = 0;
                   for(int i=0; i < columnLengths.length; i++){
                       tableLength += columnLengths[i];
                   }
                   tableLength += columnLengths.length +1;

                   vypisCiaru(tableLength);     //nazov tabulky a stlpcov
                   //String tableName = metaData.getTableName(1);
                   vypisZarovnane(tableName, tableLength-2);
                   System.out.println("*");
                   vypisCiaru(tableLength);
                   for(int i=0; i < columnNames.length; i++){
                       vypisZarovnane(columnNames[i], columnLengths[i]);
                   }
                   System.out.println("*");
                   vypisCiaru(tableLength);

                   while(rs.next()){
                       for(int i = 1; i <= metaData.getColumnCount(); i++){
                           vypisZarovnane(rs.getString(i), columnLengths[i-1]);
                       }
                       System.out.println("*");
                       vypisCiaru(tableLength);
                   }
               //}
           //}
    }
    
    public static void vypisZarovnane(String slovo, int na){
       System.out.print("*");
       System.out.print(slovo);
       if(slovo == null){
           for (int i = 0; i < na - 4; i++) {
               System.out.print(" ");
           }
           return;
       }
       for(int i=0; i < na - slovo.length(); i++){
           System.out.print(" ");
       }
   }
   
   public static void vypisCiaru(int n){
       for(int i=0; i < n; i++){
           System.out.print("*");
       }
       System.out.println("");
   }
}
