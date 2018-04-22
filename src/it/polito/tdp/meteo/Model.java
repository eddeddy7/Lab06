package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private List<SimpleCity> soluzione;
	private double punteggio=Double.MAX_VALUE;

	public Model() {

	}

	public String getUmiditaMedia(int mese) {
		MeteoDAO m =new MeteoDAO();
		Map<String, Double> j=m.cercaCcittaPerMese(mese);
		String g="";
		
		for(String q: j.keySet()) {
			g+=q+"  "+j.get(q)+ "\n";
		}


		return g;
	}

	public String trovaSequenza(int mese) {
		MeteoDAO m =new MeteoDAO();
		List<Rilevamento> rilevamenti=m.getRilevamentipermese(mese);
		ArrayList<Citta> cities = null;

		cities = new ArrayList<Citta>();

		
		for (String s : m.getCities())
          	cities.add(new Citta(s));

		for (Citta c : cities) {
         c.setRilevamenti(m.getAllRilevamentiLocalitaMese(mese, c.getNome()));

		}
	
		soluzione=new ArrayList();
		List<SimpleCity> parziale=new ArrayList();
		int step=0;
		recursive(step,parziale,cities);
		
		String result="";
		for(SimpleCity s: soluzione) {
			result+=s.toString();
		}
		result+= "\nCOSTO MINIMO: "+punteggioSoluzione(soluzione);

		return result;
	}

	private void recursive(int step, List<SimpleCity> parziale, List<Citta> cities) {

		
		
		if(step>= NUMERO_GIORNI_TOTALI) {
		if(punteggioSoluzione(parziale)<punteggio) {
			soluzione=new ArrayList(parziale);
			System.out.println(soluzione);
			punteggio=this.punteggioSoluzione(parziale);
			System.out.println(soluzione);
			
		}
		return;
		}
		
		for (Citta citta : cities) {
			SimpleCity sc = new SimpleCity(citta.getNome(), citta.getRilevamenti().get(step).getUmidita());
			parziale.add(sc);
			
			if (controllaParziale(parziale))
			{
				recursive(step + 1, parziale, cities);
			}
			
			parziale.remove(step);
	     	}
		}
		
		
		
	


	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {
   
		double score = 0.0;
		for(SimpleCity s: soluzioneCandidata) {
       	 score+=s.getCosto();
       	 
        }
		int cnt=0;
		for(int i=1;i<soluzioneCandidata.size();i++) {
			if(!soluzioneCandidata.get(i).equals(soluzioneCandidata.get(i-1)))
				cnt++;
		}
		
		score+=(cnt*COST);
		
		return score;
	}

	
	
	
	private boolean controllaParziale(List<SimpleCity> parziale) {
		if (parziale == null)
          	return false;

		
		
for(int i=0;i<parziale.size();i++)
{
	int cnt=0;
	SimpleCity temp = parziale.get(i);
	
		for(int j=0;j<parziale.size();j++) {
			if(temp.equals(parziale.get(j)))
				cnt++;
			
		}
		if(cnt>6)
			return false;
	
}
		
		SimpleCity pr = parziale.get(0);
		int counter = 1;
		for (SimpleCity sc : parziale) {

			if (!pr.equals(sc)) {

				if (counter < NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {

					return false;

				}

				counter = 1;

				pr = sc;

			} else {

				counter++;

			} 
        	   
			}
		return true;
	}
}