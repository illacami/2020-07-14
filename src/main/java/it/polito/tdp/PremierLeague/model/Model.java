package it.polito.tdp.PremierLeague.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
	
	public int getEdgeSize() {
		return grafo.edgeSet().size();
	}
	
	public void creaGrafo() {
		
		grafo = new SimpleDirectedWeightedGraph<Team,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
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
	
	public Map<Team, Double> classifica(Team squadra){
		
		Team team = idTeam.get(squadra.getTeamID());
		Map<Team, Double> classifica = new HashMap<Team, Double>();
		System.out.println(""+team.getPunteggio());
		for(Team t : idTeam.values()) {
			System.out.println(""+t.getPunteggio());	
			if(!t.equals(team)) {
				if(t.getPunteggio() > team.getPunteggio())
					classifica.put(t,grafo.getEdgeWeight(grafo.getEdge(t, team)));
				else if(t.getPunteggio() < team.getPunteggio())
					classifica.put(t, -grafo.getEdgeWeight(grafo.getEdge(team, t)));
				else 
					classifica.put(t, 0.0);
			}
		}
		for(Double punteggio : classifica.values())
			System.out.println("\n"+punteggio);
		
		return classifica;
	}
	
}
