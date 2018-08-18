/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rangematrix;

import java.util.List;
import javax.swing.event.TreeModelListener;

/**
 *
 * @author daniil_pozdeev
 */
public interface RangeMatrixModel {
    
    
    
    //Column group
    
    int getColumnGroupCount(Object parent);
    
    Object getColumnGroup(Object parent, int index);
    
    boolean isColumnGroup(Object column);
    
    String getColumnGroupName(Object column);
    
    //Row Group
    
    int getRowGroupCount(Object row);
    
    Object getRowGroup(Object row, int index);
    
    boolean isRowGroup(Object row);
    
    String getRowGroupName(Object row);
    
    //Row
    
    int getRowCount();    
    
    Object getRow(int index);    
    
    String getRowName(int index);
    
    List<Object> getRowPath(Object row);
    
    //Values
    
    Object getValueAt(int row, int col);
}
