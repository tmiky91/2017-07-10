package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private Map<Integer, ArtObject> idMap;
	
	public Model() {
		idMap = new HashMap<Integer, ArtObject>();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}
	
	public void creaGrafo() {
		ArtsmiaDAO dao = new ArtsmiaDAO();
		dao.listObjects(idMap);
		
		//aggiungo i vertici
		Graphs.addAllVertices(grafo, idMap.values());
		//aggiungo gli archi
		List<Adiacenza> adj = dao.listAdiacenze();
		for(Adiacenza a: adj) {
			ArtObject source = idMap.get(a.getEo1());
			ArtObject dest = idMap.get(a.getEo2());
			try {
			Graphs.addEdge(grafo, source, dest, a.getPeso());
			}catch(Throwable t) {
				
			}
		}
	}

	public List<> getVertexSets() {
		return grafo.vertexSet();
	}

	public List<> getEdgeSets() {
		return grafo.edgeSet();
	}
	
	

}
