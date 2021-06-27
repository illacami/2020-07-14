package it.polito.tdp.PremierLeague.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	
	private Map<Integer, Team> idTeam;
	private Graph<Team, DefaultWeightedEdge> grafo;

	public Model() {
		dao = new PremierLeagueDAO();
		idTeam = new HashMap<Integer, Team>();
		dao.listAllTeams(idTeam);
	}
	
	public Collection<Team> getVertici(){
		return idTeam.values();
	}
	
	public void creaGrafo() {
		
		grafo = new SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici
		Graphs.addAllVertices(grafo, this.getVertici());
		
		//aggiungo archi
		for(Team t1 : this.getVertici()) {
			for(Team t2 : this.getVertici()) {
				if(t1!=null && t2!=null && !t1.equals(t2) && !grafo.containsEdge(t1, t2) && !grafo.containsEdge(t2,t1)) {
//					System.out.println(t1.getPunteggio()-t2.getPunteggio());
					if(t1.getPunteggio()-t2.getPunteggio() > 0)
						Graphs.addEdgeWithVertices(grafo, t1, t2, t1.getPunteggio()-t2.getPunteggio());
					
					if(t1.getPunteggio()-t2.getPunteggio() < 0)
						Graphs.addEdgeWithVertices(grafo, t2, t1, t2.getPunteggio()-t1.getPunteggio());
					
				}
			}
		}
		
		System.out.println("# Vertici: "+grafo.vertexSet().size()+"\n# Archi: "+grafo.edgeSet().size());
	}
	
}
