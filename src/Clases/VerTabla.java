/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Franco
 */
public class VerTabla {

    public void visualizar_tabla(JTable tabla, String fecha, String fecha2, JTextField TotalTurnosDia) {
        Conexion conn = new Conexion();

        Integer TurnosTotales = 0;
        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Horario");
        Integer numero = 0;
        
        ResultSet rts = null;
        Connection con = conn.conectar();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT cantidad FROM canchas");
            rts = ps.executeQuery();
            while(rts.next()){
                numero = rts.getInt(1);
            }
        }catch(Exception ex){
            System.out.println(ex);
        }
        
        Integer contador = 1;
        while(numero >= contador){
            dt.addColumn("Cancha "+contador);
            contador = contador + 1;
        }
        


        for (int j = 1; j < 25; j++) {
            Object[] fila = new Object[1];
            if(j < 10){
                fila[0] = "0"+j + ":00 HS";
            }else{
                fila[0] = j + ":00 HS";
            }
            dt.addRow(fila);
        }

        try {
            ResultSet rs = conn.visualizar(fecha, fecha2);
            while (rs.next()) {
                TurnosTotales = TurnosTotales +1;
                String condicion = "";
                Integer fijo = rs.getInt(12);
                Integer hor = rs.getInt(8);
                Integer canch = rs.getInt(7);
                if (rs.getInt(10) == 8) {
                    condicion = " [TORN]";
                }
                if (rs.getInt(10) == 9) {
                    condicion = " [ACTI]";
                }
                if (rs.getInt(10) == 1) {
                    condicion = " [JUGANDO]";
                } else if (rs.getInt(10) == 2) {
                    condicion = " [FALTO]";
                } else if (rs.getInt(10) == 3) {
                    condicion = " [PAGO]";
                } else if (rs.getInt(10) == 5) {
                    condicion = " [FALTA MEDIA]";
                } else if (rs.getInt(10) == 6) {
                    condicion = " [TERMINADO]";
                }
                condicion = rs.getObject(2) + condicion;
                if(fijo == 1){
                    condicion = "[F]"+condicion;
                }
                
                if(canch <= numero){
                    dt.setValueAt(condicion, hor - 1, canch);
                }
                
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
 
        TotalTurnosDia.setText(""+TurnosTotales);
        
        tabla.setShowGrid(true);
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.setModel(dt);
        tabla.setRowHeight(60);
        TableColumnModel columnModel = tabla.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(80);
        
        contador = 1;
        while(numero >= contador){
            columnModel.getColumn(contador).setCellRenderer(new MiRender());
            contador = contador + 1;
        }  

    }
    
    public void visualizar_tabla2(JTable tabla, String fecha, String fecha2) {
        Conexion conn = new Conexion();

        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Horario");
        Integer numero = 0;
        
        ResultSet rts = null;
        Connection con = conn.conectar();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT cantidad FROM canchas");
            rts = ps.executeQuery();
            while(rts.next()){
                numero = rts.getInt(1);
            }
        }catch(Exception ex){
            System.out.println(ex);
        }
        
        Integer contador = 1;
        while(numero >= contador){
            dt.addColumn("Cancha "+contador);
            contador = contador + 1;
        }
        


        for (int j = 1; j < 25; j++) {
            Object[] fila = new Object[1];
            if(j < 10){
                fila[0] = "0"+j + ":00 HS";
            }else{
                fila[0] = j + ":00 HS";
            }
            dt.addRow(fila);
        }

        try {
            ResultSet rs = conn.visualizar(fecha, fecha2);
            while (rs.next()) {
                String condicion = "";
                Integer fijo = rs.getInt(12);
                Integer hor = rs.getInt(8);
                Integer canch = rs.getInt(7);
                if (rs.getInt(10) == 8) {
                    condicion = " [TORN]";
                }
                if (rs.getInt(10) == 9) {
                    condicion = " [ACTI]";
                }
                if (rs.getInt(10) == 1) {
                    condicion = " [JUGANDO]";
                } else if (rs.getInt(10) == 2) {
                    condicion = " [FALTO]";
                } else if (rs.getInt(10) == 3) {
                    condicion = " [PAGO]";
                } else if (rs.getInt(10) == 5) {
                    condicion = " [FALTA MEDIA]";
                } else if (rs.getInt(10) == 6) {
                    condicion = " [TERMINADO]";
                }
                condicion = rs.getObject(2) + condicion;
                if(fijo == 1){
                    condicion = "[F]"+condicion;
                }
                
                if(canch <= numero){
                    dt.setValueAt(condicion, hor - 1, canch);
                }
                
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
 
        
        tabla.setShowGrid(true);
        tcr.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.setModel(dt);
        tabla.setRowHeight(60);
        TableColumnModel columnModel = tabla.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(80);
        
        contador = 1;
        while(numero >= contador){
            columnModel.getColumn(contador).setCellRenderer(new MiRender());
            contador = contador + 1;
        }  

    }

    public void visualizar_productos(JTable tabla) {
        Conexion conn = new Conexion();
        ResultSet rs = conn.visualizarProducto();

        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Codigo");
        dt.addColumn("Nombre");
        dt.addColumn("Precio");
        dt.addColumn("Costo");
        dt.addColumn("Stock");
        dt.addColumn("Categoria");

        try {
            while (rs.next()) {
                Object[] fila = new Object[6];

                fila[0] = rs.getObject(2);
                fila[1] = rs.getObject(3);
                fila[2] = "$" + rs.getObject(4);
                fila[3] = "$" + rs.getObject(6);
                fila[4] = rs.getObject(5);

                Integer categoria = rs.getInt(7);

                if (categoria == 0) {
                    fila[5] = "Kiosco";
                } else {
                    fila[5] = "Bebida";
                }

                dt.addRow(fila);
            }
            tabla.setModel(dt);
            tabla.setRowHeight(40);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(100);
            columnModel.getColumn(1).setPreferredWidth(200);
            columnModel.getColumn(2).setPreferredWidth(50);
            columnModel.getColumn(3).setPreferredWidth(50);
            columnModel.getColumn(4).setPreferredWidth(50);
            columnModel.getColumn(5).setPreferredWidth(100);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void consultastock(JTable tabla) {
        Conexion conn = new Conexion();
        ResultSet rs = conn.visualizarProducto();

        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Codigo");
        dt.addColumn("Nombre");
        dt.addColumn("Stock");

        try {
            while (rs.next()) {
                Object[] fila = new Object[3];

                fila[0] = rs.getObject(2);
                fila[1] = rs.getObject(3);
                fila[2] = rs.getObject(5);

                dt.addRow(fila);
            }
            tabla.setModel(dt);
            tabla.setRowHeight(40);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(100);
            columnModel.getColumn(1).setPreferredWidth(200);
            columnModel.getColumn(2).setPreferredWidth(50);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    
    
    public void visualizar_stock(JTable tabla, String factura) {
        Conexion conn = new Conexion();
        ResultSet rs = conn.visualizarstock(factura);

        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Nombre");
        dt.addColumn("Cantidad");

        try {
            while (rs.next()) {
                Object[] fila = new Object[2];

                fila[0] = rs.getString(2);
                fila[1] = rs.getInt(3);


                dt.addRow(fila);
            }
            tabla.setModel(dt);
            tabla.setRowHeight(40);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(200);
            columnModel.getColumn(1).setMaxWidth(100);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public void visualizar_facturas(JTable tabla, String factura, String desde, String hasta) {
        Conexion conn = new Conexion();
        ResultSet rs = conn.visualizarfacturas(factura, desde, hasta);

        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Numero");
        dt.addColumn("Fecha");
        

        try {
            while (rs.next()) {
                Object[] fila = new Object[2];

                fila[0] = rs.getString(2);
                String formato =  "EEEE dd 'de' MMMM 'de' yyyy";
                java.util.Date dsd = rs.getDate(3);
                SimpleDateFormat sdf = new SimpleDateFormat(formato);
                String fecha = String.valueOf(sdf.format(dsd));

                fila[1] = fecha;


                dt.addRow(fila);
            }
            tabla.setModel(dt);
            tabla.setRowHeight(30);
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();

            tabla.setShowGrid(true);
            tcr.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    
    public void visualizar_cargas(JTable tabla, String factura) {
        Conexion conn = new Conexion();
        ResultSet rs = conn.visualizarcargas(factura);

        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Producto");
        dt.addColumn("Cantidad");
        

        try {
            while (rs.next()) {
                Object[] fila = new Object[2];

                fila[0] = rs.getString(1);
                fila[1] = rs.getInt(2);


                dt.addRow(fila);
            }
            tabla.setModel(dt);
            tabla.setRowHeight(30);
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();

            tabla.setShowGrid(true);
            tcr.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public void visualizar_ventas(JTable tabla, String desde, String hasta, Integer user, String codigo, Integer category, JTextField cantidad, JTextField total, JTextField costo, JTextField ganancia) {
        Conexion conn = new Conexion();
        ResultSet rs = conn.visualizarVentas(desde, hasta, user, codigo, category);
        Integer cantida = 0;
        Double toti = 0.00;
        Double costi = 0.00;

        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Cantidad");
        dt.addColumn("Nombre");
        dt.addColumn("Total");
        dt.addColumn("Categoria");
        dt.addColumn("Consumidor");

        try {
            while (rs.next()) {
                Object[] fila = new Object[5];

                fila[0] = rs.getObject(1);
                cantida = cantida + rs.getInt(1);
                fila[1] = rs.getObject(2);
                fila[2] = "$" + rs.getObject(3);

                if (rs.getInt(7) == 0) {
                    fila[4] = "General";
                    toti = toti + rs.getDouble(3);
                } else if (rs.getInt(7) == 1) {
                    fila[4] = "Regalo";
                } else if (rs.getInt(7) == 2) {
                    fila[4] = "Consumo Local";
                } else {
                    fila[4] = "Sebastian";
                }

                costi = costi + rs.getDouble(8);

                Integer categoria = rs.getInt(5);

                if (categoria == 0) {
                    fila[3] = "Kiosco";
                } else {
                    fila[3] = "Bebida";
                }

                dt.addRow(fila);
            }
            
            DecimalFormat df = new DecimalFormat("#.00");
            
            
            Double gananci = toti - costi;
            cantidad.setText("" + cantida);
            total.setText("$" + df.format(toti));
            costo.setText("$" + df.format(costi));
            ganancia.setText("$" + df.format(gananci));
            tabla.setModel(dt);
            tabla.setRowHeight(40);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(0);
            columnModel.getColumn(2).setPreferredWidth(200);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void visualizar_turnos(JTable tabla) {
        Conexion conn = new Conexion();
        ResultSet rs = conn.visualizarprecios();

        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Precio");

        try {
            while (rs.next()) {
                Object[] fila = new Object[1];
                fila[0] = "$"+rs.getObject(1);

                dt.addRow(fila);
            }
            tabla.setModel(dt);
            tabla.setRowHeight(40);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(0);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void visualizar_consumo(JTable tabla, Integer id, JTextField total, JTextField debe) {
        Conexion conn = new Conexion();
        ResultSet rs = conn.visualizarConsumo(id);
        Double toti = 0.00;

        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Cantidad");
        dt.addColumn("Producto");
        dt.addColumn("Total");
        dt.addColumn("");

        try {
            while (rs.next()) {
                Object[] fila = new Object[4];

                fila[0] = rs.getObject(1);
                fila[1] = rs.getObject(2);
                fila[2] = rs.getObject(3);
                toti = toti + rs.getDouble(3);
                fila[3] = rs.getObject(6);

                dt.addRow(fila);
            }
            tabla.setModel(dt);
            tabla.setRowHeight(40);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(80);
            columnModel.getColumn(1).setPreferredWidth(200);
            columnModel.getColumn(3).setPreferredWidth(1);

        } catch (Exception ex) {
            System.out.println(ex);
        }

        total.setText("" + toti);
        debe.setText("$" + toti);
    }

    public void visualizar_laventa(JTable tabla, JTextField total) {
        Conexion conn = new Conexion();
        ResultSet rs = conn.visualizarLaventa();
        Double toti = 0.00;

        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Cantidad");
        dt.addColumn("Producto");
        dt.addColumn("Total");
        dt.addColumn("");

        try {
            while (rs.next()) {
                Object[] fila = new Object[4];

                fila[0] = rs.getObject(1);
                fila[1] = rs.getObject(2);
                fila[2] = rs.getObject(3);
                toti = toti + rs.getDouble(3);
                fila[3] = rs.getObject(5);

                dt.addRow(fila);
            }
            tabla.setModel(dt);
            tabla.setRowHeight(40);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(80);
            columnModel.getColumn(1).setPreferredWidth(200);
            columnModel.getColumn(3).setPreferredWidth(1);

        } catch (Exception ex) {
            System.out.println(ex);
        }

        total.setText("" + toti);
    }

    public void visualizar_deuda(Integer id, JTextField debe) {
        Conexion conn = new Conexion();
        ResultSet rs = conn.visualizarConsumo(id);
        Double toti = 0.00;
        try {
            while (rs.next()) {
                toti = toti + rs.getDouble(3);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }

        debe.setText("$" + toti);
    }
    
    public void visualizar_listaturno(JTable tabla, String desde, String hasta, String nombre, String telefono, Integer asist, Integer falt, JTextField txttotal) {
        Conexion conn = new Conexion();
        ResultSet rs = conn.visualizarListaTurnos(desde, hasta, nombre, telefono);

        DefaultTableModel dt = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dt.addColumn("Nombre");
        dt.addColumn("Telefono");
        dt.addColumn("Asistencias");
        dt.addColumn("Faltas");
        dt.addColumn("Ingresado");

        Integer total = 0;
        
        try {
            while (rs.next()) {
                
                Connection con = conn.conectar();
                ResultSet rs2 = null;

                try {
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM reserva WHERE id_turno = "+rs.getInt(1)+" AND fecha >= " + desde + " AND fecha <= " + hasta);
                    rs2 = ps.executeQuery();

                } catch (Exception ex) {
                    System.out.println(ex);
                }
                Integer faltas = 0;
                Integer asistencias = 0;
                Integer pagado = 0;
                
                while (rs2.next()) {
                    if(rs2.getInt(5) == 3){
                        asistencias = asistencias +1;
                    }else if(rs2.getInt(5) == 2){
                        faltas = faltas +1;
                        
                    }
                    pagado = pagado + rs2.getInt(9);
                }
             
                if (asistencias >= asist) {
                    if (faltas >= falt) {
                        Object[] fila = new Object[5];

                        fila[0] = rs.getString(2);
                        fila[1] = rs.getString(4);
                        fila[2] = "" + asistencias;
                        fila[3] = "" + faltas;
                        fila[4] = "$" + pagado;

                        total = total + pagado;
                        dt.addRow(fila);
                    }
                }

            }
            
            txttotal.setText("$"+total);
            tabla.setModel(dt);
            tabla.setRowHeight(40);
            TableColumnModel columnModel = tabla.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(100);
            columnModel.getColumn(1).setPreferredWidth(200);
            columnModel.getColumn(2).setPreferredWidth(50);
            columnModel.getColumn(3).setPreferredWidth(50);
            columnModel.getColumn(4).setPreferredWidth(50);
            columnModel.getColumn(5).setPreferredWidth(100);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
}
