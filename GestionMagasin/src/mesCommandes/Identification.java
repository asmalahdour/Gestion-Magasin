package mesCommandes;
import javax.servlet.http.*;
public class Identification {
	
	static String chercheNom (Cookie [] cookies) {
		
		// cherche dans les cookies la valeur de celui qui se nomme "nom"
		// retourne la valeur de ce nom au lieu de inconnu
		for(Cookie c: cookies) {
			
			if(c.getName().equals("nom")) {
				return c.getValue();
			}
		}
		
		return "inconnu";
	}
}