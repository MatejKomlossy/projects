
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Matej Komlóssy
 */
public class Movies_genres {
    Integer movie_id;
    Integer genre_id;

    public void setMovieId(Integer id){
        if(id == null){
            return;
        }
        this.movie_id = id;
    }

    public void setGenreId(Integer id){
        if(id == null){
            return;
        }
        this.genre_id = id;
    }
              
    /**
     * nájde riadok tabuľky movies_genres podľa id filmu
     * @param id if filmu
     * @return nájdený riadok
     * @throws SQLException 
     */
    public static Movies_genres findByMovieId(int id) throws SQLException{ //najde jeden taky zaznam
        String sql = "SELECT * FROM movies_genres WHERE movie_id = ?";
        Connection c = DbContext.getConnection();
        try(PreparedStatement s = c.prepareStatement(sql)){
            s.setInt(1, id);
            
            return loadOne(s);
        }
        
    }
    
    /**
     * načíta jeden riadok tabuľky movies_genres zo statementu_do inštancie Movies_genres
     * @return načítaný riadok tabuľky
     * @throws SQLException 
     */
    public static  Movies_genres loadOne(PreparedStatement s) throws SQLException {
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
     * načíta jeden riadok z ResultSetu
     * @param rs
     * @return
     * @throws SQLException 
     */
    private static Movies_genres load(ResultSet rs) throws SQLException{
        Movies_genres mg = new Movies_genres();
        mg.movie_id = rs.getInt("movie_id");
        mg.genre_id = rs.getInt("genre_id");
 
        return mg;
    }
               
    /**
     * nájde všetky riadky tabuľky movies_genres podľa id filmu
     * @param id id filmu
     * @return zoznam nájdených riadkov
     * @throws SQLException 
     */
    public static List<Movies_genres> findAllByMovieId(int id) throws SQLException{ //najde vsetky take zaznamy
        String sql = "SELECT * FROM movies_genres WHERE movie_id = ?";
        Connection c = DbContext.getConnection();
        try(PreparedStatement s = c.prepareStatement(sql)){
            return loadAll(s);
        }
    }
    
    /**
     * načíta všetky riadky zo statementu
     * @return načítané riadky
     * @throws SQLException 
     */
    private static List<Movies_genres> loadAll(PreparedStatement s) throws SQLException{
        try(ResultSet rs = s.executeQuery()){
            List<Movies_genres> zaznamy = new ArrayList<>();
            while(rs.next()){
                zaznamy.add(load(rs));
            }  
            return zaznamy;
        }
    }

    /**
     * vloží riadok do tabuľky movies_genres
     * @throws SQLException 
     */
    public void insert() throws SQLException{ //nie je staticka lebo bereme data z tej instancie
        String sql = "INSERT INTO movies_genres(movie_id,genre_id)"+
                " VALUES(?,?)";
        Connection c = DbContext.getConnection();
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,movie_id);
            ps.setInt(2,genre_id);

            ps.executeUpdate();
        }
    }

    /**
     * zmaže všetky riadky tabuľky movies_genres podľa id filmu
     * @param movieId id filmu
     * @throws SQLException 
     */
    public static void deleteByMovieId(Integer movieId) throws SQLException {
        Connection c = DbContext.getConnection();
        String sql = "DELETE FROM movies_genres WHERE movie_id = ?";
        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, movieId);

            ps.executeUpdate();
        }
    }

    public Integer getMovieId() {
        return movie_id;
    }

    public Integer getGenreId() {
        return genre_id;
    }

    
}

