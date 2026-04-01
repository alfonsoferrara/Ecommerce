package ecom.control.listener;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.naming.Context;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class MainContextListener implements ServletContextListener {

	public MainContextListener() {
	}

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		DataSource ds = null;

		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			ds = (DataSource) envCtx.lookup("jdbc/ecommerce");

			context.setAttribute("DataSource", ds);
			System.out.println("DataSource configurato correttamente");

		} catch (NamingException e) {
			System.err.println("ERRORE CRITICO: Impossibile trovare la risorsa JNDI 'jdbc/ecommerce'.");
			e.printStackTrace();
		}

	}

	public void contextDestroyed(ServletContextEvent sce) {
		// alla distruzione del contesto il server pulisce tutto in automatico
		sce.getServletContext().removeAttribute("DataSource");
		System.out.println("DataSource rimosso dal contesto.");
	}

}
