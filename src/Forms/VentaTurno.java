/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import Clases.Conexion;
import Clases.FiltrosTextField;
import Clases.VerTabla;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import javax.swing.JTextField;

/**
 *
 * @author Franco
 */
public class VentaTurno extends javax.swing.JInternalFrame {
    VerTabla v = new VerTabla();
    Conexion conn = new Conexion();
    Integer ide;
    JTextField deuda;
    Date now;
    JTextField kios;
    FiltrosTextField filtros = new FiltrosTextField();
    public VentaTurno(JTextField debe, JTextField kiosco, Integer id, Date dia) {
        initComponents();
        deuda = debe;
        now = dia;
        kios = kiosco;
        ide = id;
        TextAutoCompleter textAutoCompleter = new TextAutoCompleter(txtCodigo);
            Connection con = conn.conectar();
            ResultSet rs = null;
            try{
                PreparedStatement ps = con.prepareStatement("SELECT * FROM productos");
                rs = ps.executeQuery();
                while(rs.next()){
                    textAutoCompleter.addItem(rs.getString(3));
                }
            }catch(Exception ex){
                System.out.println("No hay registros");
            }  
        
        
        v.visualizar_productos(tablaProductos);
        v.visualizar_consumo(tablaVenta, id, txtTotal, debe);

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM turnos WHERE id_turno = " + id);
            rs = ps.executeQuery();
            while (rs.next()) {
                txtNombre.setText(rs.getString(2));
            }
        } catch (Exception ex) {
            System.out.println("No hay registros");
        }

        txtCodigo.setFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        
        txtCodigo.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_TAB){
                    String code = txtCodigo.getText();
                    
                    ResultSet rs = conn.seleccionarProducto(code);
                    try{
                        while(rs.next()){
                            conn.guardarconsumo(rs.getString(3), rs.getDouble(4), id, rs.getInt(7), rs.getString(2), rs.getDouble(6));
                        }
                    }catch(Exception ex){
                        System.out.println(ex);
                    }
                        
                        
                    v.visualizar_consumo(tablaVenta, id, txtTotal, debe);
                    txtCodigo.setText("");
                    txtCodigo.requestFocusInWindow();
                }
                
                if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    
                        Connection con = conn.conectar();
                        ResultSet rs = null;

                        try{
                            PreparedStatement ps = con.prepareStatement("SELECT * FROM consumoturno WHERE id_turno = "+ide);
                            rs = ps.executeQuery();
                            
                            while(rs.next()){
                                conn.guardarventa(rs.getInt(1), rs.getString(2), rs.getDouble(3), now, rs.getInt(5), rs.getString(6), 0, rs.getDouble(7));
                            }
                        }catch(Exception ex){
                            System.out.println("No hay registros");
                        }
                        
                        try{
                            PreparedStatement ps = con.prepareStatement("SELECT * FROM totaldia");
                            rs = ps.executeQuery();
                            Double total = 0.00;
                            while(rs.next()){
                                total = total + rs.getDouble(2);
                            }
                            kiosco.setText("$"+total);
                        }catch(Exception ex){
                            System.out.println("No hay registros");
                        }
                        
                        

                        conn.vaciar_consumo(id);
                        v.visualizar_consumo(tablaVenta, id, txtTotal, debe);
                        v.visualizar_productos(tablaProductos);
                        txtCodigo.setText("");

                }
                txtCodigo.requestFocusInWindow();

           }
        });
        
        
        tablaProductos.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent evt) {
                String code = tablaProductos.getValueAt(tablaProductos.getSelectedRow(), 0).toString();
                    
                    ResultSet rs = conn.seleccionarProducto(code);
                    try{
                        while(rs.next()){
                            conn.guardarconsumo(rs.getString(3), rs.getDouble(4), id, rs.getInt(7), rs.getString(2), rs.getDouble(6));
                        }
                    }catch(Exception ex){
                        System.out.println(ex);
                    }
                        
                        
                    v.visualizar_consumo(tablaVenta, id, txtTotal, debe);
                    txtCodigo.setText("");
                    txtCodigo.requestFocusInWindow();
        }
    });
        
        tablaVenta.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent evt) {  
                String nombre = tablaVenta.getValueAt(tablaVenta.getSelectedRow(), 3).toString();
                conn.borrarconsumo(nombre, id);
                v.visualizar_consumo(tablaVenta, id, txtTotal, debe);
                        
                txtCodigo.setText("");
                txtCodigo.requestFocusInWindow();
        }
    });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        btnCancelar = new javax.swing.JButton();
        txtNombre = new javax.swing.JTextField();
        btnAceptar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaVenta = new javax.swing.JTable();

        setClosable(true);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Codigo:");

        txtCodigo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Total:");

        txtTotal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtTotal.setFocusable(false);
        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        txtNombre.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtNombre.setFocusable(false);

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCodigo))
                    .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaProductos);

        tablaVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaVenta.setRequestFocusEnabled(false);
        jScrollPane2.setViewportView(tablaVenta);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        Connection con = conn.conectar();
        String borrar = "DELETE FROM consumoturno WHERE id_turno = "+ide;
        PreparedStatement ps = null;
        try{  
            ps = con.prepareStatement(borrar);
            ps.executeUpdate();
            
        }catch(Exception ex){
            System.out.println(ex);
        }
        txtCodigo.requestFocusInWindow();
        v.visualizar_consumo(tablaVenta, ide, txtTotal, deuda);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE){
            dispose();
            kios.requestFocus(true);
        }
    }//GEN-LAST:event_txtCodigoKeyPressed

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalActionPerformed

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        
        Connection con = conn.conectar();
        ResultSet rs = null;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM consumoturno WHERE id_turno = " + ide);
            rs = ps.executeQuery();

            while (rs.next()) {
                conn.guardarventa(rs.getInt(1), rs.getString(2), rs.getDouble(3), now, rs.getInt(5), rs.getString(6), 0, rs.getDouble(7));
            }
        } catch (Exception ex) {
            System.out.println("No hay registros");
        }

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM totaldia");
            rs = ps.executeQuery();
            Double total = 0.00;
            while (rs.next()) {
                total = total + rs.getDouble(2);
            }
            kios.setText("$" + total);
        } catch (Exception ex) {
            System.out.println("No hay registros");
        }

        conn.vaciar_consumo(ide);
        v.visualizar_consumo(tablaVenta, ide, txtTotal, deuda);
        v.visualizar_productos(tablaProductos);
        txtCodigo.setText("");
        txtCodigo.requestFocusInWindow();
    }//GEN-LAST:event_btnAceptarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTable tablaVenta;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
