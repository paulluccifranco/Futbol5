/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

public class Conexion {

	public Connection conectar() {
		Connection con = null;
		String url = "jdbc:mysql://localhost:3306/canchas2?serverTimezone=GMT-3";
		// jdbc:mysql://db4free.net:3306/canchas2
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, "root", "root");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos", "",
					JOptionPane.WARNING_MESSAGE);
		}
		return con;
	}

	public ResultSet visualizar(String fecha, String fecha2) {
		Connection con = conectar();
		ResultSet rs = null;

		SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd");
		java.util.Date today = new Date(System.currentTimeMillis());
		String hoy = String.valueOf(formato.format(today));
		java.util.Date fechaDate = null;
		try {
			fechaDate = formato.parse(fecha);
			today = formato.parse(hoy);
		} catch (ParseException ex) {
			System.out.println(ex);
		}

		Integer comparacion = fechaDate.compareTo(today);
		System.out.println(comparacion);
		if (comparacion > -1) {
			try {
				PreparedStatement ps = con.prepareStatement(
						"SELECT * FROM turnos INNER JOIN reserva ON turnos.id_turno = reserva.id_turno WHERE reserva.fecha = '"
								+ fecha2 + "'");
				rs = ps.executeQuery();
				while (rs.next()) {
					reservar(rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(10), fecha, 1);
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}

		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT * FROM turnos INNER JOIN reserva ON turnos.id_turno = reserva.id_turno WHERE reserva.fecha = '"
							+ fecha + "' AND reserva.borrado = 0");
			rs = ps.executeQuery();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error al cargar los turnos", "", JOptionPane.WARNING_MESSAGE);
		}
		return rs;
	}

	public ResultSet visualizar_turno(Integer horario, Integer cancha, String fecha) {
		Connection con = conectar();
		ResultSet rs = null;

		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT * FROM turnos INNER JOIN reserva ON turnos.id_turno = reserva.id_turno WHERE reserva.horario = "
							+ horario + " AND reserva.cancha = " + cancha + " AND reserva.fecha = '" + fecha
							+ "' AND reserva.borrado = 0");
			rs = ps.executeQuery();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error al visualizar el turno", "", JOptionPane.WARNING_MESSAGE);
		}
		return rs;
	}

	public void guardarturno(Integer cancha, String turno, Integer horario, Integer saldo, Integer condicion,
			String telefono, String fecha, Integer fijo) {
		Connection con = conectar();

		String insert = "INSERT INTO turnos (turno, saldo, telefono) VALUES (?,?,?)";
		PreparedStatement ps = null;
		Integer idGenerado = null;
		try {
			ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, turno);
			ps.setInt(2, saldo);
			ps.setString(3, telefono);

			ps.executeUpdate();
			ResultSet generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()) {
				idGenerado = generatedKeys.getInt(1);
			}

			reservar(idGenerado, cancha, horario, condicion, fecha, fijo);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error al guardar el turno", "", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void reservar(Integer id, Integer cancha, Integer horario, Integer condicion, String fecha, Integer fijo) {
		Connection con = conectar();
		ResultSet rs = null;
		Boolean existe = false;

		if (fijo == 1) {
			try {
				PreparedStatement ps = con.prepareStatement("SELECT * FROM reserva WHERE fecha = '" + fecha
						+ "' AND horario = " + horario + " AND cancha = " + cancha);
				rs = ps.executeQuery();
				while (rs.next()) {
					existe = true;
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}

			try {
				PreparedStatement ps = con.prepareStatement("SELECT * FROM reserva WHERE fecha = '" + fecha
						+ "' AND horario = " + horario + " AND cancha = " + cancha);
				rs = ps.executeQuery();
				Boolean doble = false;
				Integer total = 0;
				while (rs.next()) {
					if (rs.getInt(8) == 0) {
						doble = true;
					}
					if (rs.getInt(1) == id) {
						doble = false;
					}
					total = total + 1;
				}
				if (doble && total == 1) {
					JOptionPane.showMessageDialog(null,
							"Hay Un Turno Fijo Superpuesto En La Cancha " + cancha + " A Las " + horario + "Hs", "",
							JOptionPane.WARNING_MESSAGE);
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}

		} else {
			try {
				PreparedStatement ps = con.prepareStatement("SELECT * FROM reserva WHERE fecha = '" + fecha
						+ "' AND horario = " + horario + " AND cancha = " + cancha + " AND borrado = 0");
				rs = ps.executeQuery();
				while (rs.next()) {
					existe = true;
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}

		if (!existe) {
			String insert = "INSERT INTO reserva (id_turno, cancha, horario, fecha, condicion, esfijo) VALUES (?,?,?,?,?,?)";
			PreparedStatement ps = null;
			try {
				ps = con.prepareStatement(insert);
				ps.setInt(1, id);
				ps.setInt(2, cancha);
				ps.setInt(3, horario);
				ps.setString(4, fecha);
				ps.setInt(5, condicion);
				ps.setInt(6, fijo);
				ps.executeUpdate();

			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
	}

	public void guardar_salida(String descripcion, Double gasto) {
		Connection con = conectar();

		String insert = "INSERT INTO salidas (descripcion, gasto) VALUES (?,?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setString(1, descripcion);
			ps.setDouble(2, gasto);

			ps.executeUpdate();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "No se pudo guardar el egreso", "", JOptionPane.WARNING_MESSAGE);
		}
	}

	public ResultSet visualizar_salidas() {
		Connection con = conectar();
		ResultSet rs = null;

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM salidas");
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println("Error de consulta");
		}
		return rs;
	}

	public void guardar_ingreso(String descripcion, Double gasto) {
		Connection con = conectar();

		String insert = "INSERT INTO ingresos (descripcion, gasto) VALUES (?,?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setString(1, descripcion);
			ps.setDouble(2, gasto);

			ps.executeUpdate();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "No se pudo guardar el ingreso", "", JOptionPane.WARNING_MESSAGE);
		}
	}

	public ResultSet visualizar_ingresos() {
		Connection con = conectar();
		ResultSet rs = null;

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM ingresos");
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println("Error de consulta");
		}
		return rs;
	}

	public void editar_turno(Integer condicion, Integer id, Integer precio) {
		Connection con = conectar();

		String insert = "UPDATE reserva SET condicion = ?, pago = ? WHERE id_reserva = " + id;
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setInt(1, condicion);
			ps.setInt(2, precio);

			ps.executeUpdate();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "No se pudo cambiar el estado del turno", "",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	public void editar_datos(Integer id, String nombre, String telefono) {
		Connection con = conectar();

		String insert = "UPDATE turnos SET turno = ?, telefono = ? WHERE id_turno = " + id;
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setString(1, nombre);
			ps.setString(2, telefono);

			ps.executeUpdate();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "No se pudo editar el turno", "", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void borrar_reserva(Integer id) {
		Connection con = conectar();

		String borrar = "DELETE FROM reserva WHERE id_reserva = " + id;
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement(borrar);
			ps.executeUpdate();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error al borrar el turno", "", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void canceladofijo(Integer idreserva) {
		Connection con = conectar();

		String insert = "UPDATE reserva SET borrado = 1 WHERE id_reserva = " + idreserva;
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void borrar_fijo(Integer id, String fecha, Integer cancha, Integer hora) {
		Connection con = conectar();

		String borrar = "DELETE FROM reserva WHERE (id_turno = " + id + " AND fecha = '" + fecha + "' AND cancha = "
				+ cancha + " AND horario = " + hora + ") OR (id_turno = " + id
				+ " AND fecha like '2_______' AND cancha = " + cancha + " AND horario = " + hora
				+ " AND condicion = 0 AND esfijo = 1)";
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement(borrar);
			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void guardarSaldo(Integer saldo, Integer id) {
		Connection con = conectar();

		String insert = "UPDATE turnos SET saldo = saldo + ? WHERE id_turno = " + id;
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setInt(1, saldo);

			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public ResultSet visualizarProducto() {
		Connection con = conectar();
		ResultSet rs = null;

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM productos ORDER BY producto ASC");
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println("Error de consulta");
		}
		return rs;
	}

	public ResultSet seleccionarProducto(String codigo) {
		Connection con = conectar();
		ResultSet rs = null;

		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT * FROM productos WHERE (codigo = '" + codigo + "') OR (producto = '" + codigo + "')");
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println("Error de consulta");
		}
		return rs;
	}

	public ResultSet visualizarVentas(String desde, String hasta, Integer user, String codigo, Integer categoria) {
		Connection con = conectar();
		ResultSet rs = null;

		String dsd = "";
		String hst = "";
		String cod = "";
		String cate = "";

		if (desde != "") {
			dsd = " AND fecha >= " + desde;
		}
		if (hasta != "") {
			hst = " AND fecha <= " + hasta;
		}
		if (!codigo.equals("")) {
			cod = " AND codigo = '" + codigo + "'";
		}
		if (categoria > 0) {
			categoria = categoria - 1;
			cate = " AND categoria = " + categoria;
		}

		try {
			PreparedStatement ps = con
					.prepareStatement("SELECT * FROM ventas WHERE user = " + user + dsd + hst + cod + cate);
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println("Error de consulta");
		}
		return rs;
	}

	public void guardar_producto(String codigo, String nombre, Double precio, Integer stock, Double costo,
			Integer categoria) {
		Connection con = conectar();
		String insert = "INSERT INTO productos (codigo, producto, precio, stock, costo, categoria) VALUES (?,?,?,?,?,?)";
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement(insert);
			ps.setString(1, codigo);
			ps.setString(2, nombre);
			ps.setDouble(3, precio);
			ps.setInt(4, stock);
			ps.setDouble(5, costo);
			ps.setInt(6, categoria);

			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void editar_producto(Integer id, String codigo, Double precio, Double costo) {
		Connection con = conectar();

		String insert = "UPDATE productos SET precio = ?, costo = ? WHERE id_producto = " + id;
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement(insert);
			ps.setDouble(1, precio);
			ps.setDouble(2, costo);

			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println("Error al actualizar");
		}
	}

	public void editar_stock(Integer id, Integer stock) {
		Connection con = conectar();

		String insert = "UPDATE productos SET stock = stock + ? WHERE id_producto = " + id;
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement(insert);
			ps.setInt(1, stock);

			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println("Error al actualizar");
		}
	}

	public void eliminar_producto(Integer id) {
		Connection con = conectar();

		String insert = "DELETE FROM productos WHERE id_producto = " + id;
		PreparedStatement ps = null;
		try {

			ps = con.prepareStatement(insert);

			ps.execute();

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void guardarventa(Integer cantidad, String nombre, Double total, Date fecha, Integer categoria,
			String codigo, Integer user, Double costo) {
		Connection con = conectar();

		String formato = "yyyyMMdd";
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		String desde = String.valueOf(sdf.format(fecha));

		String update = "UPDATE ventas SET cantidad = cantidad + ?, total = total + ?, costo = costo + ? WHERE fecha = "
				+ desde + " AND codigo = '" + codigo + "' AND user = " + user;
		PreparedStatement ps = null;
		try {

			ps = con.prepareStatement(update);
			ps.setInt(1, cantidad);
			ps.setDouble(2, total);
			ps.setDouble(3, costo);

			int n = ps.executeUpdate();

			if (n == 0) {
				String insert = "INSERT INTO ventas (cantidad, nombre, total, fecha, categoria, codigo, user, costo) VALUES (?,?,?,?,?,?,?,?)";
				try {
					ps = con.prepareStatement(insert);
					ps.setInt(1, cantidad);
					ps.setString(2, nombre);
					ps.setDouble(3, total);
					ps.setDate(4, fecha);
					ps.setInt(5, categoria);
					ps.setString(6, codigo);
					ps.setInt(7, user);
					ps.setDouble(8, costo);
					ps.executeUpdate();

				} catch (Exception ex) {
					System.out.println(ex);
				}
			}

		} catch (SQLException ex) {
			System.out.println(ex);
		}

		String insert = "UPDATE productos SET stock = stock - ? WHERE codigo = '" + codigo + "'";
		try {
			ps = con.prepareStatement(insert);
			ps.setInt(1, cantidad);

			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println("Error al actualizar");
		}

		if (user == 0) {
			String updatedia = "UPDATE totaldia SET total = total + ? WHERE tipo = " + categoria;
			ps = null;
			try {

				ps = con.prepareStatement(updatedia);
				ps.setDouble(1, total);

				int n = ps.executeUpdate();

				if (n == 0) {
					String insertdia = "INSERT INTO totaldia (tipo, total) VALUES (?,?)";
					try {
						ps = con.prepareStatement(insertdia);
						ps.setInt(1, categoria);
						ps.setDouble(2, total);
						ps.executeUpdate();
					} catch (Exception ex) {
						System.out.println(ex);
					}
				}

			} catch (SQLException ex) {
				System.out.println(ex);
			}
		}

	}

	public void guardarventaturno(Integer precio) {
		Connection con = conectar();

		String update = "UPDATE ventaturnos SET cantidad = cantidad + 1, total = total + ? WHERE precio = " + precio;
		PreparedStatement ps = null;
		try {

			ps = con.prepareStatement(update);
			ps.setInt(1, precio);

			int n = ps.executeUpdate();

			if (n == 0) {
				String insert = "INSERT INTO ventaturnos (precio, cantidad, total) VALUES (?,?,?)";
				try {
					ps = con.prepareStatement(insert);
					ps.setInt(1, precio);
					ps.setInt(2, 1);
					ps.setInt(3, precio);

					ps.executeUpdate();

				} catch (Exception ex) {
					System.out.println("Error al insertar");
				}
			}

		} catch (SQLException ex) {
			System.out.println("Error al updatear");
		}

	}

	public ResultSet visualizarTurnos() {
		Connection con = conectar();
		ResultSet rs = null;

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM ventaturnos WHERE cantidad > 0");
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println("Error de consulta");
		}
		return rs;
	}

	public void vaciar(String nombretabla) {
		Connection con = conectar();

		String insert = "TRUNCATE " + nombretabla;
		PreparedStatement ps = null;
		try {

			ps = con.prepareStatement(insert);

			ps.execute();

		} catch (Exception ex) {
			System.out.println("Error al borrar");
		}
	}

	public void guardarconsumo(String nombre, Double total, Integer id, Integer categoria, String codigo,
			Double costo) {
		Connection con = conectar();

		String update = "UPDATE consumoturno SET cantidad = cantidad + ?, total = total + ?, costo = costo + ? WHERE codigo = '"
				+ codigo + "' AND id_turno = " + id;
		PreparedStatement ps = null;
		try {

			ps = con.prepareStatement(update);
			ps.setInt(1, 1);
			ps.setDouble(2, total);
			ps.setDouble(3, costo);

			int n = ps.executeUpdate();

			if (n == 0) {
				String insert = "INSERT INTO consumoturno (cantidad, nombre, total, id_turno, categoria, codigo, costo) VALUES (?,?,?,?,?,?,?)";
				try {
					ps = con.prepareStatement(insert);
					ps.setInt(1, 1);
					ps.setString(2, nombre);
					ps.setDouble(3, total);
					ps.setInt(4, id);
					ps.setInt(5, categoria);
					ps.setString(6, codigo);
					ps.setDouble(7, costo);

					ps.executeUpdate();

				} catch (Exception ex) {
					System.out.println("Error al insertar");
				}
			}

		} catch (SQLException ex) {
			System.out.println(ex);
		}
	}

	public void borrarconsumo(String nombre, Integer id) {
		Connection con = conectar();

		String delete = "DELETE FROM consumoturno WHERE codigo = '" + nombre + "' AND id_turno = " + id;
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(delete);
			ps.execute();
		} catch (Exception ex) {
			System.out.println("Error al borrar");
		}
	}

	public void borrarconsumoventa(String nombre) {
		Connection con = conectar();

		String delete = "DELETE FROM consumoventas WHERE codigo = '" + nombre + "'";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(delete);
			ps.execute();
		} catch (Exception ex) {
			System.out.println("Error al borrar");
		}
	}

	public ResultSet visualizarConsumo(Integer id) {
		Connection con = conectar();
		ResultSet rs = null;

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM consumoturno WHERE id_turno = " + id);
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println("No hay registros");
		}
		return rs;
	}

	public ResultSet visualizarLaventa() {
		Connection con = conectar();
		ResultSet rs = null;

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM consumoventas");
			rs = ps.executeQuery();
		} catch (Exception ex) {
			System.out.println("No hay registros");
		}
		return rs;
	}

	public void guardarconsumoventa(String nombre, Double total, Integer categoria, String codigo, Double costo) {
		Connection con = conectar();

		String update = "UPDATE consumoventas SET cantidad = cantidad + ?, total = total + ?, costo = costo + ? WHERE codigo = '"
				+ codigo + "'";
		PreparedStatement ps = null;
		try {

			ps = con.prepareStatement(update);
			ps.setInt(1, 1);
			ps.setDouble(2, total);
			ps.setDouble(3, costo);

			int n = ps.executeUpdate();

			if (n == 0) {
				String insert = "INSERT INTO consumoventas (cantidad, nombre, total, categoria, codigo, costo) VALUES (?,?,?,?,?,?)";
				try {
					ps = con.prepareStatement(insert);
					ps.setInt(1, 1);
					ps.setString(2, nombre);
					ps.setDouble(3, total);
					ps.setInt(4, categoria);
					ps.setString(5, codigo);
					ps.setDouble(6, costo);

					ps.executeUpdate();

				} catch (Exception ex) {
					System.out.println("Error al insertar");
				}
			}

		} catch (SQLException ex) {
			System.out.println(ex);
		}
	}

	public void vaciar_consumo(Integer id) {
		Connection con = conectar();

		String insert = "DELETE FROM consumoturno WHERE id_turno = " + id;
		PreparedStatement ps = null;
		try {

			ps = con.prepareStatement(insert);

			ps.execute();

		} catch (Exception ex) {
			System.out.println("Error al borrar");
		}
	}

	public void contador() {
		Connection con = conectar();

		PreparedStatement ps = null;

		String insert = "UPDATE contador SET conteo = conteo - 1";
		try {
			ps = con.prepareStatement(insert);

			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println("Error al actualizar");
		}
	}

	public void mover(Integer id, Integer cancha, Integer horario, String fecha, Integer condicion, Boolean fijo,
			Integer canchaoriginal, Integer horaoriginal, Integer idreserva) {
		Connection con = conectar();
		if (fijo) {
			PreparedStatement ps = null;

			String update = "UPDATE reserva SET cancha = " + cancha + ", horario = " + horario + " WHERE id_turno = "
					+ id + " AND fecha = '" + fecha + "' AND cancha = " + canchaoriginal + " AND horario = "
					+ horaoriginal;
			try {
				ps = con.prepareStatement(update);
				ps.executeUpdate();

			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
		PreparedStatement ps = null;

		String update = "UPDATE reserva SET cancha = " + cancha + ", horario = " + horario + " WHERE id_reserva = "
				+ idreserva;
		try {
			ps = con.prepareStatement(update);
			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println(ex);
		}

	}

	public void canticanchas(Integer cantidad) {
		Connection con = conectar();

		String insert = "UPDATE canchas SET cantidad = ?";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setInt(1, cantidad);

			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void cargarstock(String numero, String descripcion, Integer cantidad) {
		Connection con = conectar();

		String insert = "INSERT INTO stockfactura (numero, producto, cantidad) VALUES (?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setString(1, numero);
			ps.setString(2, descripcion);
			ps.setInt(3, cantidad);

			ps.executeUpdate();

		} catch (Exception ex) {
			System.out.println("Error al guardar");
		}
	}

	public ResultSet visualizarstock(String factura) {
		Connection con = conectar();
		ResultSet rs = null;

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM stockfactura WHERE numero = '" + factura + "'");
			rs = ps.executeQuery();

		} catch (Exception ex) {
			System.out.println(ex);
		}
		return rs;
	}

	public void cargarfactura(String numero, String fecha) {
		Connection con = conectar();

		String insert = "INSERT INTO facturas (numero, fecha) VALUES (?,?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
			ps.setString(1, numero);
			ps.setString(2, fecha);

			ps.executeUpdate();

			ResultSet cargas = visualizarstock(numero);
			while (cargas.next()) {
				insert = "INSERT INTO cargafactura (producto, cantidad, factura) VALUES (?,?,?)";
				ps = null;
				ps = con.prepareStatement(insert);
				ps.setString(1, cargas.getString(2));
				ps.setInt(2, cargas.getInt(3));
				ps.setString(3, numero);

				ps.executeUpdate();

				insert = "UPDATE productos SET stock = stock + ? WHERE producto = '" + cargas.getString(2) + "'";
				ps = null;

				ps = con.prepareStatement(insert);
				ps.setInt(1, cargas.getInt(3));

				ps.executeUpdate();

			}

			vaciar("stockfactura");

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public ResultSet visualizarfacturas(String factura, String desde, String hasta) {
		Connection con = conectar();
		ResultSet rs = null;
		if (hasta.equals("")) {
			String formato = "yyyyMMdd";
			java.util.Date dsd = new Date(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat(formato);
			hasta = String.valueOf(sdf.format(dsd));
		}
		if (!factura.equals("")) {
			factura = " AND numero = '" + factura + "'";
		}
		if (!desde.equals("")) {
			desde = " AND fecha >= " + desde;
		}

		try {
			PreparedStatement ps = con
					.prepareStatement("SELECT * FROM facturas WHERE fecha <= " + hasta + desde + factura);
			rs = ps.executeQuery();

		} catch (Exception ex) {
			System.out.println(ex);
		}
		return rs;
	}

	public ResultSet visualizarprecios() {
		Connection con = conectar();
		ResultSet rs = null;

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM precios");
			rs = ps.executeQuery();

		} catch (Exception ex) {
			System.out.println(ex);
		}
		return rs;
	}

	public ResultSet visualizarcargas(String factura) {
		Connection con = conectar();
		ResultSet rs = null;

		try {
			PreparedStatement ps = con.prepareStatement("SELECT * FROM cargafactura WHERE factura = '" + factura + "'");
			rs = ps.executeQuery();

		} catch (Exception ex) {
			System.out.println(ex);
		}
		return rs;
	}

	public ResultSet visualizarListaTurnos(String desde, String hasta, String nombre, String telefono) {
		Connection con = conectar();
		ResultSet rs = null;

		try {
			PreparedStatement ps = con.prepareStatement(
					"SELECT * FROM turnos INNER JOIN reserva WHERE turnos.id_turno = reserva.id_turno AND reserva.fecha >= "
							+ desde + " AND reserva.fecha <= " + hasta
							+ " AND reserva.condicion < 4 AND reserva.condicion > 0" + nombre + telefono
							+ " GROUP BY reserva.id_turno");
			rs = ps.executeQuery();

		} catch (Exception ex) {
			System.out.println(ex);
		}
		return rs;
	}

}
