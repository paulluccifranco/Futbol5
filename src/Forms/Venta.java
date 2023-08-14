/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import Clases.Conexion;
import com.mxrck.autocompleter.TextAutoCompleter;
import Clases.FiltrosTextField;
import Clases.VerTabla;
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
public class Venta extends javax.swing.JInternalFrame {
    public int Posis = 0;
    VerTabla v = new VerTabla();
    Conexion conn = new Conexion();
    Date now;
    JTextField kios;
    FiltrosTextField filtros = new FiltrosTextField();
    
    public Venta(JTextField kiosco, Date dia) {
        initComponents();
        kios = kiosco;
        now = dia;
        v.visualizar_productos(tablaProductos);
        v.visualizar_laventa(tablaVenta, txtTotal);
        
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
        

        tablaVenta.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent evt) {
            if (evt.getClickCount() == 1) {   
                String codigo = tablaVenta.getValueAt(tablaVenta.getSelectedRow(), 3).toString();
                conn.borrarconsumoventa(codigo);
                v.visualizar_laventa(tablaVenta, txtTotal);
                        
                txtCodigo.setText("");
                txtCodigo.requestFocusInWindow();
            }
        }
        });
        
        txtCodigo.setFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        
        
        txtCodigo.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_TAB){
                    String code = txtCodigo.getText();

                    ResultSet rs = conn.seleccionarProducto(code);
                    try{
                        while(rs.next()){
                            conn.guardarconsumoventa(rs.getString(3), rs.getDouble(4), rs.getInt(7), rs.getString(2), rs.getDouble(6));
                        }
                    }catch(Exception ex){
                        System.out.println(ex);
                    }

                    txtCodigo.setText("");
                    v.visualizar_laventa(tablaVenta, txtTotal);
                }
                
                if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                        Integer user = cmbUser.getSelectedIndex();
                        
                        Connection con = conn.conectar();
                        ResultSet rs = null;

                        try{
                            PreparedStatement ps = con.prepareStatement("SELECT * FROM consumoventas");
                            rs = ps.executeQuery();
                            
                            while(rs.next()){
                                conn.guardarventa(rs.getInt(1), rs.getString(2), rs.getDouble(3), now, rs.getInt(4), rs.getString(5), user, rs.getDouble(6));
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

                        v.visualizar_productos(tablaProductos);
                        conn.vaciar("consumoventas");
                        v.visualizar_laventa(tablaVenta, txtTotal);

                        txtCodigo.setText("");
                        txtCodigo.requestFocusInWindow(); 
                        cmbUser.setSelectedIndex(0);
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
                            conn.guardarconsumoventa(rs.getString(3), rs.getDouble(4), rs.getInt(7), code, rs.getDouble(6));
                        }
                    }catch(Exception ex){
                        System.out.println(ex);
                    }
                    
                    
                    txtCodigo.setText("");
                    v.visualizar_laventa(tablaVenta, txtTotal);
            
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
        cmbUser = new javax.swing.JComboBox<>();
        btnAceptar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaVenta = new javax.swing.JTable();

        setClosable(true);

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Producto:");

        txtCodigo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Total:");

        txtTotal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtTotal.setFocusable(false);

        btnCancelar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        cmbUser.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        cmbUser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "General", "Regalo", "Consumo Local", "Sebastian" }));
        cmbUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbUserActionPerformed(evt);
            }
        });

        btnAceptar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
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
                    .addComponent(cmbUser, 0, 270, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCodigo)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(14, 14, 14)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel2)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
        tablaVenta.setFocusable(false);
        tablaVenta.setRequestFocusEnabled(false);
        jScrollPane2.setViewportView(tablaVenta);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        conn.vaciar("consumoventas");
        txtCodigo.requestFocusInWindow();
        cmbUser.setSelectedIndex(0);
        v.visualizar_laventa(tablaVenta, txtTotal);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE){
            dispose();
            kios.requestFocus(true);
        }
    }//GEN-LAST:event_txtCodigoKeyPressed

    private void cmbUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUserActionPerformed
        txtCodigo.requestFocusInWindow();
    }//GEN-LAST:event_cmbUserActionPerformed

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed

        Integer user = cmbUser.getSelectedIndex();

        Connection con = conn.conectar();
        ResultSet rs = null;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM consumoventas");
            rs = ps.executeQuery();

            while (rs.next()) {
                conn.guardarventa(rs.getInt(1), rs.getString(2), rs.getDouble(3), now, rs.getInt(4), rs.getString(5), user, rs.getDouble(6));
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

        v.visualizar_productos(tablaProductos);
        conn.vaciar("consumoventas");
        v.visualizar_laventa(tablaVenta, txtTotal);

        txtCodigo.setText("");
        txtCodigo.requestFocusInWindow();
        cmbUser.setSelectedIndex(0);
    }//GEN-LAST:event_btnAceptarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JComboBox<String> cmbUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTable tablaVenta;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
