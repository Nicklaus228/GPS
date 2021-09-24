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
 * Interface (fenêtre) principale de l'application, dérive de JFrame.
 * Contient toutes les fonctions nécessaires au déroulement attendu du programme.
 *
 */
public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7921238150593723661L;
	
	// Définition couleurs utilisées
	static Color BLEU_CIEL = new Color(35, 142, 181); // Définition nouvelle couleur 
	
	private JComboBox<Noeud> departs,arrivees; // Listes déroulantes
	private JButton boutonGo;
	private Reseau reseau; // Réseau routier considéré
	private JPanel container = new JPanel(); // Panneau principal
	private Noeud depart, arrivee, position; 
	private ArrayList<Arc> listeRoutes; // liste des arcs du graphe
	private boolean estArrive = false; // True lorsqu'on arrive à destination
	private Thread animation; // Pile d'exécution pour l'animation 
	
	// Variables pour la gestion des congestions
	private Arc derniereCong, lastReciproque; 
	private int poidsOrigine, poidsReciproque; 
	private int nombreCong;
	
	/**
	 * Constructeur, les différents composants sont initialisés
	 * @param reseau: Réseau considéré par l'application
	 */
	public MainFrame(Reseau reseau) {
		this.setTitle("GPS");
		this.setSize(600, 400); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setLocationRelativeTo(null); // Place la fenêtre au centre de l'écran
		this.setResizable(false); // Fixe la taille de la fenêtre
		this.reseau = reseau;
		
		// Initialisation de tous les composants de la fenêtre et leurs Listeners
		initFrame(); 
		
		this.setContentPane(container);
		this.setVisible(true); // Affiche la fenêtre
		
	}
	
	/**
	 * Fonction responsable de l'initialisation des composants.
	 * Initialise les deux listes déroulantes à partir du réseau ainsi que le bouton GO. 
	 * Initialise la liste de Routes utilisée pour la gestion des congestions. 
	 * Implémente les Listeners des différents composants.
	 */
	private void initFrame() {
		container.setLayout(new BorderLayout()); // Définition du layout utilisé
		
		// Création du panneau supérieur
		JPanel panneauSup = new JPanel(); 
		panneauSup.setPreferredSize(new Dimension(580, 40));
		panneauSup.setBackground(Color.DARK_GRAY);
		
		// Initialisation des composants du panneau supérieur
		Dimension tailleListes = new Dimension(260, 30); // taille des listes
		// Création des listes à partir des sommets du réseau
		departs = new JComboBox<Noeud>(reseau.listeNoeuds);
		departs.setPreferredSize(tailleListes);
		arrivees = new JComboBox<Noeud>(reseau.listeNoeuds);
		arrivees.setPreferredSize(tailleListes);
		// Création du bouton
		boutonGo = new JButton("GO");
		boutonGo.setFont(new Font("Cambria Math", Font.BOLD, 10));
		boutonGo.setForeground(Color.RED); // Couleur de "GO"
		boutonGo.setPreferredSize(new Dimension(50, 30)); // Taille du bouton
		
		// Ajout des composants au panneau supérieur, dans l'ordre
		panneauSup.add(departs);
		panneauSup.add(arrivees);
		panneauSup.add(boutonGo);
		
		// Création du panneau inférieur
		JPanel panneauInf = new JPanel();
		panneauInf.setPreferredSize(new Dimension(580, 335));
		panneauInf.setBackground(Color.DARK_GRAY);
		
		// Bordure pour l'affichage du réseau
		reseau.setBorder(BorderFactory.createLineBorder(BLEU_CIEL));
		
		panneauInf.add(reseau); // On ajoute le réseau (un JPanel) au panneau inférieur
		
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
		nombreCong = listeRoutes.size(); // Sauvegarde du nombre réel de congestions
		for (int i = 0; i < nombreCong*0.25; i++) {
			listeRoutes.add(null); // Ajout de "non-congestions", dont le nombre est égal au quart de congestions réelles
		}
		
		// Implémentation des Listeners
		departs.addActionListener(new ActionListener() { // Listener liste "departs"
			public void actionPerformed(ActionEvent e) {
				if (depart != null && depart != arrivee) {
					depart.setCouleur(Color.WHITE); // On réinitialise la couleur du noeud de départ précédent
				}
				depart = (Noeud)departs.getSelectedItem(); // Le noeud sélectionné est le noeud de départ
				depart.setCouleur(BLEU_CIEL); // On affiche le noeud de départ en changeant la couleur
				reseau.repaint(); // Repeint le réseau pour voir les changements
			}
		});
		
		arrivees.addActionListener(new ActionListener() { // Listener liste "arrivees"
			public void actionPerformed(ActionEvent e) { // Même chose que le Listener précédent
				if (arrivee != null && arrivee != depart) {
					arrivee.setCouleur(Color.WHITE);
				}
				arrivee = (Noeud)arrivees.getSelectedItem(); // Le noeud sélectionné est le noeud d'arrivée
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
					afficherChemin(); // Affichage du chemin le plus court pour l'itinéraire choisi
					// reseau.repaint();
					animation = new Thread(new PlayAnimation()); // Création d'un nouveau thread pour l'animation (après débug)
					animation.start(); // Démarrage de l'animation de navigation
				} catch (NullPointerException ex1) { // Déclenchée si un des points n'est pas défini
					JOptionPane.showMessageDialog(null, "Vous devez choisir un point de départ et d'arrivée !",
												"Trajet indéfini", // Titre de la fenêtre
												JOptionPane.ERROR_MESSAGE); // Icone utilisée
				} catch (TrajetNulException ex2) { // Déclenchée si un des points n'est pas défini
					
				}
				
			}
		});
	}
	
	/**
	 * Fonction pour l'affichage du chemin le plus court sur le réseau (coloriage du chemin).
	 */
	private void afficherChemin() {
		// Recoloriage noeuds de départ et d'arrivée
		position.setCouleur(BLEU_CIEL); 
		arrivee.setCouleur(Color.RED);
		
		/* Réinitialisation des distances et prédecesseurs des noeuds. 
		 * Nécessaire avant de relancer l'algorithme de Dijkstra pour ne pas fausser le calcul
		 * avec des valeurs correspondant à l'itinéraire précédent.
		*/
		for (Noeud n : reseau.listeNoeuds) {
			n.setDistance(Integer.MAX_VALUE);
			n.setPred(null);
		}
		// Réinitialisation de la couleur des arcs
		for (int i = 0; i < nombreCong; i++) {
			listeRoutes.get(i).setCouleur(Color.BLACK);
		}
		// Recoloriage des congestions, car réinitialisées par la boucle précédente.
		if(derniereCong != null && lastReciproque != null) {
			derniereCong.setCouleur(Color.RED);
			lastReciproque.setCouleur(Color.RED);
		}
		
		// MaJ les distances des noeuds avec l'algo de Dijkstra
		Dijkstra.calculChemins(position);
		
		// On parcourt le chemin le plus court
		for (Noeud noeud: Dijkstra.plusCourtChemin(arrivee)) {
				
			if(noeud.getPred() == null) { // Vrai pour le premier noeud du chemin  
				continue; // Car la boucle suivante génére une erreur dans ce cas
			}
			// On trouve l'arc venant du noeud précédent
			for(Arc arc : noeud.getPred().getAdjacences()) {
				
				if (arc.getDestination() == noeud) {
					arc.setCouleur(Color.YELLOW); // Trouvé, on change sa couleur
					break; // Pas la peine de continuer
				}
			}
			// On trouve l'arc allant vers le noeud précédent
			for (Arc arc : noeud.getAdjacences()) {
				if(arc.getDestination() == noeud.getPred()) {
					arc.setCouleur(Color.YELLOW); // Trouvé, on change sa couleur
					break;
				}
			}
		}
		reseau.repaint(); // Réaffichage du réseau
		
	}
	
	/**
	 * Fonction qui crée une congestion en modifiant le poids et la couleur de l'arc désigné.
	 * Le programme garde des références de la dernière congestion enregistrée et son poids, de même que les arcs
	 * réciproques correspondants.
	 * Ces références servent à rétablir l'état initial du graphe avant de relancer une nouvelle congestion. 
	 * @param arc: Arc dans le graphe à partir duquel créer la congestion.
	 */
	private void creerCongestion(Arc arc) {
		// MaJ des références
		derniereCong = arc; // Sauvegarde de la congestion définie.
		poidsOrigine = arc.getPoids(); // Sauvegarde du poids d'origine de l'arc.
		arc.setPoids(500); // Poids très grand, afin que l'algorithme ne passe pas par là.
		arc.setCouleur(Color.RED); // Coloriage de l'arc.
		
		// Recherche du noeud de départ de l'arc
		Noeud departArc = null;
		for (Noeud noeud: reseau.listeNoeuds) { // On parcourt tous les noeuds du graphe
			for (Arc arcSortant : noeud.getAdjacences()) { // Parcours des arcs sortant du noeud
				if (arcSortant == arc) { // Recherche d'une correspondance avec l'arc de la congestion
					departArc = noeud; // Arc de départ trouvé
					break; // Pas besoin de continuer
				}
			}
			if (departArc != null)
				break; // Sortir de la boucle principale si departArc est trouvé.
		}
		
		// Recherche de l'arc réciproque, mêmes actions
		for (Arc reciproque : arc.getDestination().getAdjacences() ) { 
			if (reciproque.getDestination() == departArc) { 
				lastReciproque = reciproque; 
				poidsReciproque = reciproque.getPoids();
				reciproque.setPoids(500); // Poids très grand
				reciproque.setCouleur(Color.RED); // On colorie l'arc réciproque
				break;
			}
		}
	}
	
	/**
	 * Fonction principale responsable de la navigation.
	 * Lorsqu'elle appelée on observe une animation de navigation entre le noeud de départ et d'arrivée.
	 * A chaque noeud une congestion aléatoire peut se créer et le chemin est recalculé. 
	 */
	private void navigation() {
		
		/* Pas besoin de réinitialiser les distances et pred des noeuds avant de relancer
		 * l'algorithme car cela est effectué dans afficherChemin(), et afficherChemin()
		 * est toujours appelé avant navigation() 
		 */
		
		Dijkstra.calculChemins(depart); 
		List<Noeud> chemin = Dijkstra.plusCourtChemin(arrivee);
		
		position = depart;
		
		// Désactivation les composants de la fenêtre pendant la navigation
		boutonGo.setEnabled(false);
		departs.setEnabled(false);
		arrivees.setEnabled(false);
		
		// Boucle principale de navigation
		estArrive = false; // Réinitialisation du booléen de contrôle
		while(!estArrive) { 
			/*
			 * Gestion des congestions. 
			 * On génére un évènement aléatoire à chaque tour à partir du deuxième en générant
			 * d'abord un index aléatoire pour la liste et ensuite en le comparant à nombreCong.
			 * S'il est inférieur l'index correspond à un arc, on peut créer une congestion.
			 * Sinon l'index correspond à un objet null, un "non-évènement" : on ne fait rien.  
			 * 
			 */
			if (position != depart) { // A partir du 2e sommet seulement
				
				// On rétablit les dernières congestions avant d'en créer une autre
				if(derniereCong != null && lastReciproque != null) {
					derniereCong.setPoids(poidsOrigine); // Rétablit le poids de la dernière congestion choisie
					derniereCong.setCouleur(Color.BLACK); // Sa couleur
					lastReciproque.setPoids(poidsReciproque); // Même chose pour la réciproque
					lastReciproque.setCouleur(Color.BLACK);
				}
				Random random = new Random();
				int index = random.nextInt(listeRoutes.size() - 1); // Génération aléatoire d'index
				if (index < nombreCong)	{ // Il s'agit d'une "réélle congestion".
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
			if (position == arrivee) { // Arrêt de la navigation lorsqu'on arrive à destination
				estArrive = true; 
				break;
			}
			position.setCouleur(Color.WHITE); // On rétablit la couleur avant de passer au suivant
			position = chemin.get(1); // Position passe au noeud suivant sur le chemin
		}
		
		
		// Fin de la navigation, réactivation des composants
		departs.setEnabled(true);
		arrivees.setEnabled(true);
		boutonGo.setEnabled(true);
	}
	
	/**
	 * Classe interne implémentant l'interface "Runnable". 
	 * Utilisée pour créer un nouveau Thread.
	 *
	 */
	class PlayAnimation implements Runnable {
		public void run() {
			navigation(); // La navigation s'effectue dans un thread parallèle.
		}
	}
	
}
