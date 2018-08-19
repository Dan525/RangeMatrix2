/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rangematrix;

import java.awt.BorderLayout;
import java.awt.Container;
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
        TableColumnHeader columns = new TableColumnHeader(new Model());
        TableRowHeader rows = new TableRowHeader(new Model());
        JScrollPane columnScrollPane = new JScrollPane(columns);
        columnScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        Container c = this.getContentPane();
        c.setLayout(new BorderLayout());
        c.add(columnScrollPane, BorderLayout.NORTH);
        c.add(rows, BorderLayout.CENTER);
        pack();
    }
    
}
