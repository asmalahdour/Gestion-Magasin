package mesCommandes;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InscriptionClient extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String nomRecu=null, motPasseRecu=null;
		String nomCookie=null, motPasseCookie=null;
		
		// initialisation cookies et param�tres
		nomRecu = request.getParameter("nom");
		motPasseRecu = request.getParameter("motdepasse");
		try {
			for(Cookie c: request.getCookies()) {
				if(c.getName().equals(nomRecu)){
					nomCookie = nomRecu;
				}
				if(c.getName().equals("motPasseCoookie")) {
					motPasseCookie=c.getValue();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		if (nomCookie==null && nomRecu==null){
			
			// Cas 1 : cas o� il n'y a ni de cookies ni de parametres
			out.println("<html>");
			out.println("<body>");
			out.println("<head>");
			out.println("<title> inscription d'un client </title>");
			out.println("</head>");
			out.println("<body bgcolor='white' >");
			out.println( nomRecu +" | "+ motPasseRecu +" | "+ nomCookie +" | "+ motPasseCookie );
			out.println("<h3>" + "Bonjour, vous devez vous inscrire " + "</h3>");
			out.println("<h3>" + "Attention mettre nom et le mot de passe avec plus de 3 caracteres" + "</h3>");
			out.println("<form action='sinscrire' method='GET' > ");
			out.println("nom");
			out.println("<input type='text' size='20' name='nom' >");
			out.println("<br>");
			out.println("mot de passe");
			out.println("<input type='password' size='20' name='motdepasse'> <br>");
			out.println("<input type='submit' value='inscription'>");
			out.println("</form>");
			out.println("</body>");
			out.println("</html>");
			System.out.println(1);
		} else if (nomCookie==null && nomRecu!=null){
			// Cas 2 : cas o� il n'y a pas de cookies mais les param�tres nom et mot de passes sont pr�sents :
			Cookie c1 = new Cookie("nom", nomRecu);
			Cookie c2 = new Cookie(nomRecu, motPasseRecu);
			response.addCookie(c1);
			response.addCookie(c2);
			response.sendRedirect("sinscrire?nom="+nomRecu);
			
		}
		else if (identique(nomRecu,nomCookie) && identique(motPasseRecu,motPasseCookie))
		{
			// Cas 4 : cas o� le nom et le mot passe sont correctes, appel � la servlet achat
			response.sendRedirect("achat");
			System.out.println(4);
		}
		else {
			// Cas 3 : les cookies sont pr�sents demande de s'identifier
			out.println("<html>");
			out.println("<body>");
			out.println("<head>");
			out.println("<title>Identification</title>");
			out.println("</head>");
			out.println("<body bgcolor='white' >");
			out.print(" <form action='sinscrire' method='GET' > ");
			out.println("nom");
			out.println("<input type='text' size='20' name='nom' >");
			out.println("<tr>");
			out.println("mot de passe");
			out.println("<input type='password' size='20' name='motdepasse'> <br>");
			out.println("<input type='submit' value='se connecter'>");
			out.println("</form>");
			out.println("</body>");
			out.println("</html>");
			System.out.println(3);
			System.out.println("cookie: nom = "+nomCookie+" | pass = "+motPasseCookie+
					"\nclient: nom = "+nomRecu+" | pass = "+motPasseRecu);
			
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException{
		
		doGet(request, response);
	}
	boolean identique (String recu, String cookie){
		
		return ((recu != null) && (recu.length() >3) &&
				(cookie != null) && (recu.equals(cookie) ));
	}
}