/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class MiRender extends DefaultTableCellRenderer{
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
        String str2 = "";
        String str3 = "";
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setFont(new Font("Serif", Font.BOLD, 14));
        if(isSelected){
            this.setBackground(Color.GRAY);
        }else{
            this.setBackground(Color.WHITE);
            this.setForeground(Color.BLACK);
        }
        if(value != null){
            String str1=(String) value;
            if(str1.length() > 6){
                str2=str1.substring(str1.length()-6, str1.length());
                if ("[PAGO]".equals(str2)){
                    this.setOpaque(true);
                    this.setBackground(Color.GREEN);
                    this.setForeground(Color.RED);
                    this.setFont(new Font("Serif", Font.BOLD, 14));
                }else if ("FALTO]".equals(str2)){
                   this.setBackground(Color.RED);
                   this.setForeground(Color.YELLOW);
                   this.setFont(new Font("Serif", Font.BOLD, 14));
                }else if ("GANDO]".equals(str2)){
                    this.setBackground(Color.YELLOW);
                    this.setForeground(Color.BLACK);
                    this.setFont(new Font("Serif", Font.BOLD, 14));
                }else if ("MEDIA]".equals(str2)){
                    this.setBackground(Color.ORANGE);
                    this.setForeground(Color.BLACK);
                    this.setFont(new Font("Serif", Font.BOLD, 14));
                }else if ("[TORN]".equals(str2)){
                    this.setBackground(Color.magenta);
                    this.setForeground(Color.BLACK);
                    this.setFont(new Font("Serif", Font.BOLD, 14));
                }else if ("[ACTI]".equals(str2)){
                    this.setBackground(Color.blue);
                    this.setForeground(Color.WHITE);
                    this.setFont(new Font("Serif", Font.BOLD, 14));
                }else if ("INADO]".equals(str2)){
                    this.setBackground(Color.PINK);
                    this.setForeground(Color.BLACK);
                    this.setFont(new Font("Serif", Font.BOLD, 14));
                }else{
                    this.setBackground(Color.WHITE);
                    this.setForeground(Color.BLACK);
                }
            }
           if (str1.length() > 3) {
               str3 = str1.substring(0, 3);
               if (str3.equals("[F]")) {
                   this.setFont(new Font("Serif", Font.BOLD, 18));
                   this.setForeground(Color.BLUE);
               }
           }
       }


          return this;
   }
}