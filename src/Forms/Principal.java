/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import Clases.Conexion;
import Clases.FiltrosTextField;
import Clases.RequestFocusListener;
import Clases.VerTabla;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import Clases.GenerarPDF;
import javax.swing.JTable;

/**
 *
 * @author Franco
 */
public class Principal extends javax.swing.JFrame {
    
    @Override
    public Image getIconImage() {
       Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("Imagenes/futbol.png"));
        return retValue;
    }
    
    GenerarPDF pdf = new GenerarPDF();
    Conexion conn = new Conexion();
    Connection con = conn.conectar();
    VerTabla v = new VerTabla();
    Integer idturno = 0;
    Integer idcopia = 0;
    Integer idreserva = 0;
    Integer condition = 0;
    Integer TotalTurnosDia = 0;
    Integer cancha;
    String fecha;
    String fecha2;
    Boolean editando = false;
    Boolean copiando = false;
    Integer hora;
    Date now = new Date(System.currentTimeMillis());
    FiltrosTextField filtros = new FiltrosTextField();
    public Principal() {
        initComponents();
        txtKiosco.setEditable(false);
        txtTurno.setEditable(false);
        txtSaldo.setEditable(false);
        txtTelefono.setEditable(false);
        filtros.filtrotamaño(txtComentario, 40);
        btnListo.setVisible(false);
        Boolean hayfecha = false;
        
        
        
        ResultSet rs;
        
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM fecha");
            rs = ps.executeQuery();
            while(rs.next()){
                now = rs.getDate(1);
                hayfecha = true;
            }
        }catch(Exception ex){
            System.out.println("No hay registros");
        }
   
        if (!hayfecha) {
            String insert = "INSERT INTO fecha (fecha) VALUES (?)";
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(insert);
                ps.setDate(1, now);

                ps.executeUpdate();

            } catch (Exception ex) {
                System.out.println("Error al guardar");
            }
        }

        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM totaldia");
            rs = ps.executeQuery();
            Double total = 0.00;
            while(rs.next()){
                total = total + rs.getDouble(2);
            }
            txtKiosco.setText("$"+total);
        }catch(Exception ex){
            System.out.println("No hay registros");
        }
        
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ventaturnos");
            rs = ps.executeQuery();
            Integer total = 0;
            while(rs.next()){
                total = total + rs.getInt(3);
            }
            txtTurnos.setText("$"+total);
        }catch(Exception ex){
            System.out.println("No hay registros");
        }
        
        
        
        
        txtComentario.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String insert = "UPDATE turnos SET comentario = ? WHERE id_turno = " + idturno;
                    PreparedStatement ps = null;
                    try {
                        ps = con.prepareStatement(insert);
                        ps.setString(1, txtComentario.getText());

                        ps.executeUpdate();

                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    txtKiosco.requestFocus();
                }

           }
        });
        
        
        txtKiosco.requestFocus();
        txtSaldo.setText("0");
        filtros.filtrotamaño(txtTurno, 19);
        filtros.evitarPegar(txtTurno);
        filtros.evitarPegar(txtTelefono);
        filtros.evitarPegar(txtSaldo);
        filtros.filtrotamaño(txtTelefono, 19);
        filtros.filtroeneteros(txtTelefono);
        filtros.filtrotamaño(txtSaldo, 5);
        filtros.filtroeneteros(txtSaldo);
        Fecha.setDate(now);
        String formato =  "yyyyMMdd";
        java.util.Date dsd = Fecha.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        fecha = String.valueOf(sdf.format(dsd));
        String formato3 =  "EEEE";
        java.util.Date dsd3 = Fecha.getDate();
        SimpleDateFormat sdf3 = new SimpleDateFormat(formato3);
        fecha2 = String.valueOf(sdf3.format(dsd3));
        
        Fecha.getDateEditor().addPropertyChangeListener("date", new PropertyChangeListener(){ 
            public void propertyChange(PropertyChangeEvent e) {
                String formato =  "yyyyMMdd";
                java.util.Date dsd = Fecha.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat(formato);
                fecha = String.valueOf(sdf.format(dsd));
                String formato3 =  "EEEE";
                java.util.Date dsd3 = Fecha.getDate();
                SimpleDateFormat sdf3 = new SimpleDateFormat(formato3);
                fecha2 = String.valueOf(sdf3.format(dsd3));
                v.visualizar_tabla(tabla, fecha, fecha2, txtTotalTurnosDia);
                

            }
        });

        v.visualizar_tabla(tabla, fecha, fecha2, txtTotalTurnosDia);
        tabla.setCellSelectionEnabled(true);
        tabla.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent evt) {
                if(!editando){
                cmbTipo.setEnabled(true);
                String formato =  "yyyyMMdd";
                java.util.Date dsd = Fecha.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat(formato);
                fecha = String.valueOf(sdf.format(dsd));
                String formato3 =  "EEEE";
                java.util.Date dsd3 = Fecha.getDate();
                SimpleDateFormat sdf3 = new SimpleDateFormat(formato3);
                fecha2 = String.valueOf(sdf3.format(dsd3));
                cancha = tabla.getSelectedColumn();
                hora = Integer.parseInt(tabla.getValueAt(tabla.getSelectedRow(), 0).toString().substring(0,2));
                
                    if (null == tabla.getValueAt(tabla.getSelectedRow(), tabla.getSelectedColumn())) {
                        if (!copiando) {
                            txtTurno.setText("");
                            txtSaldo.setText("0");
                            txtTelefono.setText("");
                            idturno = 0;
                            txtDebe.setText("");
                            cbFijo.setSelected(false);
                            cmbTipo.setSelectedIndex(0);

                            if (Fecha.getDate().before(now)) {
                                cbFijo.setEnabled(false);
                                txtTurno.setEditable(false);
                                btnEditar.setVisible(false);
                                btnGuardarturno.setVisible(true);
                                txtSaldo.setEditable(false);
                                txtTelefono.setEditable(false);
                                txtComentario.setEnabled(false);
                                btnJugando.setEnabled(false);
                                btnFalto.setEnabled(false);
                                btnMedia.setEnabled(false);
                                btnPago.setEnabled(false);
                                btnTerminado.setEnabled(false);
                                btnGuardarturno.setEnabled(false);
                                btnMover.setEnabled(false);
                                btnCopiar.setEnabled(false);
                                btnBorrarturno.setEnabled(false);
                                btnMasSeña.setEnabled(false);
                                btnMenosSeña.setEnabled(false);
                                cmbTipo.setSelectedIndex(0);
                            } else {
                                cbFijo.setEnabled(true);
                                txtTurno.setEditable(true);
                                btnEditar.setVisible(false);
                                btnGuardarturno.setVisible(true);
                                txtSaldo.setEditable(true);
                                txtTelefono.setEditable(true);
                                txtComentario.setEnabled(false);
                                btnJugando.setEnabled(false);
                                btnFalto.setEnabled(false);
                                btnMedia.setEnabled(false);
                                btnPago.setEnabled(false);
                                btnTerminado.setEnabled(false);
                                btnGuardarturno.setEnabled(true);
                                btnMover.setEnabled(false);
                                btnCopiar.setEnabled(false);
                                btnBorrarturno.setEnabled(false);
                                btnMasSeña.setEnabled(false);
                                btnMenosSeña.setEnabled(false);
                                cmbTipo.setEnabled(true);
                                txtTurno.requestFocusInWindow();
                            }
                        } else {
                            cbFijo.setEnabled(false);
                            txtTurno.setEditable(false);
                            btnEditar.setVisible(false);
                            btnGuardarturno.setVisible(true);
                            txtSaldo.setEditable(false);
                            txtTelefono.setEditable(false);
                            txtComentario.setEnabled(false);
                            btnJugando.setEnabled(false);
                            btnFalto.setEnabled(false);
                            btnMedia.setEnabled(false);
                            btnPago.setEnabled(false);
                            btnTerminado.setEnabled(false);
                            btnGuardarturno.setEnabled(true);
                            btnMover.setEnabled(false);
                            btnCopiar.setEnabled(false);
                            btnBorrarturno.setEnabled(false);
                            btnMasSeña.setEnabled(false);
                            btnMenosSeña.setEnabled(false);
                            cmbTipo.setEnabled(false);
                        }

                    } else {
                        if (!copiando) {
                            btnCopiar.setEnabled(true);
                            btnEditar.setVisible(true);
                            btnGuardarturno.setVisible(false);
                            try {
                                ResultSet rs = conn.visualizar_turno(hora, cancha, fecha);
                                String nombre = "";
                                Integer saldo = 0;
                                String telefono = "";
                                Integer fijo = 0;
                                while (rs.next()) {
                                    nombre = rs.getString(2);
                                    saldo = rs.getInt(3);
                                    telefono = rs.getString(4);
                                    fijo = rs.getInt(12);
                                    idturno = rs.getInt(1);
                                    condition = rs.getInt(10);
                                    idreserva = rs.getInt(11);
                                    txtComentario.setText(rs.getString(5));

                                }
                                if (condition > 7) {
                                    cmbTipo.setSelectedIndex(condition - 7);
                                } else {
                                    cmbTipo.setSelectedIndex(0);
                                }
                                if (fijo == 1) {
                                    cbFijo.setSelected(true);
                                } else {
                                    cbFijo.setSelected(false);
                                }
                                if (condition == 3 || condition == 2) {
                                    btnJugando.setEnabled(false);
                                    btnTerminado.setEnabled(false);
                                    btnFalto.setEnabled(false);
                                    btnPago.setEnabled(false);
                                    btnMedia.setEnabled(false);
                                    btnMover.setEnabled(false);
                                    btnBorrarturno.setEnabled(false);
                                } else if (condition == 5) {
                                    btnJugando.setEnabled(false);
                                    btnFalto.setEnabled(false);
                                    btnTerminado.setEnabled(false);
                                    btnPago.setEnabled(false);
                                    btnMedia.setEnabled(true);
                                    btnMover.setEnabled(false);
                                    btnBorrarturno.setEnabled(false);
                                } else if (condition == 6) {
                                    btnJugando.setEnabled(false);
                                    btnFalto.setEnabled(true);
                                    btnPago.setEnabled(true);
                                    btnTerminado.setEnabled(false);
                                    btnMedia.setEnabled(true);
                                    btnMover.setEnabled(false);
                                    btnBorrarturno.setEnabled(false);
                                } else {
                                    if (condition == 1) {
                                        btnJugando.setEnabled(false);
                                        btnTerminado.setEnabled(true);
                                        btnMover.setEnabled(false);
                                        btnBorrarturno.setEnabled(false);
                                    } else {
                                        btnBorrarturno.setEnabled(true);
                                        btnMover.setEnabled(true);
                                        btnJugando.setEnabled(true);
                                        btnTerminado.setEnabled(false);
                                    }
                                    if (!Fecha.getDate().equals(now)) {
                                        btnMedia.setEnabled(false);
                                        btnJugando.setEnabled(false);
                                        btnFalto.setEnabled(false);
                                        btnPago.setEnabled(false);
                                    } else {
                                        btnFalto.setEnabled(true);
                                        btnMedia.setEnabled(true);
                                        btnJugando.setEnabled(true);
                                        btnPago.setEnabled(true);
                                    }
                                }
                                txtTurno.setText(nombre);
                                txtComentario.setEnabled(true);
                                txtSaldo.setText("" + saldo);
                                txtTelefono.setText(telefono);
                            } catch (Exception ex) {
                                System.out.println("Error de consulta");
                            }

                            btnMenosSeña.setEnabled(true);
                            btnMasSeña.setEnabled(true);
                            cmbTipo.setEnabled(false);
                            cbFijo.setEnabled(false);
                            txtTurno.setEditable(false);
                            txtSaldo.setEditable(false);
                            txtTelefono.setEditable(false);
                            btnGuardarturno.setEnabled(false);

                            String valor = (String) tabla.getValueAt(tabla.getSelectedRow(), tabla.getSelectedColumn());
                            if (valor.length() > 6) {
                                String str2 = valor.substring(valor.length() - 6, valor.length());
                                if (str2.equals(":00 HS")) {
                                    txtTurno.setText("");
                                    txtSaldo.setText("");
                                    txtTelefono.setText("");
                                    idturno = 0;
                                    txtDebe.setText("");
                                    cbFijo.setEnabled(false);
                                    txtTurno.setEditable(false);
                                    txtSaldo.setEditable(false);
                                    txtTelefono.setEditable(false);
                                    btnJugando.setEnabled(false);
                                    btnFalto.setEnabled(false);
                                    btnMedia.setEnabled(false);
                                    btnPago.setEnabled(false);
                                    btnTerminado.setEnabled(false);
                                    btnGuardarturno.setEnabled(false);
                                    btnMover.setEnabled(false);
                                    btnBorrarturno.setEnabled(false);
                                    btnMenosSeña.setEnabled(false);
                                    btnMasSeña.setEnabled(false);
                                    btnCopiar.setEnabled(false);
                                    btnEditar.setVisible(false);
                                    cmbTipo.setEnabled(false);
                                    txtComentario.setEnabled(false);
                                }
                                if (str2.equals("[TORN]") || str2.equals("[ACTI]")) {
                                    txtSaldo.setText("0");
                                    txtDebe.setText("");
                                    cbFijo.setEnabled(false);
                                    txtTurno.setEditable(false);
                                    txtSaldo.setEditable(false);
                                    txtTelefono.setEditable(false);
                                    btnJugando.setEnabled(false);
                                    btnFalto.setEnabled(false);
                                    btnMedia.setEnabled(false);
                                    btnPago.setEnabled(false);
                                    btnTerminado.setEnabled(false);
                                    btnGuardarturno.setEnabled(false);
                                    btnMover.setEnabled(true);
                                    btnBorrarturno.setEnabled(true);
                                    btnMenosSeña.setEnabled(false);
                                    btnMasSeña.setEnabled(false);
                                    cmbTipo.setEnabled(false);

                                }
                            }
                            v.visualizar_deuda(idturno, txtDebe);
                        } else {
                            cbFijo.setEnabled(false);
                            txtTurno.setEditable(false);
                            txtSaldo.setEditable(false);
                            txtTelefono.setEditable(false);
                            btnJugando.setEnabled(false);
                            btnFalto.setEnabled(false);
                            btnMedia.setEnabled(false);
                            btnPago.setEnabled(false);
                            btnTerminado.setEnabled(false);
                            btnGuardarturno.setEnabled(false);
                            btnMover.setEnabled(false);
                            btnBorrarturno.setEnabled(false);
                            btnMenosSeña.setEnabled(false);
                            btnMasSeña.setEnabled(false);
                            cmbTipo.setEnabled(false);
                        }
                    }
                

            if (evt.getClickCount() == 2) {
                if(!txtTurno.getText().equals("")){
                    VentaTurno miTurno2 = new VentaTurno(txtDebe, txtKiosco, idturno, now);
                    dskPane.add(miTurno2);
                    miTurno2.show();
                }
            }
                
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

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnJugando = new javax.swing.JButton();
        btnFalto = new javax.swing.JButton();
        btnPago = new javax.swing.JButton();
        btnMedia = new javax.swing.JButton();
        btnTerminado = new javax.swing.JButton();
        txtComentario = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        txtTelefono = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbFijo = new javax.swing.JCheckBox();
        txtSaldo = new javax.swing.JTextField();
        txtTurno = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnMenosSeña = new javax.swing.JButton();
        btnMasSeña = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtDebe = new javax.swing.JTextField();
        cmbTipo = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        btnGuardarturno = new javax.swing.JButton();
        btnBorrarturno = new javax.swing.JButton();
        btnMover = new javax.swing.JButton();
        btnCopiar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnListo = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        Fecha = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtTurnos = new javax.swing.JTextField();
        txtKiosco = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtTotalTurnosDia = new javax.swing.JTextField();
        dskPane = new javax.swing.JDesktopPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        btnArchivo = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Futbol 5");
        setIconImage(getIconImage());
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(153, 255, 255));
        jPanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jButton1.setText("Venta");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        btnJugando.setText("Jugando");
        btnJugando.setEnabled(false);
        btnJugando.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJugandoActionPerformed(evt);
            }
        });
        btnJugando.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        btnFalto.setText("Falto");
        btnFalto.setEnabled(false);
        btnFalto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFaltoActionPerformed(evt);
            }
        });
        btnFalto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        btnPago.setText("Pago");
        btnPago.setEnabled(false);
        btnPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagoActionPerformed(evt);
            }
        });
        btnPago.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        btnMedia.setText("Media Cancha");
        btnMedia.setEnabled(false);
        btnMedia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMediaActionPerformed(evt);
            }
        });
        btnMedia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnMediaformKeyPressed(evt);
            }
        });

        btnTerminado.setText("Terminado");
        btnTerminado.setEnabled(false);
        btnTerminado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTerminadoActionPerformed(evt);
            }
        });

        txtComentario.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtComentario)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnJugando, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTerminado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFalto, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnMedia, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(txtComentario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnJugando)
                    .addComponent(btnFalto)
                    .addComponent(btnTerminado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMedia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(0, 204, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        txtTelefono.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Telefono:");
        jLabel4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        cbFijo.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        cbFijo.setText("Turno Fijo");
        cbFijo.setEnabled(false);
        cbFijo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        txtSaldo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtSaldo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaldoActionPerformed(evt);
            }
        });
        txtSaldo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        txtTurno.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTurno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Turno:");
        jLabel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Saldo:");
        jLabel2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        btnMenosSeña.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnMenosSeña.setText("-");
        btnMenosSeña.setEnabled(false);
        btnMenosSeña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenosSeñaActionPerformed(evt);
            }
        });
        btnMenosSeña.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        btnMasSeña.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnMasSeña.setText("+");
        btnMasSeña.setEnabled(false);
        btnMasSeña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMasSeñaActionPerformed(evt);
            }
        });
        btnMasSeña.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Debe:");
        jLabel3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        txtDebe.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtDebe.setFocusable(false);
        txtDebe.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        cmbTipo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        cmbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Comun", "Torneo", "Actividad" }));

        jPanel4.setBackground(new java.awt.Color(0, 204, 204));
        jPanel4.setLayout(null);

        btnGuardarturno.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnGuardarturno.setText("Guardar");
        btnGuardarturno.setEnabled(false);
        btnGuardarturno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarturnoActionPerformed(evt);
            }
        });
        btnGuardarturno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        jPanel4.add(btnGuardarturno);
        btnGuardarturno.setBounds(95, 31, 89, 37);

        btnBorrarturno.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnBorrarturno.setText("Borrar");
        btnBorrarturno.setEnabled(false);
        btnBorrarturno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarturnoActionPerformed(evt);
            }
        });
        btnBorrarturno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        jPanel4.add(btnBorrarturno);
        btnBorrarturno.setBounds(95, 0, 89, 25);

        btnMover.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnMover.setText("Mover");
        btnMover.setEnabled(false);
        btnMover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoverActionPerformed(evt);
            }
        });
        jPanel4.add(btnMover);
        btnMover.setBounds(0, 0, 89, 25);

        btnCopiar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCopiar.setText("Copiar");
        btnCopiar.setEnabled(false);
        btnCopiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopiarActionPerformed(evt);
            }
        });
        btnCopiar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCopiarformKeyPressed(evt);
            }
        });
        jPanel4.add(btnCopiar);
        btnCopiar.setBounds(0, 30, 89, 37);

        btnEditar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        btnEditar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEditarformKeyPressed(evt);
            }
        });
        jPanel4.add(btnEditar);
        btnEditar.setBounds(95, 31, 90, 37);

        btnListo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnListo.setText("Listo");
        btnListo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListoActionPerformed(evt);
            }
        });
        btnListo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnListoformKeyPressed(evt);
            }
        });
        jPanel4.add(btnListo);
        btnListo.setBounds(0, 31, 89, 37);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTurno))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addComponent(btnMenosSeña, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnMasSeña, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 22, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(cbFijo, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDebe, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(30, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTurno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(txtSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtDebe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMenosSeña)
                    .addComponent(btnMasSeña)
                    .addComponent(cbFijo))
                .addContainerGap())
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel6.setText("Fecha");
        jLabel6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        Fecha.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Fecha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel7.setText("Turnos:");
        jLabel7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel8.setText("Buffet:");
        jLabel8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        txtTurnos.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtTurnos.setFocusable(false);
        txtTurnos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        txtKiosco.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtKiosco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Cantidad");

        txtTotalTurnosDia.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTurnos, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtKiosco))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalTurnosDia)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtTurnos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtKiosco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotalTurnosDia)
                            .addComponent(jLabel9))
                        .addGap(13, 13, 13)))
                .addContainerGap())
        );

        dskPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jScrollPane2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tabla.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tabla);

        dskPane.add(jScrollPane2);
        jScrollPane2.setBounds(0, 0, 1370, 590);

        jMenuBar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        btnArchivo.setText("Archivo");
        btnArchivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jMenuItem11.setText("Nuevo Ingreso");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        btnArchivo.add(jMenuItem11);

        jMenuItem10.setText("Nuevo Egreso");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        btnArchivo.add(jMenuItem10);

        jMenuItem1.setText("Resumen del Dia");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuItem1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        btnArchivo.add(jMenuItem1);

        jMenuItem6.setText("Salir");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        btnArchivo.add(jMenuItem6);

        jMenuBar1.add(btnArchivo);

        jMenu2.setText("Productos");
        jMenu2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jMenuItem3.setText("ABM de Productos");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem9.setText("Agregar Compra");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuItem7.setText("Ver Facturas");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem8.setText("Consulta De Stock");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Ventas");
        jMenu3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jMenuItem5.setText("Ventas Por Fecha");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuBar1.add(jMenu3);

        jMenu1.setText("Canchas");
        jMenu1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jMenuItem2.setText("Cantidad de canchas");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu4.setText("Turnos");
        jMenu4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jMenuItem12.setText("Listado De Turnos");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem12);

        jMenuItem4.setText("Precios De Turnos");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenuItem4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        jMenu4.add(jMenuItem4);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(dskPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dskPane, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        Productos miProducto = new Productos();
        dskPane.add(miProducto);
        miProducto.show();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        PreciosTurnos miTurno = new PreciosTurnos();
        dskPane.add(miTurno);
        miTurno.show();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        JTextArea txResumen = new JTextArea();
        Integer cantidad = 0;
        Double totalturnos = 0.00;
        Double totalsalidas = 0.00;
        Double totalingresos = 0.00;
        Double bebida = 0.00;
        Double kiosco = 0.00;
        
        JTextField txNombre = new JTextField(20);
        txNombre.addAncestorListener( new RequestFocusListener() );
        filtros.filtrotamaño(txNombre, 10);
        filtros.filtroeneteros(txNombre);
        
        JPanel myPanelPrincipal = new JPanel();
        myPanelPrincipal.setLayout(new BoxLayout(myPanelPrincipal, BoxLayout.Y_AXIS));
        myPanelPrincipal.add(new JLabel("Efectivo:"));
        myPanelPrincipal.add(txNombre);
        txNombre.requestFocusInWindow();
        
        int conf = JOptionPane.showConfirmDialog(null, myPanelPrincipal, "Efectivo total", JOptionPane.OK_CANCEL_OPTION);

        if(conf != 0) {
            return;
        }
        
        Double saldo = Double.parseDouble(txNombre.getText());
        
        ResultSet rs = null;
        rs = conn.visualizarTurnos();
        try {
            while(rs.next()){
                cantidad = cantidad + rs.getInt(2);
                totalturnos = totalturnos + rs.getDouble(3);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        txResumen.append("Turnos "+cantidad+": $"+totalturnos);
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        
        rs = conn.visualizarTurnos();
        try {
            while(rs.next()){
                txResumen.append("   $"+rs.getInt(1)+" "+rs.getInt(2)+": $"+rs.getInt(3));
                txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("Otros ingresos:"); // Esto para el salto de línea
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        
        rs = conn.visualizar_ingresos();
        try {
            while(rs.next()){
                txResumen.append("   "+rs.getString(1)+" $"+rs.getDouble(2));
                txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
                totalingresos = totalingresos + rs.getDouble(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        totalturnos = totalturnos + totalingresos;
        txResumen.append("Total: $"+totalturnos);
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("Salidas:");
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        
        rs = conn.visualizar_salidas();
        try {
            while(rs.next()){
                txResumen.append("   "+rs.getString(1)+" $"+rs.getDouble(2));
                txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
                totalsalidas = totalsalidas + rs.getDouble(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        totalturnos = totalturnos - totalsalidas;
        txResumen.append("Total a retirar: $"+totalturnos);
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea

        
        
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM totaldia");
            rs = ps.executeQuery();
            while(rs.next()){
                if(rs.getInt(1) == 0){
                    kiosco = kiosco + rs.getDouble(2);
                }else{
                    bebida = bebida + rs.getDouble(2);
                }
            }
        }catch(Exception ex){
            System.out.println("No hay registros");
        }
        
        
        
        Double totalbuffet = kiosco + bebida;   
        
        Double sobrante = saldo - totalbuffet - totalturnos;
        
        totalbuffet = totalbuffet + sobrante;
        
        txResumen.append("Buffet: $"+totalbuffet);
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("   Kiosco: $"+kiosco);
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("   Bebida: $"+bebida);
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("   Sobrante: $"+sobrante);        
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("Efectivo: $"+saldo);
        
        Integer faltas = 0;
        Integer torneo = 0;
        Integer actividades = 0;
        
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reserva WHERE fecha = '"+fecha+"' AND borrado = 0");
            rs = ps.executeQuery();
            while(rs.next()){
                if(rs.getInt(5) == 2){
                    faltas = faltas + 1;
                }else if(rs.getInt(5) == 8){
                    torneo = torneo + 1;
                }else if(rs.getInt(5) == 9){
                    actividades = actividades + 1;
                }
            }
        }catch(Exception ex){
            System.out.println("No hay registros");
        }
        
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("Faltaron "+faltas+" turnos");
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("Torneo "+torneo+" turnos");
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("Actividades "+actividades+" turnos");
        
        JPanel myTelefono = new JPanel();
        myTelefono.add(txResumen);
        
        JOptionPane.showMessageDialog(null, myTelefono, "Resumen", JOptionPane.DEFAULT_OPTION);

        
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F1){
            Venta miVentas = new Venta(txtKiosco, now);
            dskPane.add(miVentas);
            miVentas.show();
        }
    }//GEN-LAST:event_formKeyPressed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        VentasFechas miVentas = new VentasFechas();
        dskPane.add(miVentas);
        miVentas.show();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        String[] options = {"Sin Factura", "Con Factura"};
        int seleccion = JOptionPane.showOptionDialog(null, "Elija La Opcion Deseada", "Tipo De Carga", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (seleccion == 0) {
            Stock miStock = new Stock();
            dskPane.add(miStock);
            miStock.show();
        } else {
            JTextField txDescripcion = new JTextField(20);
            txDescripcion.addAncestorListener(new RequestFocusListener());
            filtros.filtrotamaño(txDescripcion, 29);


            JPanel mySalida = new JPanel();
            mySalida.setLayout(new BoxLayout(mySalida, BoxLayout.Y_AXIS));
            mySalida.add(new JLabel("Numero de factura:"));
            mySalida.add(txDescripcion);

            int confirm = JOptionPane.showConfirmDialog(null, mySalida, "Factura", JOptionPane.OK_CANCEL_OPTION);

            if (confirm != 0) {
                return;
            }
            if (txDescripcion.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe indicar el numero de factura", "", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (txDescripcion.getText().length() > 29) {
                JOptionPane.showMessageDialog(null, "El numero no puede contener mas de 30 caracteres", "", JOptionPane.WARNING_MESSAGE);
                return;
            }
         
            ResultSet rs = null;
            Integer existe = 0;
            String nombre = txDescripcion.getText();

            try {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM facturas WHERE numero = '" + nombre + "'");
                rs = ps.executeQuery();

                while (rs.next()) {
                    existe = 1;
                }
                if (existe == 1) {
                    JOptionPane.showMessageDialog(null, "El numero de factura ya se encuentra en la base de datos", "", JOptionPane.WARNING_MESSAGE);
                    return;
                }

            } catch (Exception ex) {
                System.out.println("Error de consulta");
            }


            
            
            ConFactura miFactura = new ConFactura(txDescripcion.getText());
            dskPane.add(miFactura);
            miFactura.show();
            
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        JTextField txDescripcion = new JTextField(20);
        txDescripcion.addAncestorListener( new RequestFocusListener() );
        JTextField txGasto = new JTextField(20);
        filtros.filtrotamaño(txDescripcion, 29);
        filtros.filtrotamañodecimal(txGasto, 9);
        filtros.filtrodecimales(txGasto);
        
        
        JPanel mySalida = new JPanel();
        mySalida.setLayout(new BoxLayout(mySalida, BoxLayout.Y_AXIS));
        mySalida.add(new JLabel("Descripcion:"));
        mySalida.add(txDescripcion);
        mySalida.add(new JLabel("Importe:"));
        mySalida.add(txGasto);
        
        int confirm = JOptionPane.showConfirmDialog(null, mySalida, "Salida", JOptionPane.OK_CANCEL_OPTION);

        if(confirm != 0) {
            return;
        }
        
        if(txDescripcion.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Debe escribir una descripcion", "", JOptionPane.WARNING_MESSAGE);
            return;
        }else if(txDescripcion.getText().length() > 29){
            JOptionPane.showMessageDialog(null, "La descripcion no puede contener mas de 30 caracteres", "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if(txGasto.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Debe indicar el importe", "", JOptionPane.WARNING_MESSAGE);
            return;
        }else if(txGasto.getText().length() > 9){
            JOptionPane.showMessageDialog(null, "El importe no debe contener mas de 10 caracteres", "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try{
            conn.guardar_salida(txDescripcion.getText(), Double.parseDouble(txGasto.getText()));
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Hubo un error al guardar el egreso", "", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        JTextField txDescripcion = new JTextField(20);
        txDescripcion.addAncestorListener( new RequestFocusListener() );
        JTextField txGasto = new JTextField(20);
        filtros.filtrotamaño(txDescripcion, 29);
        filtros.filtrotamañodecimal(txGasto, 9);
        filtros.filtrodecimales(txGasto);
        filtros.evitarPegar(txDescripcion);
        filtros.evitarPegar(txGasto);
        
        
        JPanel mySalida = new JPanel();
        mySalida.setLayout(new BoxLayout(mySalida, BoxLayout.Y_AXIS));
        mySalida.add(new JLabel("Descripcion:"));
        mySalida.add(txDescripcion);
        mySalida.add(new JLabel("Total:"));
        mySalida.add(txGasto);
        
        int confirm = JOptionPane.showConfirmDialog(null, mySalida, "Ingreso", JOptionPane.OK_CANCEL_OPTION);

        if(confirm != 0) {
            return;
        }
        
        if(txDescripcion.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Debe escribir una descripcion", "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(txGasto.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Debe indicar el importe", "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try{
            conn.guardar_ingreso(txDescripcion.getText(), Double.parseDouble(txGasto.getText()));
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Hubo un error al guardar el ingreso", "", JOptionPane.ERROR_MESSAGE);
        }
         

    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JTextField txCanti = new JTextField(20);
        txCanti.addAncestorListener( new RequestFocusListener() );
        filtros.filtrotamaño(txCanti, 3);
        filtros.filtroeneteros(txCanti);
        filtros.evitarPegar(txCanti);
        
        JPanel mySalida = new JPanel();
        mySalida.setLayout(new BoxLayout(mySalida, BoxLayout.Y_AXIS));
        mySalida.add(new JLabel("Cantidad de canchas:"));
        mySalida.add(txCanti);

        
        int confirm = JOptionPane.showConfirmDialog(null, mySalida, "Canchas", JOptionPane.OK_CANCEL_OPTION);

        if(confirm != 0) {
            return;
        }
        conn.canticanchas(Integer.parseInt(txCanti.getText()));
        v.visualizar_tabla(tabla, fecha, fecha2, txtTotalTurnosDia);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        String[] option = {"Sí", "No"};
        int selec = JOptionPane.showOptionDialog(null, "Esta Seguro Que Desea Salir. Esto Reiniciara Los Valores Del Dia", "Cerrar Programa", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
        if (selec == 1) {
            return;
        }

        conn.vaciar("ventaturnos");
        conn.vaciar("fecha");
        conn.vaciar("salidas");
        conn.vaciar("ingresos");
        conn.vaciar("totaldia");
        dispose();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        Facturas miFacturas = new Facturas(dskPane);
        dskPane.add(miFacturas);
        miFacturas.show();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void btnMoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoverActionPerformed
        Boolean esfijo = cbFijo.isSelected();
        Mover miMovida = new Mover(idturno, esfijo, tabla, Fecha.getDate(), condition, cancha, hora, idreserva);
        dskPane.add(miMovida);
        miMovida.show();
    }//GEN-LAST:event_btnMoverActionPerformed

    private void btnMasSeñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMasSeñaActionPerformed
        JTextField txNombre = new JTextField(20);
        txNombre.addAncestorListener( new RequestFocusListener() );
        filtros.filtrotamaño(txNombre, 5);
        filtros.filtroeneteros(txNombre);

        JPanel myPanelPrincipal = new JPanel();
        myPanelPrincipal.setLayout(new BoxLayout(myPanelPrincipal, BoxLayout.Y_AXIS));
        myPanelPrincipal.add(new JLabel("Ingrese el importe:"));
        myPanelPrincipal.add(txNombre);
        txNombre.requestFocusInWindow();
        Integer saldo = 0;

        int conf = JOptionPane.showConfirmDialog(null, myPanelPrincipal, "Saldo A Sumar", JOptionPane.OK_CANCEL_OPTION);

        if(conf != 0) {
            return;
        }

        if(!txNombre.getText().equals("")){
            saldo = Integer.parseInt(txNombre.getText());
        }

        conn.guardarSaldo(saldo, idturno);
        conn.guardar_ingreso("Seña de " + txtTurno.getText(), 1.0 * saldo);
        txtTurno.setText("");
        txtSaldo.setText("0");
        txtTelefono.setText("");
        idturno = 0;

        btnJugando.setEnabled(false);
        btnFalto.setEnabled(false);
        btnPago.setEnabled(false);
        btnGuardarturno.setEnabled(false);
        btnBorrarturno.setEnabled(false);
        txtTurno.setEditable(false);
        txtSaldo.setEditable(false);
        txtTelefono.setEditable(false);
    }//GEN-LAST:event_btnMasSeñaActionPerformed

    private void btnMenosSeñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenosSeñaActionPerformed
        JTextField txNombre = new JTextField(20);
        txNombre.addAncestorListener( new RequestFocusListener() );
        filtros.filtrotamaño(txNombre, 5);
        filtros.filtroeneteros(txNombre);

        JPanel myPanelPrincipal = new JPanel();
        myPanelPrincipal.setLayout(new BoxLayout(myPanelPrincipal, BoxLayout.Y_AXIS));
        myPanelPrincipal.add(new JLabel("Ingrese el importe:"));
        myPanelPrincipal.add(txNombre);
        txNombre.requestFocusInWindow();
        Integer saldo = 0;

        int conf = JOptionPane.showConfirmDialog(null, myPanelPrincipal, "Saldo A Restar", JOptionPane.OK_CANCEL_OPTION);

        if(conf != 0) {
            return;
        }

        if(!txNombre.getText().equals("")){
            saldo = Integer.parseInt(txNombre.getText());
        }

        conn.guardarSaldo(-saldo, idturno);
        conn.guardar_salida("Seña de " + txtTurno.getText(), 1.0 * saldo);
        txtTurno.setText("");
        txtSaldo.setText("0");
        txtTelefono.setText("");
        idturno = 0;

        btnJugando.setEnabled(false);
        btnFalto.setEnabled(false);
        btnPago.setEnabled(false);
        btnGuardarturno.setEnabled(false);
        btnBorrarturno.setEnabled(false);
        txtTurno.setEditable(false);
        txtSaldo.setEditable(false);
        txtTelefono.setEditable(false);
    }//GEN-LAST:event_btnMenosSeñaActionPerformed

    private void txtSaldoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaldoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaldoActionPerformed

    private void btnGuardarturnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarturnoActionPerformed
        if(editando){
            if (txtTurno.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Es necesario escribir el nombre", "", JOptionPane.WARNING_MESSAGE);
                txtTurno.requestFocusInWindow();
                return;
            } else if (txtTurno.getText().length() > 19) {
                JOptionPane.showMessageDialog(null, "El nombre no puede contener mas de 20 caracteres", "", JOptionPane.WARNING_MESSAGE);
                txtTurno.setText("");
                txtTurno.requestFocusInWindow();
                return;
            }
            if (txtTelefono.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe escribir el telefono", "", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (txtTelefono.getText().length() > 19) {
                JOptionPane.showMessageDialog(null, "El telefono no puede contener mas de 20 caracteres", "", JOptionPane.WARNING_MESSAGE);
                txtTelefono.setText("");
                txtTelefono.requestFocusInWindow();
                return;
            }
            
            conn.editar_datos(idturno, txtTurno.getText(), txtTelefono.getText());
            JOptionPane.showMessageDialog(null, "Turno Editado Correctamente", "", JOptionPane.INFORMATION_MESSAGE);

            v.visualizar_tabla(tabla, fecha, fecha2, txtTotalTurnosDia);
            txtTurno.setText("");
            txtSaldo.setText("0");
            txtTelefono.setText("");
            idturno = 0;

            btnJugando.setEnabled(false);
            btnFalto.setEnabled(false);
            btnPago.setEnabled(false);
            btnGuardarturno.setEnabled(false);
            btnBorrarturno.setEnabled(false);
            txtTurno.setEditable(false);
            txtSaldo.setEditable(false);
            txtTelefono.setEditable(false);
            
            
        }else{
            if (txtTurno.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Es necesario escribir el nombre", "", JOptionPane.WARNING_MESSAGE);
                txtTurno.requestFocusInWindow();
                return;
            } else if (txtTurno.getText().length() > 19) {
                JOptionPane.showMessageDialog(null, "El nombre no puede contener mas de 20 caracteres", "", JOptionPane.WARNING_MESSAGE);
                txtTurno.setText("");
                txtTurno.requestFocusInWindow();
                return;
            }
            if (txtSaldo.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe indicar el saldo", "", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (txtSaldo.getText().length() > 19) {
                JOptionPane.showMessageDialog(null, "El saldo no puede contener mas de 20 caracteres", "", JOptionPane.WARNING_MESSAGE);
                txtSaldo.setText("0");
                txtSaldo.requestFocusInWindow();
                return;
            }
            if (txtTelefono.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Debe escribir el telefono", "", JOptionPane.WARNING_MESSAGE);
                return;
            } else if (txtTelefono.getText().length() > 19) {
                JOptionPane.showMessageDialog(null, "El telefono no puede contener mas de 20 caracteres", "", JOptionPane.WARNING_MESSAGE);
                txtTelefono.setText("");
                txtTelefono.requestFocusInWindow();
                return;
            }

            String turno = txtTurno.getText();
            Integer saldo = Integer.parseInt(txtSaldo.getText());
            Integer condicion = 0;

            if (cmbTipo.getSelectedIndex() > 0) {
                condicion = cmbTipo.getSelectedIndex() + 7;
            }

            Integer fijo = 0;
            if (!copiando) {
                if (cbFijo.isSelected()) {
                    fijo = 1;
                    ResultSet rs = conn.visualizar_turno(hora, cancha, fecha2);
                    try {
                        if (rs.next()) {
                            JOptionPane.showMessageDialog(null, "Ya hay un turno fijo en este campo", "Error al guardar", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                String telefono = txtTelefono.getText();

                if (saldo > 0) {
                    conn.guardar_ingreso("Seña", 1.0 * saldo);
                }

                if (cbFijo.isSelected()) {
                    conn.guardarturno(cancha, turno, hora, saldo, condicion, telefono, fecha2, fijo);
                } else {
                    conn.guardarturno(cancha, turno, hora, saldo, condicion, telefono, fecha, fijo);
                }

                JOptionPane.showMessageDialog(null, "Turno Guardado Correctamente", "", JOptionPane.INFORMATION_MESSAGE);

                txtTurno.setText("");
                txtSaldo.setText("0");
                txtTelefono.setText("");

            } else {
                if (cbFijo.isSelected()) {
                    String[] options = {"Solo Por Esta Fecha", "Copiar Como Turno Fijo"};
                    int seleccion = JOptionPane.showOptionDialog(null, "Desea Copiar El Fijo o Solo Por Esta Fecha", "El Turno Es Fijo", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (seleccion == 1) {
                        ResultSet rs = conn.visualizar_turno(hora, cancha, fecha2);
                        try {
                            if (rs.next()) {
                                JOptionPane.showMessageDialog(null, "Ya hay un turno fijo en este campo", "Error Al Copiar", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        conn.reservar(idcopia, cancha, hora, condicion, fecha2, 1);
                    } else {
                        conn.reservar(idcopia, cancha, hora, condicion, fecha, 0);
                    }
                }else{
                    conn.reservar(idcopia, cancha, hora, condicion, fecha, 0);
                }

                    JOptionPane.showMessageDialog(null, "Turno Copiado Correctamente", "", JOptionPane.INFORMATION_MESSAGE);
                }

            idturno = 0;
            v.visualizar_tabla(tabla, fecha, fecha2, txtTotalTurnosDia);

            btnJugando.setEnabled(false);
            btnFalto.setEnabled(false);
            btnPago.setEnabled(false);
            btnGuardarturno.setEnabled(false);
            btnBorrarturno.setEnabled(false);
            txtTurno.setEditable(false);
            txtSaldo.setEditable(false);
            txtTelefono.setEditable(false);
        }
        tabla.setEnabled(true);
        editando = false;
    }//GEN-LAST:event_btnGuardarturnoActionPerformed

    private void btnBorrarturnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarturnoActionPerformed
        if(cbFijo.isSelected()){
            String[] options = {"Borrar solo por esta fecha", "Borrar definitivo"};
            int seleccion = JOptionPane.showOptionDialog(null, "Seleccione la opcion deseada", "Baja de turno fijo", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if(seleccion == 0){
                conn.canceladofijo(idreserva);
            }else if(seleccion == 1){
                conn.borrar_fijo(idturno, fecha2, cancha, hora);
                conn.borrar_reserva(idreserva);
                String borrar = "DELETE FROM reserva WHERE id_turno = " + idturno + " AND fecha > '" + fecha + "' AND horario = " + hora + " AND cancha = " + cancha;
                PreparedStatement ps = null;
                try {
                    ps = con.prepareStatement(borrar);
                    ps.executeUpdate();

                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }

        }else{
            String[] option = {"Sí", "No"};
            int selec = JOptionPane.showOptionDialog(null, "Esta seguro que desea borrar el turno", "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
            if (selec == 1) {
                return;
            }

            conn.borrar_reserva(idreserva);
            txtTurno.setText("");
            idturno = 0;
            txtSaldo.setText("0");
            txtTelefono.setText("");

            btnJugando.setEnabled(false);
            btnFalto.setEnabled(false);
            btnPago.setEnabled(false);
            btnGuardarturno.setEnabled(false);
            btnBorrarturno.setEnabled(false);
            txtTurno.setEditable(false);
            txtSaldo.setEditable(false);
            txtTelefono.setEditable(false);
        }
        v.visualizar_tabla(tabla, fecha, fecha2, txtTotalTurnosDia);
    }//GEN-LAST:event_btnBorrarturnoActionPerformed

    private void btnTerminadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTerminadoActionPerformed
        Integer condicion = 6;
        
        conn.editar_turno(condicion, idreserva, 0);

        txtTurno.setText("");
        txtSaldo.setText("0");
        txtTelefono.setText("");
        idturno = 0;
        v.visualizar_tabla(tabla, fecha, fecha2, txtTotalTurnosDia);

        btnTerminado.setEnabled(false);
        btnJugando.setEnabled(false);
        btnFalto.setEnabled(false);
        btnPago.setEnabled(false);
        btnGuardarturno.setEnabled(false);
        btnBorrarturno.setEnabled(false);
        txtTurno.setEditable(false);
        btnMover.setEnabled(false);
        txtSaldo.setEditable(false);
        txtTelefono.setEditable(false);
    }//GEN-LAST:event_btnTerminadoActionPerformed

    private void btnMediaformKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnMediaformKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMediaformKeyPressed

    private void btnMediaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMediaActionPerformed
        Integer condicion = 5;
        
        JComboBox cmbPrecio = new JComboBox();
        
        ResultSet rs = null;
        
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM turnos INNER JOIN reserva ON turnos.id_turno = reserva.id_turno WHERE reserva.fecha = '"+fecha+"' AND turnos.id_turno = "+idturno+" AND turnos.comentario != ''");
            rs = ps.executeQuery();
            while(rs.next()){
                JOptionPane.showMessageDialog(null, ""+rs.getString(5), "", JOptionPane.WARNING_MESSAGE);
            }
        }catch(Exception ex){
            System.out.println(ex);
        }
        
        
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM precios GROUP BY precio ORDER BY precio ASC");
            rs = ps.executeQuery();
            while(rs.next()){
                cmbPrecio.addItem(rs.getString(1));
            }
        }catch(Exception ex){
            System.out.println("No hay registros");
        }

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reserva WHERE id_reserva = " + idreserva + " AND condicion = 5");
            rs = ps.executeQuery();
            Integer n = 0;
            String pago = "";
            while (rs.next()) {
                n = 1;
                pago = rs.getString(9);
            }
            if (n == 1) {
                cmbPrecio.removeAllItems();
                cmbPrecio.addItem(pago);
            }

        } catch (Exception ex) {
            System.out.println("Error de consulta");
        }

        
        
        
        JPanel myPanelPrincipal = new JPanel();
        myPanelPrincipal.setLayout(new BoxLayout(myPanelPrincipal, BoxLayout.Y_AXIS));
        myPanelPrincipal.add(new JLabel("Precio:"));
        myPanelPrincipal.add(cmbPrecio);
        
        int conf = JOptionPane.showConfirmDialog(null, myPanelPrincipal, "Precio del Turno", JOptionPane.OK_CANCEL_OPTION);

        if(conf != 0) {
            return;
        }
        String precio1 = (String) cmbPrecio.getSelectedItem();
        Integer precio = Integer.parseInt(precio1)/2;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reserva WHERE id_reserva = " + idreserva + " AND condicion = 5");
            rs = ps.executeQuery();
            Integer n = 0;
            while (rs.next()) {
                n = 1;
            }
            if (n == 0) {
                conn.editar_turno(condicion, idreserva, precio*2);
                conn.guardarventaturno(precio);
            } else {
                conn.editar_turno(3, idreserva, precio*2);

                String update = "UPDATE ventaturnos SET cantidad = cantidad - 1, total = total - ? WHERE precio = " + precio;
                ps = null;
                try {

                    ps = con.prepareStatement(update);
                    ps.setInt(1, precio);

                    ps.executeUpdate();
                } catch (Exception ex) {
                    System.out.println("Error de consulta");
                }

                conn.guardarventaturno(precio * 2);
                if (!cbFijo.isSelected()) {
                    String[] options = {"No", "Sí"};
                    int seleccion = JOptionPane.showOptionDialog(null, "Desea reservar nuevamente", "Reserva de turno", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (seleccion == 1) {
                        Seleccion miSeleccion = new Seleccion(idturno);
                        dskPane.add(miSeleccion);
                        miSeleccion.show();
                    }
                }

            }

        } catch (Exception ex) {
            System.out.println("Error de consulta");
        }

        rs = null;
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ventaturnos");
            rs = ps.executeQuery();
            Integer total = 0;
            while(rs.next()){
                total = total + rs.getInt(3);
            }
            txtTurnos.setText("$"+total);
        }catch(Exception ex){
            System.out.println("No hay registros");
        }

        txtTurno.setText("");
        txtSaldo.setText("0");
        txtTelefono.setText("");
        idturno = 0;

        v.visualizar_tabla(tabla, fecha, fecha2, txtTotalTurnosDia);
        btnJugando.setEnabled(false);
        btnFalto.setEnabled(false);
        btnMedia.setEnabled(false);
        btnPago.setEnabled(false);
        btnGuardarturno.setEnabled(false);
        btnBorrarturno.setEnabled(false);
        txtTurno.setEditable(false);
        btnMover.setEnabled(false);
        txtSaldo.setEditable(false);
        txtTelefono.setEditable(false);
    }//GEN-LAST:event_btnMediaActionPerformed

    private void btnPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagoActionPerformed
        Integer condicion = 3;
        
        JComboBox cmbPrecio = new JComboBox();
        
        ResultSet rs = null;
        
        
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM turnos INNER JOIN reserva ON turnos.id_turno = reserva.id_turno WHERE reserva.fecha = '"+fecha+"' AND turnos.id_turno = "+idturno+" AND turnos.comentario != ''");
            rs = ps.executeQuery();
            while(rs.next()){
                JOptionPane.showMessageDialog(null, ""+rs.getString(5), "", JOptionPane.WARNING_MESSAGE);
            }
        }catch(Exception ex){
            System.out.println(ex);
        }
        
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM precios GROUP BY precio ORDER BY precio ASC");
            rs = ps.executeQuery();
            while(rs.next()){
                cmbPrecio.addItem(rs.getString(1));
            }
        }catch(Exception ex){
            System.out.println("No hay registros");
        }
        
        JPanel myPanelPrincipal = new JPanel();
        myPanelPrincipal.setLayout(new BoxLayout(myPanelPrincipal, BoxLayout.Y_AXIS));
        myPanelPrincipal.add(new JLabel("Precio:"));
        myPanelPrincipal.add(cmbPrecio);
        
        int conf = JOptionPane.showConfirmDialog(null, myPanelPrincipal, "Precio del Turno", JOptionPane.OK_CANCEL_OPTION);

        if(conf != 0) {
            return;
        }
        
        String precio1 = (String) cmbPrecio.getSelectedItem();
        Integer precio = Integer.parseInt(precio1);

        conn.editar_turno(condicion, idreserva, precio);
        
        if(!cbFijo.isSelected()){
            String[] options = {"No", "Sí"};
            int seleccion = JOptionPane.showOptionDialog(null, "Desea Reservar Nuevamente", "Reserva de Turno", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if(seleccion == 1){
                Seleccion miSeleccion = new Seleccion(idturno);
                dskPane.add(miSeleccion);
                    miSeleccion.show();
                }
            }

            conn.guardarventaturno(precio);

            try {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM ventaturnos");
                rs = ps.executeQuery();
                Integer total = 0;
                while (rs.next()) {
                    total = total + rs.getInt(3);
                }
                txtTurnos.setText("$" + total);
            } catch (Exception ex) {
                System.out.println("No hay registros");
            }

            txtTurno.setText("");
            txtSaldo.setText("0");
            txtTelefono.setText("");
            idturno = 0;

            v.visualizar_tabla(tabla, fecha, fecha2, txtTotalTurnosDia);
            btnJugando.setEnabled(false);
            btnFalto.setEnabled(false);
            btnMedia.setEnabled(false);
            btnPago.setEnabled(false);
            btnGuardarturno.setEnabled(false);
            btnBorrarturno.setEnabled(false);
            txtTurno.setEditable(false);
            btnMover.setEnabled(false);
            txtSaldo.setEditable(false);
            txtTelefono.setEditable(false);
        

    }//GEN-LAST:event_btnPagoActionPerformed

    private void btnFaltoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFaltoActionPerformed
        Integer condicion = 2;
            
        conn.editar_turno(condicion, idreserva, 0);

        txtTurno.setText("");
        txtSaldo.setText("0");
        txtTelefono.setText("");
        idturno = 0;
        v.visualizar_tabla(tabla, fecha, fecha2, txtTotalTurnosDia);
        btnJugando.setEnabled(false);
        btnFalto.setEnabled(false);
        btnMedia.setEnabled(false);
        btnPago.setEnabled(false);
        btnGuardarturno.setEnabled(false);
        btnBorrarturno.setEnabled(false);
        txtTurno.setEditable(false);
        btnMover.setEnabled(false);
        txtSaldo.setEditable(false);
        txtTelefono.setEditable(false);

    }//GEN-LAST:event_btnFaltoActionPerformed

    private void btnJugandoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJugandoActionPerformed
        Integer condicion = 1;

        conn.editar_turno(condicion, idreserva, 0);

        txtTurno.setText("");
        txtSaldo.setText("0");
        txtTelefono.setText("");
        idturno = 0;
        v.visualizar_tabla(tabla, fecha, fecha2, txtTotalTurnosDia);

        btnJugando.setEnabled(false);
        btnFalto.setEnabled(false);
        btnPago.setEnabled(false);
        btnGuardarturno.setEnabled(false);
        btnMover.setEnabled(false);
        btnBorrarturno.setEnabled(false);
        txtTurno.setEditable(false);
        txtSaldo.setEditable(false);
        txtTelefono.setEditable(false);

    }//GEN-LAST:event_btnJugandoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Venta miVentas = new Venta(txtKiosco, now);
        dskPane.add(miVentas);
        miVentas.show();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnCopiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopiarActionPerformed
        idcopia = idturno;
        btnCopiar.setVisible(false);
        btnListo.setVisible(true);
        copiando = true;
        
        
    }//GEN-LAST:event_btnCopiarActionPerformed

    private void btnCopiarformKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCopiarformKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCopiarformKeyPressed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        editando = true;
        cbFijo.setEnabled(false);
        btnGuardarturno.setVisible(true);
        txtSaldo.setEditable(false);
        txtComentario.setEnabled(false);
        btnJugando.setEnabled(false);
        btnFalto.setEnabled(false);
        btnMedia.setEnabled(false);
        btnPago.setEnabled(false);
        btnTerminado.setEnabled(false);
        btnGuardarturno.setEnabled(false);
        btnMover.setEnabled(false);
        btnCopiar.setEnabled(false);
        btnBorrarturno.setEnabled(false);
        btnMasSeña.setEnabled(false);
        btnMenosSeña.setEnabled(false);
        btnEditar.setVisible(false);
        btnGuardarturno.setVisible(true);
        btnGuardarturno.setEnabled(true);
        txtTurno.setEditable(true);
        txtTurno.requestFocusInWindow();
        txtTelefono.setEditable(true);
        tabla.setEnabled(false);
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEditarformKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEditarformKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditarformKeyPressed

    private void btnListoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListoActionPerformed
        idcopia = 0;
        btnCopiar.setVisible(true);
        btnListo.setVisible(false);
        copiando = false;
        if(null == tabla.getValueAt(tabla.getSelectedRow(), tabla.getSelectedColumn())){
            txtTurno.setText("");
            txtSaldo.setText("0");
            txtTelefono.setText("");
        }
    }//GEN-LAST:event_btnListoActionPerformed

    private void btnListoformKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnListoformKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnListoformKeyPressed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        ListadoTurnos lista = new ListadoTurnos();
        dskPane.add(lista);
        lista.show();
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        JTable tableta = new JTable();
        
        v.consultastock(tableta);
        
        pdf.CrearPDF(tableta, "Stock Total: ");
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser Fecha;
    private javax.swing.JMenu btnArchivo;
    private javax.swing.JButton btnBorrarturno;
    private javax.swing.JButton btnCopiar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnFalto;
    private javax.swing.JButton btnGuardarturno;
    private javax.swing.JButton btnJugando;
    private javax.swing.JButton btnListo;
    private javax.swing.JButton btnMasSeña;
    private javax.swing.JButton btnMedia;
    private javax.swing.JButton btnMenosSeña;
    private javax.swing.JButton btnMover;
    private javax.swing.JButton btnPago;
    private javax.swing.JButton btnTerminado;
    private javax.swing.JCheckBox cbFijo;
    private javax.swing.JComboBox<String> cmbTipo;
    private javax.swing.JDesktopPane dskPane;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txtComentario;
    private javax.swing.JTextField txtDebe;
    private javax.swing.JTextField txtKiosco;
    private javax.swing.JTextField txtSaldo;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtTotalTurnosDia;
    private javax.swing.JTextField txtTurno;
    private javax.swing.JTextField txtTurnos;
    // End of variables declaration//GEN-END:variables
}
