package gps;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Classe modélisant un réseau routier qui est un graphe
 *
 */

class Reseau extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4116329181785741084L;
	
	Noeud listeNoeuds[]; // Liste des noeuds composant le graphe
	int coord_x[]; // Coordonnées en x des noeuds du graphe
	int coord_y[]; // Coordonnées en y des noeuds du graphe
	
	/**
	 * Constructeur d'un réseau routier
	 * @param liste de noeuds composant le graphe
	 * @param coordonnées en X des noeuds composant le graphe
	 * @param coordonnées en Y des noeuds composant le graphe
	 */
	public Reseau(Noeud[] liste, int[] X, int[] Y) {
		this.listeNoeuds = liste;
		this.coord_x = X;
		this.coord_y = Y;
		this.setPreferredSize(new Dimension(580,325)); // Taille du panneau
		this.setBackground(Color.LIGHT_GRAY);
		
		this.setVisible(true);
		
	}
	
	/**
	 * Affichage du réseau
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setFont(new Font("Open Sans", Font.BOLD, 16));
		
		// On parcourt tous les noeuds du réseau
		for (int i = 0; i < listeNoeuds.length; i++) {
			int x = coord_x[i]; // Coordonnée x du noeud 
			int y = coord_y[i]; // Coordonnée y du noeud
			int x2 = -1, y2 = -1, index2 = listeNoeuds.length - 1; // Initialisation de variables à utiliser
			
			Noeud n = listeNoeuds[i]; // Noeud courant
			// On dessine tous les arcs partant du noeud
			for (Arc a : n.getAdjacences()) {
		
				// On trouve les coordonnées du noeud d'arrivée
				for (int index = 0; index < listeNoeuds.length; index++) {
					if(listeNoeuds[index] == a.getDestination()) { // Trouvé !
						x2 = coord_x[index]; // MaJ des coordonnées du noeud d'arrivée
						y2 = coord_y[index];
						index2 = index;
						break; // Pas la peine de continuer si noeud trouvé
					}
				}
				
				// On ne dessine que la première référence de l'arc (pas l'arc réciproque)
				if (index2 > i) { // On vérifie si l'arc n'a pas déjà été dessiné
					Graphics2D g2 = (Graphics2D) g;// OBJECT g2
					g2.setColor(a.getCouleur());
					g2.setStroke(new BasicStroke(4));
					g2.drawLine(x + 15, y + 15, x2 + 15, y2 + 15);
					
				}
				
			}
			g.setColor(n.getCouleur()); // Couleur du noeud
			g.fillOval(x, y, 30, 30); // Dessin du noeud à ses coordonnées
			g.setColor(Color.BLACK);
			g.drawString(n.getNom(), x + 10, y + 20); // On écrit le nom du noeud au centre, en noir
			g.drawOval(x, y, 30, 30); // Dessin du contour du noeud en noir
			
		}
	}
	
	
}
