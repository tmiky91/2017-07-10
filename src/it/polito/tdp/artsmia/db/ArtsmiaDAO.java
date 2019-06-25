package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.EsibizioniComuni;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects(Map<Integer, ArtObject> idMap) {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("object_id"))) {
					ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
							res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
							res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
							res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
					
					result.add(artObj);
					idMap.put(res.getInt("object_id"), artObj);
				}

				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<EsibizioniComuni> getEsibizioniComuni(Map<Integer, ArtObject> idMap) {
		final String sql=	"select eo1.object_id as id1, eo2.object_id as id2, count(*) as peso " + 
							"from exhibition_objects as eo1, exhibition_objects as eo2 " + 
							"where eo1.object_id > eo2.object_id " + 
							"and eo1.exhibition_id=eo2.exhibition_id " + 
							"group by id1, id2";
		List<EsibizioniComuni> result = new LinkedList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				ArtObject o1 = idMap.get(res.getInt("id1"));
				ArtObject o2 = idMap.get(res.getInt("id2"));
				if(o1!=null && o2!=null) {
					EsibizioniComuni ec = new EsibizioniComuni(o1, o2, res.getDouble("peso"));
					result.add(ec);
				}

				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
