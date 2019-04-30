package it.polito.tdp.artsmia.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private List<ArtObject> artObjects;
	private Graph<ArtObject, DefaultWeightedEdge> graph;
	
	/**
	 * Popola la list artObjects (dal DB) e crea il grafo.
	 */
	
	public void creaGrafo() {
		//LEGGI LA LISTA DEGLI OGGETTI DAL DB
		ArtsmiaDAO artsmiaDao = new ArtsmiaDAO();
		this.artObjects = artsmiaDao.listObjects();
		
		//CREA IL GRAFO
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//AGGIUNGI I VERTICI
//		for (ArtObject ao: this.artObjects) {
//			this.graph.addVertex(ao);
//		}
		Graphs.addAllVertices(this.graph, this.artObjects); //equivale al ciclo for sopra
		
		//AGGIUNGI GLI ARCHI CON IL LORO PESO
		
		for(ArtObject ao: this.artObjects) {
			List<ArtObjectAndCount> connessi = artsmiaDao.listArtObjectAndCount(ao);
			for(ArtObjectAndCount c: connessi) {
				ArtObject dest = new ArtObject(c.getArtObjectId(), null, null, null, 0, null, null, null, null, null, 0, null, null, null, null, null);
				Graphs.addEdge(this.graph, ao, dest, c.getCount());
				System.out.format("(%d, %d) peso %d\n", ao.getId(), dest.getId(), c.getCount());
			}
		}
		/* VERSIONE 1 -poco efficiente **
		for(ArtObject aop : this.artObjects) {
			for(ArtObject aoa : this.artObjects) {
				if(!aop.equals(aoa) && aop.getId()<aoa.getId()) { //con la prima parte dell'if escludo coppie uguali e dato che il grafo non è orientato occorre evitare che si creino due coppie uguali di archi (1-5, 5-1) con la seconda parte dell'if 
					int peso = exhibitionComuni(aop, aoa);
					if(peso!=0) {
//						DefaultWeightedEdge e = this.graph.addEdge(aop, aoa);
//						graph.setEdgeWeight(e, peso);

						System.out.format("(%d, %d) peso %d\n", aop.getId(), aoa.getId(), peso);
						Graphs.addEdge(this.graph, aop, aoa, peso);
					}
				}
			}
		}
		*/
	}
	private int exhibitionComuni(ArtObject aop, ArtObject aoa) {
		ArtsmiaDAO dao = new ArtsmiaDAO();
		int comuni = dao.contaExhibitionComuni(aop, aoa);
		return comuni;
	}

}
