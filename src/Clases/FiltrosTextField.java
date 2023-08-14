/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.awt.Event;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.InputMap;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 *
 * @author Franco
 */
public class FiltrosTextField {
    
    public void filtroeneteros(JTextField campo){
        campo.addKeyListener(new KeyAdapter(){
                public void keyTyped(KeyEvent e){
                    char caracter = e.getKeyChar();

                    // Verificar si la tecla pulsada no es un digito
                    if(((caracter < '0') ||  (caracter > '9')) && (caracter != '\b' /*corresponde a BACK_SPACE*/)){
                       e.consume();  // ignorar el evento de teclado
                    }

               }
            });
    }
    
    public void filtrodecimales(JTextField campo){
        campo.addKeyListener(new KeyAdapter(){
                public void keyTyped(KeyEvent e){
                    char caracter = e.getKeyChar();

                    // Verificar si la tecla pulsada no es un digito
                    if (((caracter < '0') || (caracter > '9')) 
                        && (caracter != KeyEvent.VK_BACK_SPACE)
                        && (caracter != '.' || campo.getText().contains(".")) ) {
                        e.consume();
                    }

               }
            });
    }
    
     public void filtrotamañodecimal(JTextField campo, Integer tamaño){
        campo.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e){
                if(campo.getText().contains(".")){
                    if(campo.getText().length() > tamaño){
                        e.consume();
                    }
                }else{
                    if(campo.getText().length() > tamaño-2){
                        e.consume();
                    }
                }


           }
        });
    }
    
    
    public void filtrotamaño(JTextField campo, Integer tamaño){
        campo.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e){
                if(campo.getText().length() > tamaño-1){
                    e.consume();
                }

           }
        });
    }
    
    public static void evitarPegar(JTextField campo) {

        InputMap map2 = campo.getInputMap(JTextField.WHEN_FOCUSED);
        map2.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK), "null");

    }
}
