package listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

/**
 * Listener to handle the initialisation and destruction of the servlet context.
 * It ensures that the MySQL JDBC driver is loaded and deregistered correctly.
 */


// The ContextListener class implements the ServletContextListener interface.
public class ContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Error loading MySQL JDBC driver: " + e);
		}
	}

// The contextDestroyed method is called when the servlet context is destroyed.
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		AbandonedConnectionCleanupThread.checkedShutdown();
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
				System.out.println("Deregistered JDBC driver: " + driver);
			} catch (SQLException ex) {
				System.out.println("Error deregistering driver: " + driver + ", Error: " + ex);
			}
		}
	}
}
