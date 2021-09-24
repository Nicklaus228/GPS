package gps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Interface (fen�tre) principale de l'application, d�rive de JFrame.
 * Contient toutes les fonctions n�cessaires au d�roulement attendu du programme.
 *
 */
public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7921238150593723661L;
	
	// D�finition couleurs utilis�es
	static Color BLEU_CIEL = new Color(35, 142, 181); // D�finition nouvelle couleur 
	
	private JComboBox<Noeud> departs,arrivees; // Listes d�roulantes
	private JButton boutonGo;
	private Reseau reseau; // R�seau routier consid�r�
	private JPanel container = new JPanel(); // Panneau principal
	private Noeud depart, arrivee, position; 
	private ArrayList<Arc> listeRoutes; // liste des arcs du graphe
	private boolean estArrive = false; // True lorsqu'on arrive � destination
	private Thread animation; // Pile d'ex�cution pour l'animation 
	
	// Variables pour la gestion des congestions
	private Arc derniereCong, lastReciproque; 
	private int poidsOrigine, poidsReciproque; 
	private int nombreCong;
	
	/**
	 * Constructeur, les diff�rents composants sont initialis�s
	 * @param reseau: R�seau consid�r� par l'application
	 */
	public MainFrame(Reseau reseau) {
		this.setTitle("GPS");
		this.setSize(600, 400); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setLocationRelativeTo(null); // Place la fen�tre au centre de l'�cran
		this.setResizable(false); // Fixe la taille de la fen�tre
		this.reseau = reseau;
		
		// Initialisation de tous les composants de la fen�tre et leurs Listeners
		initFrame(); 
		
		this.setContentPane(container);
		this.setVisible(true); // Affiche la fen�tre
		
	}
	
	/**
	 * Fonction responsable de l'initialisation des composants.
	 * Initialise les deux listes d�roulantes � partir du r�seau ainsi que le bouton GO. 
	 * Initialise la liste de Routes utilis�e pour la gestion des congestions. 
	 * Impl�mente les Listeners des diff�rents composants.
	 */
	private void initFrame() {
		container.setLayout(new BorderLayout()); // D�finition du layout utilis�
		
		// Cr�ation du panneau sup�rieur
		JPanel panneauSup = new JPanel(); 
		panneauSup.setPreferredSize(new Dimension(580, 40));
		panneauSup.setBackground(Color.DARK_GRAY);
		
		// Initialisation des composants du panneau sup�rieur
		Dimension tailleListes = new Dimension(260, 30); // taille des listes
		// Cr�ation des listes � partir des sommets du r�seau
		departs = new JComboBox<Noeud>(reseau.listeNoeuds);
		departs.setPreferredSize(tailleListes);
		arrivees = new JComboBox<Noeud>(reseau.listeNoeuds);
		arrivees.setPreferredSize(tailleListes);
		// Cr�ation du bouton
		boutonGo = new JButton("GO");
		boutonGo.setFont(new Font("Cambria Math", Font.BOLD, 10));
		boutonGo.setForeground(Color.RED); // Couleur de "GO"
		boutonGo.setPreferredSize(new Dimension(50, 30)); // Taille du bouton
		
		// Ajout des composants au panneau sup�rieur, dans l'ordre
		panneauSup.add(departs);
		panneauSup.add(arrivees);
		panneauSup.add(boutonGo);
		
		// Cr�ation du panneau inf�rieur
		JPanel panneauInf = new JPanel();
		panneauInf.setPreferredSize(new Dimension(580, 335));
		panneauInf.setBackground(Color.DARK_GRAY);
		
		// Bordure pour l'affichage du r�seau
		reseau.setBorder(BorderFactory.createLineBorder(BLEU_CIEL));
		
		panneauInf.add(reseau); // On ajoute le r�seau (un JPanel) au panneau inf�rieur
		
		// On ajoute les deux panneaux au panneau principal
		container.add(panneauSup, BorderLayout.NORTH);
		container.add(panneauInf, BorderLayout.SOUTH);
		
		// Initialisation de la liste des arcs
		listeRoutes = new ArrayList<Arc>();
		for (Noeud n: reseau.listeNoeuds) {
			for (Arc a: n.getAdjacences()) {
				listeRoutes.add(a);
			}
		}
		nombreCong = listeRoutes.size(); // Sauvegarde du nombre r�el de congestions
		for (int i = 0; i < nombreCong*0.25; i++) {
			listeRoutes.add(null); // Ajout de "non-congestions", dont le nombre est �gal au quart de congestions r�elles
		}
		
		// Impl�mentation des Listeners
		departs.addActionListener(new ActionListener() { // Listener liste "departs"
			public void actionPerformed(ActionEvent e) {
				if (depart != null && depart != arrivee) {
					depart.setCouleur(Color.WHITE); // On r�initialise la couleur du noeud de d�part pr�c�dent
				}
				depart = (Noeud)departs.getSelectedItem(); // Le noeud s�lectionn� est le noeud de d�part
				depart.setCouleur(BLEU_CIEL); // On affiche le noeud de d�part en changeant la couleur
				reseau.repaint(); // Repeint le r�seau pour voir les changements
			}
		});
		
		arrivees.addActionListener(new ActionListener() { // Listener liste "arrivees"
			public void actionPerformed(ActionEvent e) { // M�me chose que le Listener pr�c�dent
				if (arrivee != null && arrivee != depart) {
					arrivee.setCouleur(Color.WHITE);
				}
				arrivee = (Noeud)arrivees.getSelectedItem(); // Le noeud s�lectionn� est le noeud d'arriv�e
				arrivee.setCouleur(Color.RED);
				reseau.repaint();
			}
		});
		
		boutonGo.addActionListener(new ActionListener(){ // Listener bouton
			public void actionPerformed(ActionEvent e) {
				try {
					if (depart == null || arrivee == null) {
						throw new NullPointerException();
					}
					if (depart == arrivee) {
						throw new TrajetNulException();
					}
					position = depart; // Initialisation de position
					afficherChemin(); // Affichage du chemin le plus court pour l'itin�raire choisi
					// reseau.repaint();
					animation = new Thread(new PlayAnimation()); // Cr�ation d'un nouveau thread pour l'animation (apr�s d�bug)
					animation.start(); // D�marrage de l'animation de navigation
				} catch (NullPointerException ex1) { // D�clench�e si un des points n'est pas d�fini
					JOptionPane.showMessageDialog(null, "Vous devez choisir un point de d�part et d'arriv�e !",
												"Trajet ind�fini", // Titre de la fen�tre
												JOptionPane.ERROR_MESSAGE); // Icone utilis�e
				} catch (TrajetNulException ex2) { // D�clench�e si un des points n'est pas d�fini
					
				}
				
			}
		});
	}
	
	/**
	 * Fonction pour l'affichage du chemin le plus court sur le r�seau (coloriage du chemin).
	 */
	private void afficherChemin() {
		// Recoloriage noeuds de d�part et d'arriv�e
		position.setCouleur(BLEU_CIEL); 
		arrivee.setCouleur(Color.RED);
		
		/* R�initialisation des distances et pr�decesseurs des noeuds. 
		 * N�cessaire avant de relancer l'algorithme de Dijkstra pour ne pas fausser le calcul
		 * avec des valeurs correspondant � l'itin�raire pr�c�dent.
		*/
		for (Noeud n : reseau.listeNoeuds) {
			n.setDistance(Integer.MAX_VALUE);
			n.setPred(null);
		}
		// R�initialisation de la couleur des arcs
		for (int i = 0; i < nombreCong; i++) {
			listeRoutes.get(i).setCouleur(Color.BLACK);
		}
		// Recoloriage des congestions, car r�initialis�es par la boucle pr�c�dente.
		if(derniereCong != null && lastReciproque != null) {
			derniereCong.setCouleur(Color.RED);
			lastReciproque.setCouleur(Color.RED);
		}
		
		// MaJ les distances des noeuds avec l'algo de Dijkstra
		Dijkstra.calculChemins(position);
		
		// On parcourt le chemin le plus court
		for (Noeud noeud: Dijkstra.plusCourtChemin(arrivee)) {
				
			if(noeud.getPred() == null) { // Vrai pour le premier noeud du chemin  
				continue; // Car la boucle suivante g�n�re une erreur dans ce cas
			}
			// On trouve l'arc venant du noeud pr�c�dent
			for(Arc arc : noeud.getPred().getAdjacences()) {
				
				if (arc.getDestination() == noeud) {
					arc.setCouleur(Color.YELLOW); // Trouv�, on change sa couleur
					break; // Pas la peine de continuer
				}
			}
			// On trouve l'arc allant vers le noeud pr�c�dent
			for (Arc arc : noeud.getAdjacences()) {
				if(arc.getDestination() == noeud.getPred()) {
					arc.setCouleur(Color.YELLOW); // Trouv�, on change sa couleur
					break;
				}
			}
		}
		reseau.repaint(); // R�affichage du r�seau
		
	}
	
	/**
	 * Fonction qui cr�e une congestion en modifiant le poids et la couleur de l'arc d�sign�.
	 * Le programme garde des r�f�rences de la derni�re congestion enregistr�e et son poids, de m�me que les arcs
	 * r�ciproques correspondants.
	 * Ces r�f�rences servent � r�tablir l'�tat initial du graphe avant de relancer une nouvelle congestion. 
	 * @param arc: Arc dans le graphe � partir duquel cr�er la congestion.
	 */
	private void creerCongestion(Arc arc) {
		// MaJ des r�f�rences
		derniereCong = arc; // Sauvegarde de la congestion d�finie.
		poidsOrigine = arc.getPoids(); // Sauvegarde du poids d'origine de l'arc.
		arc.setPoids(500); // Poids tr�s grand, afin que l'algorithme ne passe pas par l�.
		arc.setCouleur(Color.RED); // Coloriage de l'arc.
		
		// Recherche du noeud de d�part de l'arc
		Noeud departArc = null;
		for (Noeud noeud: reseau.listeNoeuds) { // On parcourt tous les noeuds du graphe
			for (Arc arcSortant : noeud.getAdjacences()) { // Parcours des arcs sortant du noeud
				if (arcSortant == arc) { // Recherche d'une correspondance avec l'arc de la congestion
					departArc = noeud; // Arc de d�part trouv�
					break; // Pas besoin de continuer
				}
			}
			if (departArc != null)
				break; // Sortir de la boucle principale si departArc est trouv�.
		}
		
		// Recherche de l'arc r�ciproque, m�mes actions
		for (Arc reciproque : arc.getDestination().getAdjacences() ) { 
			if (reciproque.getDestination() == departArc) { 
				lastReciproque = reciproque; 
				poidsReciproque = reciproque.getPoids();
				reciproque.setPoids(500); // Poids tr�s grand
				reciproque.setCouleur(Color.RED); // On colorie l'arc r�ciproque
				break;
			}
		}
	}
	
	/**
	 * Fonction principale responsable de la navigation.
	 * Lorsqu'elle appel�e on observe une animation de navigation entre le noeud de d�part et d'arriv�e.
	 * A chaque noeud une congestion al�atoire peut se cr�er et le chemin est recalcul�. 
	 */
	private void navigation() {
		
		/* Pas besoin de r�initialiser les distances et pred des noeuds avant de relancer
		 * l'algorithme car cela est effectu� dans afficherChemin(), et afficherChemin()
		 * est toujours appel� avant navigation() 
		 */
		
		Dijkstra.calculChemins(depart); 
		List<Noeud> chemin = Dijkstra.plusCourtChemin(arrivee);
		
		position = depart;
		
		// D�sactivation les composants de la fen�tre pendant la navigation
		boutonGo.setEnabled(false);
		departs.setEnabled(false);
		arrivees.setEnabled(false);
		
		// Boucle principale de navigation
		estArrive = false; // R�initialisation du bool�en de contr�le
		while(!estArrive) { 
			/*
			 * Gestion des congestions. 
			 * On g�n�re un �v�nement al�atoire � chaque tour � partir du deuxi�me en g�n�rant
			 * d'abord un index al�atoire pour la liste et ensuite en le comparant � nombreCong.
			 * S'il est inf�rieur l'index correspond � un arc, on peut cr�er une congestion.
			 * Sinon l'index correspond � un objet null, un "non-�v�nement" : on ne fait rien.  
			 * 
			 */
			if (position != depart) { // A partir du 2e sommet seulement
				
				// On r�tablit les derni�res congestions avant d'en cr�er une autre
				if(derniereCong != null && lastReciproque != null) {
					derniereCong.setPoids(poidsOrigine); // R�tablit le poids de la derni�re congestion choisie
					derniereCong.setCouleur(Color.BLACK); // Sa couleur
					lastReciproque.setPoids(poidsReciproque); // M�me chose pour la r�ciproque
					lastReciproque.setCouleur(Color.BLACK);
				}
				Random random = new Random();
				int index = random.nextInt(listeRoutes.size() - 1); // G�n�ration al�atoire d'index
				if (index < nombreCong)	{ // Il s'agit d'une "r��lle congestion".
					creerCongestion(listeRoutes.get(index));
				}	
			}
			
			afficherChemin(); // On affiche le nouveau chemin
			
			chemin = Dijkstra.plusCourtChemin(arrivee); // MaJ du chemin
			position.setCouleur(Color.ORANGE); // Coloriage de la position actuelle
			reseau.repaint();
			try {
				Thread.sleep(2000);  // Pause de 2 secondes
			}
			catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			if (position == arrivee) { // Arr�t de la navigation lorsqu'on arrive � destination
				estArrive = true; 
				break;
			}
			position.setCouleur(Color.WHITE); // On r�tablit la couleur avant de passer au suivant
			position = chemin.get(1); // Position passe au noeud suivant sur le chemin
		}
		
		
		// Fin de la navigation, r�activation des composants
		departs.setEnabled(true);
		arrivees.setEnabled(true);
		boutonGo.setEnabled(true);
	}
	
	/**
	 * Classe interne impl�mentant l'interface "Runnable". 
	 * Utilis�e pour cr�er un nouveau Thread.
	 *
	 */
	class PlayAnimation implements Runnable {
		public void run() {
			navigation(); // La navigation s'effectue dans un thread parall�le.
		}
	}
	
}
