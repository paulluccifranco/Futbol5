/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import Forms.Cargas;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JTable;

/**
 *
 * @author user
 */
public class GenerarPDF {
    
    public void CrearPDF(JTable tabla, String titulo){
    try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream("table.pdf"));
            doc.open();
            PdfPTable pdfTable = new PdfPTable(tabla.getColumnCount());
            //adding table headers
            doc.add(new Paragraph(titulo));
            doc.add(new Paragraph("    "));
            for (int i = 0; i < tabla.getColumnCount(); i++) {
                pdfTable.addCell(tabla.getColumnName(i));
            }
            //extracting data from the JTable and inserting it to PdfPTable
            for (int rows = 0; rows < tabla.getRowCount(); rows++) {
                for (int cols = 0; cols < tabla.getColumnCount(); cols++) {
                    pdfTable.addCell(tabla.getValueAt(rows, cols).toString());

                }
            }
            doc.add(pdfTable);
            doc.close();
            System.out.println("done");
        } catch (DocumentException ex) {
    
            } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Cargas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "table.pdf");
            System.out.println("Final");
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
}
    
}
