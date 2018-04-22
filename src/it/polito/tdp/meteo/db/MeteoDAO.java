package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";
		

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		final String sql = "SELECT Data, Umidita FROM situazione WHERE localita = ? and  MONTH(Data) = ? ";



		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();



		try {

			Connection conn = DBConnect.getInstance().getConnection();

			PreparedStatement st = conn.prepareStatement(sql);



			st.setString(1, localita);

			st.setInt(2, mese);



			ResultSet rs = st.executeQuery();



			while (rs.next()) {



				Rilevamento r = new Rilevamento(localita, rs.getDate("Data"), rs.getInt("Umidita"));

				rilevamenti.add(r);

			}
			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);

		}
	}

	public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {

		return 0.0;
	}

	public Map<String, Double> cercaCcittaPerMese(int mese) {
		final String sql="SELECT localita, AVG(umidita) AS Media FROM situazione WHERE MONTH(Data) = ?  GROUP BY localita";
		 Map <String , Double> medie= new TreeMap<String, Double>();
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			ResultSet rs = st.executeQuery();
			

            
			 List <Rilevamento>rilevamenti =new ArrayList<Rilevamento>();
			
			while (rs.next()) {
           
			medie.put(rs.getString("Localita"), rs.getDouble("Media"));
		
			}
			conn.close();
		}
		catch(SQLException e) {
 //  System.out.println("ERROREEEEEEEE");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		return medie;
	}

	public List<Rilevamento> getRilevamentipermese(int mese) {
		final String sql = "SELECT Localita, Data, Umidita FROM situazione WHERE MONTH(Data) = ? ORDER BY data ASC";
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}


	

	
	}

	public List<String> getCities() {

		final String sql = "SELECT DISTINCT localita FROM situazione";



		try {

			List<String> cities = new ArrayList<String>();



			Connection conn = DBConnect.getInstance().getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();



			while (rs.next()) {

				cities.add(rs.getString("localita"));

			}



			conn.close();

			return cities;



		} catch (SQLException e) {

			e.printStackTrace();

			throw new RuntimeException(e);

		}

	}


}
