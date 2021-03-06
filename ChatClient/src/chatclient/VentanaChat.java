/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Gateway
 */
public class VentanaChat extends javax.swing.JFrame implements Runnable{
    
    private String usuario;
    //constantes para definir el protocolo de comunicacionm
    private final String MSG_SEND = "MSG";
    private final String USER = "USER";
    
    private Socket chatClient;
    
    private OutputStream outputStream;
    private InputStream inputStream;
    
    private DataOutputStream salidaDatos;
    private DataInputStream entradaDatos;
    
    private Thread recibir;
    /**
     * Creates new form VentanaChat
     */
    public VentanaChat() {
        initComponents();
        
        
    }
    
    public VentanaChat(String usuario){
        initComponents();
        this.lblUsuario.setText(usuario);
        this.usuario = usuario;
        
        this.recibir = new Thread(this);
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
        txtMensajesRecibidos = new javax.swing.JTextArea();
        txtMensajeEnviar = new javax.swing.JTextField();
        btnEnviar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Java Chat");

        txtMensajesRecibidos.setEditable(false);
        txtMensajesRecibidos.setColumns(20);
        txtMensajesRecibidos.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtMensajesRecibidos.setRows(5);
        jScrollPane1.setViewportView(txtMensajesRecibidos);

        txtMensajeEnviar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        btnEnviar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Nick: ");

        lblUsuario.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblUsuario.setText("usuario");

        btnClose.setText("CERRAR");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEnviar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMensajeEnviar)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnClose)
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblUsuario))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtMensajeEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
       if (validarCampos()){
            enviarDatos(this.txtMensajeEnviar.getText());
       }else{
           message("Debe de colocar un texto");
       }
       
        limpiar(false);
       
    }//GEN-LAST:event_btnEnviarActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        enviarDatos("CLOSE:"+this.usuario);
        this.dispose();
        System.exit(1);
    }//GEN-LAST:event_btnCloseActionPerformed
    
    public void conexion(int puerto, String ip){
        try{
            this.chatClient = new Socket(ip,puerto); 
            enviarDatos();
            this.recibir.start();            
        }catch(IOException e){
            messagesInformation(e.toString(),JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void enviarDatos(String mensaje){
        //this.txtMensajesRecibidos.append(this.MSG_SEND+mensaje+"\n");
        try{
            this.outputStream = this.chatClient.getOutputStream();
            this.salidaDatos = new DataOutputStream(this.outputStream);
            
            this.salidaDatos.writeUTF(this.MSG_SEND+":"+mensaje);
            this.salidaDatos.flush();
        }catch(IOException e){
            messagesInformation(e.toString(),JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void enviarDatos(){        
        try{
            this.outputStream = this.chatClient.getOutputStream();
            this.salidaDatos = new DataOutputStream(this.outputStream);
            
            this.salidaDatos.writeUTF(this.USER+":"+this.usuario);
            this.salidaDatos.flush();
        }catch(IOException e){
            messagesInformation(e.toString(),JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void recibirDatos(){
        try{
            this.inputStream = this.chatClient.getInputStream();
            this.entradaDatos = new DataInputStream(this.inputStream);
            
            this.txtMensajesRecibidos.append(this.entradaDatos.readUTF()+"\n");
            

        }catch(IOException e){
            messagesInformation(e.toString(),JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void messagesInformation(String message,int typeMessage){
        JOptionPane.showMessageDialog(null, message,"Java Chat",typeMessage);
    }
    private void enviarMensaje(String argMensaje){
        this.txtMensajesRecibidos.append(this.MSG_SEND+argMensaje+"\n");
        
    }
    
    private boolean validarCampos(){
        if(this.txtMensajeEnviar.getText().equals("")){
            return false;
        }else{
            return true;
        }
    }
    
    private void limpiar(boolean v){
        if (v){
            txtMensajeEnviar.setText("");
            txtMensajesRecibidos.setText("");
        }else{
            txtMensajeEnviar.setText("");
        }
    }
    
    private void message(String argMensaje){
        JOptionPane.showMessageDialog(null, argMensaje,"Java Chat",JOptionPane.WARNING_MESSAGE);
    }
    
    @Override
    public void run() {
        while(true){
            recibirDatos();          
        }
    }
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
            java.util.logging.Logger.getLogger(VentanaChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaChat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JTextField txtMensajeEnviar;
    private javax.swing.JTextArea txtMensajesRecibidos;
    // End of variables declaration//GEN-END:variables
}
