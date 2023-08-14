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
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Collections;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Franco
 */
public class ConFactura extends javax.swing.JInternalFrame {
    
    FiltrosTextField filtros = new FiltrosTextField();
    Conexion conn = new Conexion();
    VerTabla v = new VerTabla();
    Integer id = 0;
    Integer ucaja = 0;
    String factura = "";
    String fecha = "";
    
    public ConFactura(String factu) {
        initComponents();
        factura = factu;
        v.visualizar_stock(tablaProductos, factura);
        filtros.filtrotamaño(txtCodigo, 19);
        filtros.filtrotamaño(txtProducto, 19);
        filtros.filtrotamaño(txtUnidades, 9);
        filtros.filtroeneteros(txtUnidades);
        filtros.filtrodecimales(txtCosto);
        filtros.filtrotamañodecimal(txtCosto, 9);
        txtProducto.requestFocusInWindow();
        txtFactura.setText(factura);
        
        String formato =  "yyyyMMdd";
        java.util.Date dsd = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        fecha = String.valueOf(sdf.format(dsd));
        
        
        TextAutoCompleter textAutoCompleter = new TextAutoCompleter(txtProducto);
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
            
        txtProducto.setFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
            
        txtProducto.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_TAB){
                    Boolean existe = false;
                    Connection con = conn.conectar();
                    ResultSet rs = null;

                    try{
                        PreparedStatement ps = con.prepareStatement("SELECT * FROM productos WHERE (producto = '"+txtProducto.getText()+"') OR (codigo = '"+txtProducto.getText()+"')");
                        rs = ps.executeQuery();
                        while(rs.next()){
                            existe = true;
                            txtProducto.setText(rs.getString(3));
                            txtCodigo.setText(rs.getString(2));
                            txtStock.setText(rs.getString(5));
                            id = rs.getInt(1);
                        }
                    }catch(Exception ex){
                        System.out.println(ex);
                    }
                    if(existe){
                        txtProducto.setEditable(false);
                        txtUnidades.requestFocusInWindow();
                    }else{
                        JOptionPane.showMessageDialog(null, "El Producto Indicado No Se encuentra En La Base De Datos", "Error Al Cargar", JOptionPane.ERROR_MESSAGE);
                        txtProducto.setText("");
                        txtProducto.requestFocusInWindow();
                    }
                }
           }
        });    

        
        tablaProductos.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent evt) { 
                Connection con = conn.conectar();
                String nombre = tablaProductos.getValueAt(tablaProductos.getSelectedRow(), 0).toString();
                String delete = "DELETE FROM stockfactura WHERE producto = '" + nombre + "'";
                PreparedStatement ps = null;
                try {
                    ps = con.prepareStatement(delete);
                    ps.execute();
                } catch (Exception ex) {
                    System.out.println("Error al borrar");
                }
                txtProducto.requestFocusInWindow();
                v.visualizar_stock(tablaProductos, factura);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnGuardarProducto = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        txtCodigo = new javax.swing.JTextField();
        txtProducto = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtUnidades = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtFactura = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtCosto = new javax.swing.JTextField();

        setClosable(true);

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaProductos);

        jPanel1.setBackground(new java.awt.Color(51, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnGuardarProducto.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnGuardarProducto.setText("Guardar");
        btnGuardarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProductoActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnAgregar.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnGuardarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                    .addComponent(btnGuardarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(0, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtCodigo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtCodigo.setFocusable(false);

        txtProducto.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Codigo:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Producto:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel9.setText("Cantidad:");

        txtUnidades.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Stock:");

        txtStock.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtStock.setFocusable(false);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel4.setText("Factura:");

        txtFactura.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtFactura.setFocusable(false);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setText("Costo:");

        txtCosto.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProducto))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFactura)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProductoActionPerformed
        Boolean existe = false;
        try{            
            ResultSet cargas = conn.visualizarstock(factura);
            while (cargas.next()) {
                existe = true;
            }
        }catch(Exception ex){
            System.out.println(ex);
        }
        
        if(existe){
            conn.cargarfactura(factura, fecha);
            JOptionPane.showMessageDialog(null, "Factura Cargada Con Exito", "", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }else{
            JOptionPane.showMessageDialog(null, "Asegurese De Agregar Almenos Un Producto", "No Se Pudo Cargar La Factura", JOptionPane.ERROR_MESSAGE);
            txtProducto.requestFocusInWindow();
            return;
        }
    }//GEN-LAST:event_btnGuardarProductoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        txtCodigo.setText("");
        txtProducto.setText("");
        txtCosto.setText("");
        txtUnidades.setText("");
        txtStock.setText("");
        conn.vaciar("stockfactura");
        v.visualizar_stock(tablaProductos, factura);
        txtProducto.setEditable(true);
        txtProducto.requestFocusInWindow();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        if(txtUnidades.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Debe indicar la cantidad de unidades", "Error al Agregar", JOptionPane.WARNING_MESSAGE);
            txtUnidades.requestFocusInWindow();
            return;
        }
        if(txtProducto.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Debe indicar que producto actualizar", "Error al Agregar", JOptionPane.WARNING_MESSAGE);
            txtProducto.requestFocusInWindow();
            return;
        }
        if(txtCodigo.getText().equals("")){
            JOptionPane.showMessageDialog(null, "El Producto No Se Encuentra En La Base De Datos", "Error al Agregar", JOptionPane.WARNING_MESSAGE);
            txtProducto.requestFocusInWindow();
            return;
        }
        Integer compro = Integer.parseInt(txtUnidades.getText());
        
        if(compro < 1){
            JOptionPane.showMessageDialog(null, "Debe agregar almenos 1 unidad para actualizar el stock", "Error al guardar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Boolean actualizar = false;
        if (!txtCosto.getText().equals("")) {
            Double costo = Double.parseDouble(txtCosto.getText());
            Connection con = conn.conectar();
            ResultSet rs = null;
            try {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM productos WHERE producto = '" + txtProducto.getText() + "' AND costo <> " + costo);
                rs = ps.executeQuery();
                while (rs.next()) {
                    actualizar = true;
                }
            } catch (Exception ex) {
                System.out.println("No hay registros");
            }
            if (actualizar) {
                String[] options = {"Actualizar", "Cancelar"};
                int seleccion = JOptionPane.showOptionDialog(null, "Desea Actualizar El Costo Del Producto", "Actualizar Costo", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (seleccion == 0) {
                    String insert = "UPDATE productos SET costo = ? WHERE producto = '" + txtProducto.getText() + "'";
                    PreparedStatement ps = null;
                    try {
                        ps = con.prepareStatement(insert);
                        ps.setDouble(1, costo);

                        ps.executeUpdate();

                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }

        }
        
        conn.cargarstock(factura, txtProducto.getText(), Integer.parseInt(txtUnidades.getText()));
        
        txtCodigo.setText("");
        txtUnidades.setText("");
        txtProducto.setText("");
        txtStock.setText("");
        txtCosto.setText("");
        txtProducto.setEditable(true);
        txtProducto.requestFocusInWindow();
        v.visualizar_stock(tablaProductos, factura);
    }//GEN-LAST:event_btnAgregarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardarProducto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JTextField txtFactura;
    private javax.swing.JTextField txtProducto;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtUnidades;
    // End of variables declaration//GEN-END:variables
}
