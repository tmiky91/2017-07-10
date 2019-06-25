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
	}

	public String creaGrafo() {
		String risultato="";
		ArtsmiaDAO dao = new ArtsmiaDAO();
		dao.listObjects(idMap);
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, idMap.values());
		List<EsibizioniComuni> esibComuni = dao.getEsibizioniComuni(idMap);
		for(EsibizioniComuni ec: esibComuni) {
			DefaultWeightedEdge edge = grafo.getEdge(ec.getO1(), ec.getO2());
			if(edge==null) {
				Graphs.addEdgeWithVertices(grafo, ec.getO1(), ec.getO2(), ec.getPeso());
			}
		}
		risultato+="Grafo Creato! Vertici: "+grafo.vertexSet().size()+" Archi: "+grafo.edgeSet().size();
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
			while (iterator.hasNext()) {
				connessioni.add(iterator.next());
			}
			risultato+="ID corrispondente all'opera: "+o1.getTitle()+"\n"+"Numero vertici componente connessa: "+connessioni.size();
		}else {
			risultato+="Errore: ID non presente nel grafo";
		}
		
		return risultato;
	}

}
