package gps;

import java.awt.Color;

/**
 * Classe mod�lisant un noeud d'un graphe.
 *
 */
public class Noeud implements Comparable<Noeud> {

	
	private String nom; // Nom du noeud
	private Arc adjacences[]; // Liste des arcs partant de ce noeud
	private int distance; // Distance minimale entre le noeud de d�part et ce noeud. 
	private Noeud pred; // Noeud pr�c�dent dans le chemin le plus court � partir du noeud de d�part.
	private Color couleur; // Couleur du sommet
	
	/**
	 * Constructeur du noeud
	 * @param Nom du noeud
	 */
	public Noeud (String name) {
		this.nom = name;
		this.couleur = Color.WHITE;
		this.distance = Integer.MAX_VALUE; // Distance initialis�e � l'infini.
	}
	
	/**
	 * M�thode de l'interface Comparable, utilis�e pour comparer deux noeuds, n�cessaire pour utiliser PriorityQueue.
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
