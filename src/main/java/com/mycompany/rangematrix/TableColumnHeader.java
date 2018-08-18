/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rangematrix;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 *
 * @author daniil_pozdeev
 */
public class TableColumnHeader extends JComponent {

    private RangeMatrixModel model;
    private Graphics2D g2d;
    private FontMetrics fm;
    private float spaceAroundName = 4;

    public TableColumnHeader(RangeMatrixModel model) {
        init(model);
    }

    private void init(RangeMatrixModel model) {
        this.model = model;
    }    

    public RangeMatrixModel getModel() {
        return model;
    }

    public ArrayList<Object> getLeafColumns(Object parentColumn, ArrayList<Object> leafColumnList) {
        int columnCount = model.getColumnGroupCount(parentColumn);

        for (int i = 0; i < columnCount; i++) {
            Object child = model.getColumnGroup(parentColumn, i);
            boolean isGroup = model.isColumnGroup(child);
            if (isGroup) {
                getLeafColumns(child, leafColumnList);
            } else {
                leafColumnList.add(child);
            }
        }
        return leafColumnList;
    }    

    public void setSpaceAroundName(int newSpace) {
        this.spaceAroundName = newSpace;
    }

    public float getCellHeight(int heightMultiplier) {
        return (fm.getHeight() + 2 * spaceAroundName) * heightMultiplier;
    }

    public float getWidthOfColumnName(Object column, FontMetrics fm) {
        String columnName = model.getColumnGroupName(column);
        return fm.stringWidth(columnName) + 2 * spaceAroundName;
    }

    public float getWidthOfColumn(Object column, FontMetrics fm) {
        float columnWidth = 0;
        float ownColumnWidth = fm.stringWidth(model.getColumnGroupName(column)) + 2 * spaceAroundName;
        
        ArrayList<Object> leafColumnList = getLeafColumns(column, new ArrayList<>());
        if (leafColumnList.isEmpty()) {
            return ownColumnWidth;
        }
        for (Object leafColumn : leafColumnList) {
            float leafColumnWidth = getWidthOfColumnName(leafColumn, fm);
            columnWidth += leafColumnWidth;
        }
        return columnWidth;
    }
    
    public int getMaxRowIndex(Object parentColumn, ArrayList<Integer> maxRowIndexList,int maxRowIndex) {
        int columnCount = model.getColumnGroupCount(parentColumn);
        
        for (int i = 0; i < columnCount; i++) {            
            Object child = model.getColumnGroup(parentColumn, i);
            boolean isGroup = model.isColumnGroup(child);
            if (isGroup) {
                maxRowIndex++;
                getMaxRowIndex(child, maxRowIndexList, maxRowIndex);
                maxRowIndex--;                
            }
            maxRowIndexList.add(maxRowIndex);
        }
        return Collections.max(maxRowIndexList);
    }

    public int getHeightMultiplier(Object parentColumn, boolean isGroup, int rowIndex, int maxRowIndex) {
        if (!isGroup) {
            return (maxRowIndex - rowIndex) + 1;
        } else {
            return 1;
        }
    }

    public void drawColumns(Object parentColumn, float parentCellX, float parentCellY, int rowCounter, int maxRowIndex) {
        int columnCount = model.getColumnGroupCount(parentColumn);
        float cellX = parentCellX;
        float cellY = parentCellY;

        for (int i = 0; i < columnCount; i++) {
            Object child = model.getColumnGroup(parentColumn, i);
            String columnName = model.getColumnGroupName(child);

            float cellWidth = getWidthOfColumn(child, fm);

            boolean isGroup = model.isColumnGroup(child);

            int heightMultiplier = getHeightMultiplier(parentColumn, isGroup, rowCounter, maxRowIndex);

            float cellHeight = getCellHeight(heightMultiplier);
            
            Rectangle2D rect = new Rectangle2D.Float(cellX, cellY, cellWidth, cellHeight);
            g2d.draw(rect);
            g2d.drawString(columnName,
                           cellX + cellWidth / 2 - fm.stringWidth(columnName) / 2,
                           cellY + cellHeight / 2 - fm.getHeight() / 2 + 12);        //12 - высота верхней панели окна

            if (isGroup) {
                rowCounter++;
                cellY += cellHeight;
                drawColumns(child, cellX, cellY, rowCounter, maxRowIndex);
                rowCounter--;
                cellY -= cellHeight;
            }            
            cellX += cellWidth;            
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        g2d = (Graphics2D) g;
        fm = g2d.getFontMetrics();
        
        int maxRowIndex = getMaxRowIndex(null, new ArrayList<>(), 0);

        drawColumns(null, 0, 0, 0, maxRowIndex);
    }
}
