package gps;

import javax.swing.JOptionPane;

/**
 * Exception d�clench�e lorsqu'on choisit un itin�raire vide (depart == arrivee)
 *
 */
class TrajetNulException extends Exception {

	/**
	 * Constructeur : affichage d'un message � l'utilisateur
	 */
	TrajetNulException() {
		JOptionPane.showMessageDialog(null, "Vous �tes d�j� � l'arriv�e !", "Trajet vide", JOptionPane.INFORMATION_MESSAGE);
		//this.printStackTrace();
	}
}
