import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.sql.Timestamp;
import java.util.Set;

/**
 * 
 * @author Matej Komlóssy
 */
class OrderOperations {
    
    /**
     * vytvorí objednávku
     * @param cu zákazník,ktorý vytvára objednávku
     * @param s predstavenie, na ktoré sa objednávka vytvára
     * @param typyListkov všetky možné typy lístkov
     * @param pocty zadané počty lístkov každého typu
     * @return vztvorená objednávka
     * @throws SQLException
     * @throws OrderException 
     */
    public static Order createOrder(Customer cu, Screening s, List<TicketType> typyListkov, List<Integer> pocty) throws SQLException, OrderException{
        Connection c = DbContext.getConnection();
        Order o = new Order();
        
        c.setAutoCommit(false);
        c.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try{
            o = doCreateOrder(cu, s, typyListkov, pocty);
            c.commit();
            System.out.println("objednavka bola vytvorena");
        }
        catch(SQLException e){
            c.rollback();
            
            if(e.getSQLState().equals("40001")){
                createOrder(cu, s, typyListkov, pocty); //zopakujem
                //throw new OrderException("Serializable error");
            }
            else{
                throw e;
            }
        }
        catch(Exception e){
            c.rollback();
            throw e;
        }
        finally{
            c.setAutoCommit(true);
        }
        
        return o;
    }
    
    /**
     * vytvorí objednávku, funkcia je volaná funkciou createOrder
     * @param cu zákazník,ktorý vytvára objednávku
     * @param s predstavenie, na ktoré sa objednávka vytvára
     * @param typyListkov všetky možné typy lístkov
     * @param pocty zadané počty lístkov každého typu
     * @return vytvorená objednávka
     * @throws SQLException
     * @throws OrderException 
     */
    public static Order doCreateOrder(Customer cu, Screening s, List<TicketType> typyListkov, List<Integer> pocty) throws SQLException, OrderException{
        Connection c = DbContext.getConnection();
        
        List<Integer> lst = s.timeAndTicketCheck(); //ci sa da objednavat a kolko je volnych listkov na to predstavenie
        if(lst.get(0) == 1){
            throw new OrderException("uz sa neda objednavat na toto predstavenie");
        }

        int ticketCount = 0;
        for(Integer pocet : pocty){
            ticketCount += pocet;
        }

        Integer numberOfFreeTickets = lst.get(1);
        if(numberOfFreeTickets == 0) throw new OrderException("uz nie su volne ziadne listky");
        if(ticketCount > numberOfFreeTickets){
            throw new OrderException("tolko listkov nie je volnych, je ich iba "+ numberOfFreeTickets);
        }

        //vytvorit objednvaku
        Order o = new Order();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        o.setCustomerId(cu.getId());
        o.setScreeningId(s.getId());
        o.setCreatedAt(timestamp);
        o.setIsConfirmed(false);
        o.insert(); //teraz dostane id

        //pridat listky bez seat_id
        int orderId = o.getId();
        for(int i = 0; i < typyListkov.size(); i++){
            TicketType tt = typyListkov.get(i);
            int ticketTypeId = tt.getId();
            BigDecimal soldFor = tt.getPrice(); 
            int pocet = pocty.get(i);
            for (int j = 0; j < pocet; j++) {
                Ticket t = new Ticket();
                t.setScreeningId(s.getId());
                t.setTicketTypeId(ticketTypeId);
                t.setOrderId(orderId);
                t.setCustomerId(cu.getId()); 
                t.setSoldFor(soldFor);
                
                t.insert();
            }
        }
        
        return o;
    }
    
    /**
     * potvrdí objednávku
     * @param selectedSeatIds id zvolených miest
     * @param s predstavenie, na ktoré je objednávka vytvorená
     * @param o objednávka, ktorá sa potvrdzuje
     * @throws SQLException
     * @throws SeatException
     * @throws OrderException 
     */
    public static void confirmOrder(List<Integer> selectedSeatIds, Screening s, Order o) throws SQLException, SeatException, OrderException{
        Connection c = DbContext.getConnection();
        
        c.setAutoCommit(false);
        c.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        try{         
            Set<Integer> freeSeatIds = s.setOfFreeSeats(); //ci su data stale rovnake
            for(Integer id : selectedSeatIds){
                if(freeSeatIds.contains(id) == false){
                    c.rollback(); //aby sa ukoncila transakcia
                    throw new SeatException("zvolene miesta uz nie su volne alebo ste zadali zle cislo miesta.");
                }
            }
        
            List<Ticket> ticketsForUpdate = Ticket.findAllByScreeningAndOrder(s.getId(), o.getId());
            for (int i = 0; i < ticketsForUpdate.size(); i++) { //delete from tickets t where t.customer_id = cusId
                Ticket ticket = ticketsForUpdate.get(i);        //and t.screening_id = s.id and t.order_id = o.id
                ticket.setSeatId(selectedSeatIds.get(i));       //a nanovo insert podla selectedSeatIds a ostatnych id...to by sa tiez dalo
                ticket.update();
            }
            if(o.isValid() == false){  //ci je order platna este
                throw new OrderException("objednavke vyprsal cas platnosti");
            }
            o.setIsConfirmed(true);//potvrdi sa
            o.update();
            
            c.commit();
            System.out.println("objednavka bola potvrdena");
        }
        catch(SQLException e){
            c.rollback();
            
            if(e.getSQLState().equals("40001")){
                throw new SeatException("zvolene miesta uz nie su volne");
                //confirmOrder(selectedSeatIds, s, o); 
            }
            else{
                throw e;
            }
        }
        catch(Exception e){
            c.rollback();
            throw e;
        }
        finally{
            c.setAutoCommit(true);
        }
    }
    
    
    /**
     * vymaže neplatné objednávky
     * @throws SQLException 
     */
    public static void deleteInvalidOrders() throws SQLException{
       Connection c = DbContext.getConnection();
        String sql = "DELETE FROM orders o WHERE o.is_confirmed = false "
                + "AND current_timestamp - o.created_at > INTERVAL '10 minutes'";
        try(Statement s = c.createStatement()){
            s.executeUpdate(sql);
        }
    }
    
}
