package gps;

import java.awt.Color;

/**
 * Classe modélisant un arc dans un graphe.
 *
 */
public class Arc {

	private Noeud destination; // Noeud vers lequel pointe cet arc.
	private int poids; // Poids de l'arc.
	private Color couleur; // Couleur de l'arc

	/**
	 * Constructeur d'un arc avec deux arguments.
	 * @param dest : Noeud de destination.
	 * @param poids : Poids de l'arc. 
	 */
	public Arc(Noeud dest, int poids) {
		this.destination = dest;
		this.poids = poids;
		this.couleur = Color.BLACK;
	}
	
	// Accesseurs et mutateurs
	public int getPoids() {
		return poids;
	}

	public void setPoids(int poids) {
		this.poids = poids;
	}

	public Color getCouleur() {
		return couleur;
	}

	public void setCouleur(Color couleur) {
		this.couleur = couleur;
	}

	public Noeud getDestination() {
		return destination;
	}

}
