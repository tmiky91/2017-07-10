package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import com.sun.media.sound.AiffFileReader;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> grafo;
	private Map<Integer, ArtObject> idMap;
	private ArtsmiaDAO dao = new ArtsmiaDAO();
	
	public Model() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new HashMap<>();
	}

	public String creaGrafo() {
		String risultato="";
		dao.listObjects(idMap);
		List<EsposizioniComuni> espComuni = dao.getEsposizioniComuni(idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		for(EsposizioniComuni ec: espComuni) {
			Graphs.addEdgeWithVertices(grafo, ec.getO1(), ec.getO2());
			DefaultWeightedEdge edge = grafo.getEdge(ec.getO1(), ec.getO2());
			grafo.setEdgeWeight(edge, ec.getPeso());
//			if(edge==null) {
//				Graphs.addEdge(grafo, ec.getO1(), ec.getO2(), ec.getPeso());
//			}else {
//				grafo.setEdgeWeight(edge, ec.getPeso());
//			}
		}
		risultato+="Grafo Creato. Vertici: "+grafo.vertexSet().size()+" Archi: "+grafo.edgeSet().size();
		return risultato;
	}

	public boolean isDigit(String id) {
		if(id.matches("\\d+")) {
			return true;
		}
		return false;
	}

	public String getConnessione(int id) {
		String risultato="";
		List<ArtObject> connessioni = new LinkedList<>();
		ArtObject o1 = idMap.get(id);
		if(o1!=null) {
			BreadthFirstIterator<ArtObject, DefaultWeightedEdge> iterator = new BreadthFirstIterator<>(grafo, o1);
			while(iterator.hasNext()) {
				connessioni.add(iterator.next());
			}
			risultato+="ID corrispondente all'opera: "+o1.getName()+"\n"+ "Vertici che compongono la componente connessa: "+connessioni.size();
		}else {
			risultato="Errore: ID non presente nel grafo";
		}
		return risultato;
	}

}
