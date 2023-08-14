package Clases;

import static java.awt.Frame.MAXIMIZED_BOTH;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import Forms.Principal;

/**
 *
 * @author Franco
 */
public class Futbol5 {

	public static void main(String[] args) {

		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					System.out.println("CHOSEN THIS");
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Error setting native LAF: " + e);
		}

		Connection con = null;
		String url = "jdbc:mysql://localhost:3306/canchas2?serverTimezone=GMT-3";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, "root", "root");
			System.out.println("Cargado");
		} catch (Exception ex) {
			System.out.println("Error: ");
		}

		ResultSet rs = null;
		Integer acabo = 10;

		String insert = "UPDATE contador SET conteo = conteo -0";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println(ex);
		}

		try {
			ps = con.prepareStatement("SELECT conteo FROM contador");
			rs = ps.executeQuery();
			while (rs.next()) {
				acabo = rs.getInt(1);
			}
		} catch (Exception ex) {
			System.out.println("No hay registros");
		}

		if (acabo < 0) {
			JOptionPane.showMessageDialog(null, "Ya uso el programa todas las veces permitidas", "Se termino la prueba",
					JOptionPane.WARNING_MESSAGE);
			return;
		} else {
			Principal miPrincipal = new Principal();
			miPrincipal.setExtendedState(MAXIMIZED_BOTH);
			miPrincipal.setVisible(true);
		}

	}

}
