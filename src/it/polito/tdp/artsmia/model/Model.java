package it.polito.tdp.artsmia.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> grafo;

	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		ArtsmiaDAO dao = new ArtsmiaDAO();
		dao.creaGrafoDaDb(grafo);
	}

	public String calcolaComponenteConnessa(String id) {
		String risultato = "";
		this.creaGrafo();
		
//		for(ArtObject o2: grafo.vertexSet()) {
//			if(o2.getId()==o1.getId()) {
//				ConnectivityInspector<ArtObject, DefaultWeightedEdge> inspector = new ConnectivityInspector<>(grafo);
//				risultato+=inspector.connectedSetOf(o2).size();
//			}
//		}
		
		ArtsmiaDAO dao = new ArtsmiaDAO();
		ArtObject o1 = dao.listObjects().get(Integer.parseInt(id));
		List<ArtObject> lista = Graphs.neighborListOf(grafo, o1);
		return risultato+="Componenti connesse: "+lista.size();
		
//		Metodo alternativo:
//		ConnectivityInspector<ArtObject, DefaultWeightedEdge> inspector = new ConnectivityInspector<>(grafo);
//		return risultato+=inspector.connectedSetOf(o1).size();

	}

	public boolean matches(String id) {
		return id.matches("\\d+");
	}
	
	

}
