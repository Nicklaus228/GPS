package gps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Classe contenant les m�thodes statiques utilis�es pour le calcul du chemin le plus court.
 * Impl�mente l'algorithme de Dijkstra en utilisant une file de priorit�s. 
 * Code r�utilis�. Voir r�f�rence dans le rapport du projet.
 */
class Dijkstra {

	/**
	 * Calcule les distances minimales de la source � chaque noeud du graphe.
	 * Met � jour pour chaque noeud l'attribut distance qui est la distance minimale.
	 * Met � jour pour chaque noeud l'attribut predecesseur qui est le noeud pr�c�dent dans le chemin le plus court.
	 * @param source : Noeud de d�part dans le graphe.
	 */
	static void calculChemins(Noeud source) {
		
		source.setDistance(0); // La distance de la source � la source est 0 : initialisation de l'algorithme.
		
		PriorityQueue<Noeud> fileNoeuds = new PriorityQueue<Noeud>(); // File de priorit� utilis�e dans l'algorithme.
		fileNoeuds.add(source); 
		
		while(!fileNoeuds.isEmpty()) { // Tant que la file n'est pas vide 
			
			Noeud u = fileNoeuds.poll(); // On r�cup�re le noeud de distance minimale
			
			// On parcourt tous les arcs sortant du noeud u 
			for (Arc a : u.getAdjacences()) {
				Noeud v = a.getDestination(); // Noeud v reli� � u par l'arc a
				int poids = a.getPoids();
				int distanceDeSource = u.getDistance() + poids; // La distance de v � la source = distance de u + poids de l'arc
				if (distanceDeSource < v.getDistance()) { // Si la nouvelle distance est minimale, maj de la distance.
					fileNoeuds.remove(v);
					v.setDistance(distanceDeSource); // Nouvelle distance minimale de la source � v
					v.setPred(u); // u est le noeud pr�c�dent dans le chemin le plus court depuis la source
					fileNoeuds.add(v);
				}
			}
		}
	}
	
	/**
	 * Retourne le chemin le plus court de la source (d�j� d�finie) au noeud pass� en argument.
	 * @param arrivee: Point d'arriv�e.
	 * @return La liste de noeuds composant le chemin le plus court.
	 */
	static List<Noeud> plusCourtChemin(Noeud arrivee) {
		List<Noeud> chemin = new ArrayList<Noeud>(); // Chemin le plus court
		
		for (Noeud  noeud = arrivee; noeud != null; noeud = noeud.getPred()) {
			chemin.add(noeud);
			
		}
		Collections.reverse(chemin); // Remet le chemin dans le bon odre (de d�part � arriv�e)
		return chemin;
	}
	
}
