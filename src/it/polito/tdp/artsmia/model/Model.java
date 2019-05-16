package it.polito.tdp.artsmia.model;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> grafo;

	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		ArtsmiaDAO dao = new ArtsmiaDAO();
		dao.creaGrafoDaDb(grafo);
		System.out.println("Vertici: "+grafo.vertexSet().size()+" Archi: "+grafo.edgeSet().size());
	}

	public String calcolaComponenteConnessa(String id) {
		String risultato = "";
		this.creaGrafo();		
		ArtsmiaDAO dao = new ArtsmiaDAO();
		List<ArtObject> lista = new LinkedList<>();
		ArtObject o1 = dao.listObjects().get(Integer.parseInt(id));
		BreadthFirstIterator<ArtObject, DefaultWeightedEdge> iterator = new BreadthFirstIterator<>(grafo, o1);
		while(iterator.hasNext()) {
			lista.add(iterator.next());
		}
		risultato="ID corrispondente all'opera: "+o1.getName()+" Componenti connesse: "+lista.size();
		return risultato;
	}

	public boolean matches(String id) {
		return id.matches("\\d+");
	}
	
	

}
