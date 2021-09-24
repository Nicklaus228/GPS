package gps;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Classe mod�lisant un r�seau routier qui est un graphe
 *
 */

class Reseau extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4116329181785741084L;
	
	Noeud listeNoeuds[]; // Liste des noeuds composant le graphe
	int coord_x[]; // Coordonn�es en x des noeuds du graphe
	int coord_y[]; // Coordonn�es en y des noeuds du graphe
	
	/**
	 * Constructeur d'un r�seau routier
	 * @param liste de noeuds composant le graphe
	 * @param coordonn�es en X des noeuds composant le graphe
	 * @param coordonn�es en Y des noeuds composant le graphe
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
	 * Affichage du r�seau
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setFont(new Font("Open Sans", Font.BOLD, 16));
		
		// On parcourt tous les noeuds du r�seau
		for (int i = 0; i < listeNoeuds.length; i++) {
			int x = coord_x[i]; // Coordonn�e x du noeud 
			int y = coord_y[i]; // Coordonn�e y du noeud
			int x2 = -1, y2 = -1, index2 = listeNoeuds.length - 1; // Initialisation de variables � utiliser
			
			Noeud n = listeNoeuds[i]; // Noeud courant
			// On dessine tous les arcs partant du noeud
			for (Arc a : n.getAdjacences()) {
		
				// On trouve les coordonn�es du noeud d'arriv�e
				for (int index = 0; index < listeNoeuds.length; index++) {
					if(listeNoeuds[index] == a.getDestination()) { // Trouv� !
						x2 = coord_x[index]; // MaJ des coordonn�es du noeud d'arriv�e
						y2 = coord_y[index];
						index2 = index;
						break; // Pas la peine de continuer si noeud trouv�
					}
				}
				
				// On ne dessine que la premi�re r�f�rence de l'arc (pas l'arc r�ciproque)
				if (index2 > i) { // On v�rifie si l'arc n'a pas d�j� �t� dessin�
					Graphics2D g2 = (Graphics2D) g;// OBJECT g2
					g2.setColor(a.getCouleur());
					g2.setStroke(new BasicStroke(4));
					g2.drawLine(x + 15, y + 15, x2 + 15, y2 + 15);
					
				}
				
			}
			g.setColor(n.getCouleur()); // Couleur du noeud
			g.fillOval(x, y, 30, 30); // Dessin du noeud � ses coordonn�es
			g.setColor(Color.BLACK);
			g.drawString(n.getNom(), x + 10, y + 20); // On �crit le nom du noeud au centre, en noir
			g.drawOval(x, y, 30, 30); // Dessin du contour du noeud en noir
			
		}
	}
	
	
}
