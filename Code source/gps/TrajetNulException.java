package gps;

import javax.swing.JOptionPane;

/**
 * Exception déclenchée lorsqu'on choisit un itinéraire vide (depart == arrivee)
 *
 */
class TrajetNulException extends Exception {

	/**
	 * Constructeur : affichage d'un message à l'utilisateur
	 */
	TrajetNulException() {
		JOptionPane.showMessageDialog(null, "Vous êtes déjà à l'arrivée !", "Trajet vide", JOptionPane.INFORMATION_MESSAGE);
		//this.printStackTrace();
	}
}
