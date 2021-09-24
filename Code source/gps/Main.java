package gps;

/**
 * Classe Main: point d'entrée de l'application, c'est là qu'on définit le réseau à partir duquel
 * instancier un objet MainFrame. 
 * Un réseau par défaut est déjà défini.
 * @author Charles
 *
 */
public class Main {

	/**
	 * Création du réseau par défaut.
	 * @return Reseau (par défaut)
	 */
	static private Reseau reseauParDefaut() {
		// Définition des différents noeuds du graphe.
		Noeud v0 = new Noeud("A");
		Noeud v1 = new Noeud("B");
		Noeud v2 = new Noeud("C");
		Noeud v3 = new Noeud("D");
		Noeud v4 = new Noeud("E");
		Noeud v5 = new Noeud("F");
		Noeud v6 = new Noeud("G");
		Noeud v7 = new Noeud("H");
		Noeud v8 = new Noeud("I");
		Noeud v9 = new Noeud("J");
		
		// Définition des listes d'adjacences des différents noeuds.
		v0.setAdjacences(new Arc[]{ new Arc(v1, 5),new Arc(v4,4),new Arc(v7, 15)});
		v1.setAdjacences(new Arc[]{ new Arc(v0, 5),new Arc(v2, 7),new Arc(v4, 3), new Arc(v3,3) });
		v2.setAdjacences(new Arc[]{ new Arc(v3,10),new Arc(v1, 7), new Arc(v9,8) });
		v3.setAdjacences(new Arc[]{ new Arc(v1, 3),new Arc(v2, 10),new Arc(v6, 2) });
		v4.setAdjacences(new Arc[]{ new Arc(v0, 4),new Arc(v1, 3),new Arc(v5, 3),new Arc(v7, 6)});
		v5.setAdjacences(new Arc[]{ new Arc(v4, 3),new Arc(v8, 5),new Arc(v6, 4)});
		v6.setAdjacences(new Arc[]{ new Arc(v5, 4),new Arc(v3, 2),new Arc(v9, 5)});
		v7.setAdjacences(new Arc[]{ new Arc(v0, 15),new Arc(v4, 6),new Arc(v8, 6)});
		v8.setAdjacences(new Arc[]{ new Arc(v7, 6),new Arc(v9, 7),new Arc(v5, 5)});
		v9.setAdjacences(new Arc[]{ new Arc(v8, 7),new Arc(v6, 5),new Arc(v2, 8)});

		// Création des listes pour définir un nouvel objet Reseau.
		Noeud[] vertices = { v0, v1, v2, v3, v4,v5,v6,v7,v8,v9};	
		int[] coor_x = {35,200,500,350,75,225,350,35,225,500}; // Coordonnées X des noeuds du réseau sur le panneau
		int[] coor_y = {35,35,35,90,150,150,150,275,275,275}; // Coordonnées Y
		
		return  new Reseau(vertices, coor_x, coor_y); 
	}
	
	public static void main(String[] args) {
		
		Reseau test = reseauParDefaut();
		MainFrame fen = new MainFrame(test);
	}

}
