/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import Clases.Conexion;
import Clases.FiltrosTextField;
import Clases.VerTabla;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Franco
 */
public class Productos extends javax.swing.JInternalFrame {
    
    FiltrosTextField filtros = new FiltrosTextField();
    Conexion conn = new Conexion();
    VerTabla v = new VerTabla();
    Integer id = 0;
    Boolean editar = false;
    
    public Productos() {
        initComponents();
        v.visualizar_productos(tablaProductos);
        filtros.filtrotamaño(txtCodigo, 19);
        filtros.filtrotamaño(txtProducto, 19);
        filtros.filtrodecimales(txtPrecio);
        filtros.filtrotamañodecimal(txtPrecio, 9);
        filtros.filtrodecimales(txtCosto);
        filtros.filtrotamañodecimal(txtCosto, 9);
        filtros.filtrotamañodecimal(txtGanancia, 7);
        filtros.filtrodecimales(txtGanancia);
        
        
        txtGanancia.addKeyListener(new KeyAdapter(){
                public void keyReleased(KeyEvent e){
                    if(!txtCosto.getText().equals("") && !txtGanancia.getText().equals("")){
                        Double costo = Double.parseDouble(txtCosto.getText());
                        Double ganar = Double.parseDouble(txtGanancia.getText());
                        Double preco = costo + (costo * ganar)/100.0;
                        txtPrecio.setText(""+preco);
                    }

               }
            });
        
        txtPrecio.addKeyListener(new KeyAdapter(){
                public void keyReleased(KeyEvent e){
                    if(!txtCosto.getText().equals("") && !txtPrecio.getText().equals("")){
                        Double preco = Double.parseDouble(txtPrecio.getText());
                        Double costo = Double.parseDouble(txtCosto.getText());
                        Double ganar = ((preco - costo) / costo) * 100.0;
                        txtGanancia.setText(""+ganar);
                    }

               }
            });
        
        
        tablaProductos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent event) {
                btnEditarProducto.setEnabled(true);
                btnBorrar.setEnabled(true);
                btnNuevo.setEnabled(true);
                String code = tablaProductos.getValueAt(tablaProductos.getSelectedRow(), 0).toString();
                ResultSet rs = conn.seleccionarProducto(code);
                try{
                    while(rs.next()){
                        DecimalFormat df = new DecimalFormat("#.00");
                        Double ganancia = 0.00;
                        txtProducto.setText(rs.getString(3));
                        txtCodigo.setText(rs.getString(2));
                        txtPrecio.setText(rs.getString(4));
                        txtCosto.setText(rs.getString(6));
                        txtStock.setText(rs.getString(5));
                        id = rs.getInt(1);
                        ganancia = ((rs.getDouble(4) - rs.getDouble(6)) / rs.getDouble(6))*100;
                        txtGanancia.setText(""+df.format(ganancia));
                        
                        Integer category = rs.getInt(7);
                        cmbCategoria.setSelectedIndex(category);
                        
                    }
                }catch(Exception ex){
                    System.out.println(ex);
                }     
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
        btnBorrar = new javax.swing.JButton();
        btnGuardarProducto = new javax.swing.JButton();
        btnEditarProducto = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        txtPrecio = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        txtProducto = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCosto = new javax.swing.JTextField();
        cmbCategoria = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtGanancia = new javax.swing.JTextField();

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

        jPanel1.setBackground(new java.awt.Color(255, 255, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnBorrar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnBorrar.setText("Borrar");
        btnBorrar.setEnabled(false);
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        btnGuardarProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnGuardarProducto.setText("Guardar");
        btnGuardarProducto.setEnabled(false);
        btnGuardarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProductoActionPerformed(evt);
            }
        });

        btnEditarProducto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnEditarProducto.setText("Editar");
        btnEditarProducto.setEnabled(false);
        btnEditarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProductoActionPerformed(evt);
            }
        });

        btnNuevo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setEnabled(false);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnEditarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                    .addComponent(btnBorrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEditarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtPrecio.setFocusable(false);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Precio:");

        txtCodigo.setFocusable(false);

        txtProducto.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtProducto.setFocusable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Codigo:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Producto:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Costo:");

        txtCosto.setFocusable(false);

        cmbCategoria.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cmbCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kiosco", "Bebida" }));
        cmbCategoria.setEnabled(false);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Categoria:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel4.setText("Stock:");

        txtStock.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtStock.setFocusable(false);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Porcentaje de Ganancia:");

        txtGanancia.setFocusable(false);

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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGanancia, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addGap(10, 10, 10)
                        .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodigo))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addGap(10, 10, 10)
                                .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(txtGanancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(90, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProductoActionPerformed
        String codigo;
        Double precio;
        Double costo;
        Integer stock = 0;
        String nombre;
        Integer categoria = cmbCategoria.getSelectedIndex();
        
        
        if(editar){
            codigo = txtCodigo.getText();
            if(txtCosto.getText().equals("")){
                costo = 0.00;
            }else{
                costo = Double.parseDouble(txtCosto.getText());
            }
            if(txtPrecio.getText().equals("")){
                precio = 0.00;
            }else{
                precio = Double.parseDouble(txtPrecio.getText());
            } 

            
            conn.editar_producto(id, codigo, precio, costo);
            
        }else{
            if(txtProducto.getText().equals("")){
            JOptionPane.showMessageDialog(null, "El nombre del producto es necesario", "Error al Guardar", JOptionPane.WARNING_MESSAGE);
            return;
            }else{
                Connection con = conn.conectar();
                ResultSet rs = null;
                Integer existe = 0;
                nombre = txtProducto.getText();

                try{
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM productos WHERE producto = '"+nombre+"'");
                    rs = ps.executeQuery();

                    while(rs.next()){
                        existe = 1;
                    }
                    if(existe == 1){
                        JOptionPane.showMessageDialog(null, "El producto ya se encuentra en la base de datos", "Error al Guardar", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                }catch(Exception ex){
                    System.out.println("Error de consulta");
                }
                }
            if(txtCodigo.getText().equals("")){
                codigo = txtProducto.getText();
            }else{
                codigo = txtCodigo.getText();
                Connection con = conn.conectar();
                ResultSet rs = null;
                Integer existe = 0;

                try{
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM productos WHERE codigo = '"+codigo+"' OR codigocaja = '"+codigo+"'");
                    rs = ps.executeQuery();

                    while(rs.next()){
                        existe = 1;
                    }
                    if(existe == 1){
                        JOptionPane.showMessageDialog(null, "El codigo ya se encuentra en la base de datos", "Error al Guardar", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                }catch(Exception ex){
                    System.out.println("Error de consulta");
                }
            }
            if(txtCosto.getText().equals("")){
                costo = 0.00;
            }else{
                costo = Double.parseDouble(txtCosto.getText());
            }
            if(txtPrecio.getText().equals("")){
                precio = 0.00;
            }else{
                precio = Double.parseDouble(txtPrecio.getText());
            }

            conn.guardar_producto(codigo, nombre, precio, stock, costo, categoria);
        }

        txtCodigo.setFocusable(false);
        txtProducto.setFocusable(false);
        txtPrecio.setFocusable(false);
        txtGanancia.setFocusable(false);
        txtCosto.setFocusable(false);
        editar = false;
        cmbCategoria.setEnabled(false);
        
        txtCodigo.setText("");
        txtProducto.setText("");
        txtPrecio.setText("");
        txtCosto.setText("");
        txtStock.setText("");
        txtGanancia.setText("");
        
        btnGuardarProducto.setEnabled(false);
        btnBorrar.setEnabled(false);
        btnEditarProducto.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnNuevo.setEnabled(true);
        
        tablaProductos.setEnabled(true);
        v.visualizar_productos(tablaProductos);

    }//GEN-LAST:event_btnGuardarProductoActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        editar = false;
        tablaProductos.setEnabled(false);
        btnGuardarProducto.setEnabled(true);
        btnBorrar.setEnabled(false);
        btnEditarProducto.setEnabled(false);
        btnCancelar.setEnabled(true);
        btnNuevo.setEnabled(false);
        txtCodigo.setText("");
        txtProducto.setText("");
        txtProducto.requestFocusInWindow();
        txtPrecio.setText("");
        txtCosto.setText("");
        txtGanancia.setText("");
        cmbCategoria.setSelectedIndex(0);
        
        txtCodigo.setFocusable(true);
        txtProducto.setFocusable(true);
        txtGanancia.setFocusable(true);
        txtProducto.requestFocusInWindow();
        txtPrecio.setFocusable(true);
        txtCosto.setFocusable(true);
        cmbCategoria.setEnabled(true);
        tablaProductos.setEnabled(false);
        txtProducto.requestFocusInWindow();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnEditarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarProductoActionPerformed
        btnGuardarProducto.setEnabled(true);
        btnBorrar.setEnabled(false);
        btnEditarProducto.setEnabled(false);
        btnCancelar.setEnabled(true);
        btnNuevo.setEnabled(false);
        tablaProductos.setEnabled(false);
        
        txtPrecio.setFocusable(true);
        txtGanancia.setFocusable(true);
        txtPrecio.requestFocusInWindow();
        txtCosto.setFocusable(true);
        editar = true;
    }//GEN-LAST:event_btnEditarProductoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        btnNuevo.setEnabled(true);
        btnGuardarProducto.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnBorrar.setEnabled(false);
        tablaProductos.setEnabled(true);
        
        txtCodigo.setText("");
        txtProducto.setText("");
        txtPrecio.setText("");
        txtCosto.setText("");
        txtGanancia.setText("");
        txtGanancia.setFocusable(false);
        txtCodigo.setFocusable(false);
        txtProducto.setFocusable(false);
        txtPrecio.setFocusable(false);
        txtCosto.setFocusable(false);
        cmbCategoria.setEnabled(false);
        editar = false;
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        tablaProductos.setEnabled(false);
        conn.eliminar_producto(id);
        v.visualizar_productos(tablaProductos);
        tablaProductos.setEnabled(true);
        
        txtCodigo.setText("");
        txtProducto.setText("");
        txtPrecio.setText("");
        txtCosto.setText("");
        txtGanancia.setText("");
        txtStock.setText("");
        cmbCategoria.setSelectedIndex(0);
        btnGuardarProducto.setEnabled(false);
        btnBorrar.setEnabled(false);
        btnEditarProducto.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnNuevo.setEnabled(true);
        
    }//GEN-LAST:event_btnBorrarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEditarProducto;
    private javax.swing.JButton btnGuardarProducto;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> cmbCategoria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JTextField txtGanancia;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtProducto;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables
}