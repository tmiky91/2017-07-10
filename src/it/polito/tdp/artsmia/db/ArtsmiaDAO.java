package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.model.ArtObject;

public class ArtsmiaDAO {

	public Map<Integer, ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		Map<Integer, ArtObject> result = new HashMap<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.put(artObj.getId(), artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void creaGrafoDaDb(SimpleWeightedGraph<ArtObject, DefaultWeightedEdge> grafo) {
		final String sql =	"select o1.object_id as obj1, o2.object_id as obj2, count(*) as peso " + 
							"from exhibitions as e, objects as o1, objects as o2, exhibition_objects as eo1, exhibition_objects as eo2 " + 
							"where e.exhibition_id=eo1.exhibition_id " + 
							"and e.exhibition_id=eo2.exhibition_id " + 
							"and o1.object_id=eo1.object_id " + 
							"and o2.object_id=eo2.object_id " + 
							"and o1.object_id > o2.object_id " + 
							"group by obj1, obj2";
		
		
		Map<ArtObject, ArtObject> identityMap = new HashMap<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				int id1 = res.getInt("obj1");
				int id2 = res.getInt("obj2");
				int peso = res.getInt("peso");
				
				ArtObject o1 = new ArtObject(id1);
				ArtObject o2 = new ArtObject(id2);
				
				if(!identityMap.containsKey(o1)) {
					identityMap.put(o1, o1);
				}
				if(!identityMap.containsKey(o2)) {
					identityMap.put(o2, o2);
				}
				if(!grafo.containsVertex(identityMap.get(o1))) {
					grafo.addVertex(identityMap.get(o1));
				}
				if(!grafo.containsVertex(identityMap.get(o2))) {
					grafo.addVertex(identityMap.get(o2));
				}
				if(!grafo.containsEdge(identityMap.get(o1), identityMap.get(o2)) || !grafo.containsEdge(identityMap.get(o2), identityMap.get(o1))) {
					DefaultWeightedEdge edge = grafo.addEdge(identityMap.get(o1), identityMap.get(o2));
					grafo.setEdgeWeight(edge, peso);
				}
				else {
					DefaultWeightedEdge edge =grafo.getEdge(identityMap.get(o1), identityMap.get(o2));
				}

			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore DB");
		}
		
	}
	
}
