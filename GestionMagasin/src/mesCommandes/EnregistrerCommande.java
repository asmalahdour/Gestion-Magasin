package mesCommandes;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
public class EnregistrerCommande extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection connexion=null;
	Statement stmt=null;
	PreparedStatement pstmt=null;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException{ 
		
		String nom = null;
		int nbreProduit = 0;
		Cookie[] cookies = request.getCookies();
		boolean connu = false;
		nom = Identification.chercheNom(cookies);
		
		ouvreBase();
		ajouteNomBase(nom);
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<html>");
		out.println("<body>");
		out.println("<head>");
		out.println("<title> votre commande </title>");
		out.println("</head>");
		out.println("<body bgcolor=\"white\">");
		out.println("<h3>" + "Bonjour " + nom + " voici ta nouvelle commande" + "</h3>");
		
		HttpSession session = request.getSession();
		Enumeration names = session.getAttributeNames();
		
		while (names.hasMoreElements()) {
			nbreProduit++;
			String name = (String) names.nextElement();
			String value = session.getAttribute(name).toString();
			out.println(name + " = " + value + "<br>");
		}
		
		ajouteCommandeBase(nom,session);
		out.println("<h3>" + "et voici " + nom + " ta commande complete" + "</h3>");
		montreCommandeBase(nom, out);
		out.println("<A HREF=vider> Vous pouvez commandez un autre disque </A><br> ");
		out.println("</body>");
		out.println("</html>");
	}
	protected void ouvreBase() {
		try {
			String pilote = getInitParameter("driver");
			String db = getInitParameter("localisation");
			Class.forName(pilote).newInstance();
			connexion = DriverManager.getConnection(db,"root","");
			connexion.setAutoCommit(true);
			stmt = connexion.createStatement();
		}
		catch (Exception E) {
			log(" -------- probeme " + E.getClass().getName() );
			E.printStackTrace();
		}
	}
	
	protected void fermeBase() {
		try {
			stmt.close();
			connexion.close();
		}
		catch (Exception E) {
			log(" -------- probleme " + E.getClass().getName() );
			E.printStackTrace();
		}
	}
	
	protected void ajouteNomBase(String nom) {
		try {
			ResultSet rset = null;
			pstmt= connexion.prepareStatement("select numero from personnel where nom=?");
			pstmt.setString(1,nom);
			rset=pstmt.executeQuery();
			if (!rset.next())
				stmt.executeUpdate("INSERT INTO personnel(nom) VALUES ( '" + nom + "' )" );
		}
		catch (Exception E) {
			log(" - probeme " + E.getClass().getName() );
			E.printStackTrace();
		}
	}
	
	protected void ajouteCommandeBase(String nom, HttpSession session ) {
		// ajoute le contenu du panier dans la base
		int id ;
		String code;
		ResultSet rset = null;
		Enumeration names = session.getAttributeNames();
		
		try {
			while (names.hasMoreElements()) {
				code = (String) names.nextElement();
				
				pstmt= connexion.prepareStatement("select numero from personnel where nom=?");
				pstmt.setString(1,nom);
				rset=pstmt.executeQuery();
				id = rset.getInt("numero");
				
				stmt.executeUpdate("INSERT INTO commande(article,qui) VALUES ('" + code + "' ,"+id+")" );
			}
		}
		catch (Exception E) {
			log(" - probeme " + E.getClass().getName() );
			E.printStackTrace();
		}
	}
	
	protected void montreCommandeBase(String nom, PrintWriter out) {
		// affiche les produits présents dans la base
		int id;
		String article = null;
		try {
			ResultSet rset = null;
			pstmt = connexion.prepareStatement("select numero from personnel where nom=?");
			pstmt.setString(1,nom);
			rset=pstmt.executeQuery();
			id = rset.getInt("numero");
			pstmt = connexion.prepareStatement("select article from commande where qui=?");
			pstmt.setString(1,"'"+id+"'");
			rset=pstmt.executeQuery();
			while (!rset.next()) {
				article = rset.getString("article");
				out.println("<h4>"+article+"</h4>");
			}
		}
		catch (Exception E) {
			log(" - probeme " + E.getClass().getName() );
			E.printStackTrace();
		}
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException{
		doGet(request, response);
	}
}