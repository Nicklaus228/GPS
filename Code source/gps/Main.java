package gps;

/**
 * Classe Main: point d'entr�e de l'application, c'est l� qu'on d�finit le r�seau � partir duquel
 * instancier un objet MainFrame. 
 * Un r�seau par d�faut est d�j� d�fini.
 * @author Charles
 *
 */
public class Main {

	/**
	 * Cr�ation du r�seau par d�faut.
	 * @return Reseau (par d�faut)
	 */
	static private Reseau reseauParDefaut() {
		// D�finition des diff�rents noeuds du graphe.
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
		
		// D�finition des listes d'adjacences des diff�rents noeuds.
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

		// Cr�ation des listes pour d�finir un nouvel objet Reseau.
		Noeud[] vertices = { v0, v1, v2, v3, v4,v5,v6,v7,v8,v9};	
		int[] coor_x = {35,200,500,350,75,225,350,35,225,500}; // Coordonn�es X des noeuds du r�seau sur le panneau
		int[] coor_y = {35,35,35,90,150,150,150,275,275,275}; // Coordonn�es Y
		
		return  new Reseau(vertices, coor_x, coor_y); 
	}
	
	public static void main(String[] args) {
		
		Reseau test = reseauParDefaut();
		MainFrame fen = new MainFrame(test);
	}

}
