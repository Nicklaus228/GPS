package gps;

import java.awt.Color;

/**
 * Classe modélisant un noeud d'un graphe.
 *
 */
public class Noeud implements Comparable<Noeud> {

	
	private String nom; // Nom du noeud
	private Arc adjacences[]; // Liste des arcs partant de ce noeud
	private int distance; // Distance minimale entre le noeud de départ et ce noeud. 
	private Noeud pred; // Noeud précédent dans le chemin le plus court à partir du noeud de départ.
	private Color couleur; // Couleur du sommet
	
	/**
	 * Constructeur du noeud
	 * @param Nom du noeud
	 */
	public Noeud (String name) {
		this.nom = name;
		this.couleur = Color.WHITE;
		this.distance = Integer.MAX_VALUE; // Distance initialisée à l'infini.
	}
	
	/**
	 * Méthode de l'interface Comparable, utilisée pour comparer deux noeuds, nécessaire pour utiliser PriorityQueue.
	 */
	public int compareTo(Noeud autre) {
		return Integer.compare(this.getDistance(), autre.getDistance());
	}
	
	public String toString() {
		return nom;
	}
	
	// Accesseurs et mutateurs
	public Arc[] getAdjacences() {
		return adjacences;
	}

	public void setAdjacences(Arc[] adjacences) {
		this.adjacences = adjacences;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public Noeud getPred() {
		return pred;
	}

	public void setPred(Noeud pred) {
		this.pred = pred;
	}

	public Color getCouleur() {
		return couleur;
	}

	public void setCouleur(Color couleur) {
		this.couleur = couleur;
	}

	public String getNom() {
		return nom;
	}
		
}
