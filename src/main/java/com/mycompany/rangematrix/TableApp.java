/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rangematrix;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 *
 * @author daniil_pozdeev
 */
public class TableApp extends JFrame {
    
    public TableApp() {
        super("Таблица");
        TableColumnHeader t = new TableColumnHeader(new Model());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.add(t);
        this.setSize(new Dimension(1000,600));
    }
    
}
