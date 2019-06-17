package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Map<Integer, ArtObject> idMap;
	private SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> grafo;
	
	public Model() {
		idMap = new HashMap<>();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}

	public String creaGrafo() {
		String risultato="";
		ArtsmiaDAO dao = new ArtsmiaDAO();
		dao.listObjects(idMap);
		List<Esibizione> esibizioni = dao.getEsibizioniComuni(idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		for(Esibizione e: esibizioni) {
			DefaultWeightedEdge edge = grafo.getEdge(e.getO1(), e.getO2());
			if(edge==null) {
				Graphs.addEdgeWithVertices(grafo, e.getO1(), e.getO2(), e.getPeso());
			}
		}
		risultato+="Grafo Creato! Vertici: "+grafo.vertexSet().size()+" Archi: "+grafo.edgeSet().size()+"\n";
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
			BreadthFirstIterator<ArtObject, DefaultWeightedEdge> iterator = new BreadthFirstIterator<>(grafo);
			while(iterator.hasNext()) {
				connessioni.add(iterator.next());
			}
			risultato+="ID corrispondente all'opera: "+o1.getName()+"\n"+"Numero di vertici componente connessa: "+connessioni.size();
		}else {
			risultato="Errore: Oggetto non presente nel grafo";
		}
		return risultato;
	}

}
