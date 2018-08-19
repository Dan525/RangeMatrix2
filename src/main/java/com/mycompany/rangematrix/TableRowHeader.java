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
 * @author Daniil
 */
public class TableRowHeader extends JComponent {

    private RangeMatrixModel model;
    private Graphics2D g2d;
    private FontMetrics fm;

    public TableRowHeader(RangeMatrixModel model) {
        init(model);
    }

    TableRowHeader() {

    }

    private void init(RangeMatrixModel model) {
        this.model = model;
        //updateUI();
    }

    @Override
    public void updateUI() {
        setUI(UIManager.getUI(this));
        invalidate();
    }

    public RangeMatrixModel getModel() {
        return model;
    }
    
    float lowestPointHeader = 0;
    int columnCounter = 0;
    float sidePanelWidth = 200;

    ArrayList<Float> cellXList = new ArrayList<>();
    ArrayList<Float> cellYList = new ArrayList<>();
    ArrayList<Float> cellWidthList = new ArrayList<>();
    ArrayList<Float> cellHeightList = new ArrayList<>();

    public float setLowestPoint(float y, float cellHeight) {
        if (y > lowestPointHeader) {
            lowestPointHeader = y + cellHeight;
            return y;
        } else {
            return lowestPointHeader + cellHeight;
        }
    }

    public ArrayList<Object> getLeafRows(Object parentRow, ArrayList<Object> leafRowList) {
        int rowCount = model.getRowGroupCount(parentRow);

        for (int i = 0; i < rowCount; i++) {
            Object child = model.getRowGroup(parentRow, i);
            boolean isGroup = model.isRowGroup(child);
            if (isGroup) {
                getLeafRows(child, leafRowList);
            } else {
                leafRowList.add(child);
            }
        }
        return leafRowList;
    }

    float spaceAroundName = 4;

    public void setSpaceAroundName(int newSpace) {
        this.spaceAroundName = newSpace;
    }

    public float getCellHeight(int heightMultiplier) {
        return (fm.getHeight() + 2 * spaceAroundName) * heightMultiplier;
    }

    public float getWidthOfRowByName(Object row, FontMetrics fm) {
        String rowName = model.getRowGroupName(row);
        return fm.stringWidth(rowName) + 2 * spaceAroundName;
    }
    
    public float getWidthOfRow(int indexOfColumn, int columnCounter, ArrayList<Float> rowsOfColumnList) {
        int rowCount = model.getRowGroupCount(null);

        for (int i = 0; i < rowCount; i++) {
            Object child = model.getRowGroup(null, i);

            boolean isGroup = model.isColumnGroup(child);

            if (isGroup && columnCounter < indexOfColumn) {
                columnCounter++;
                getWidthOfRow(indexOfColumn, columnCounter, rowsOfColumnList);
                columnCounter--;
                
            } else if (columnCounter == indexOfColumn) {
                rowsOfColumnList.add(getWidthOfRowByName(child, fm));
            }         
        }
        return Collections.max(rowsOfColumnList);
    }

    public float getHeightOfRow(Object row, FontMetrics fm) {
        ArrayList<Object> leafRowList = getLeafRows(row, new ArrayList<>());
        if (leafRowList.isEmpty()) {
            return getCellHeight(1);
        } else {
            return getCellHeight(leafRowList.size());
        }
    }
    
    public int getMaxColumnIndex(Object parentRow, ArrayList<Integer> maxColumnIndexList,int maxColumnIndex) {
        int rowCount = model.getRowGroupCount(parentRow);
        
        for (int i = 0; i < rowCount; i++) {            
            Object child = model.getRowGroup(parentRow, i);
            boolean isGroup = model.isRowGroup(child);
            if (isGroup) {
                maxColumnIndex++;
                getMaxColumnIndex(child, maxColumnIndexList, maxColumnIndex);
                maxColumnIndex--;                
            }
            maxColumnIndexList.add(maxColumnIndex);
        }
        return Collections.max(maxColumnIndexList);
    }
    

    public void drawRows(Object parentRow, float parentCellX, float parentCellY, int columnCounter) {
        int rowCount = model.getRowGroupCount(parentRow);
        float cellX = parentCellX;
        float cellY = parentCellY;

        for (int i = 0; i < rowCount; i++) {
            Object child = model.getRowGroup(parentRow, i);
            String columnName = model.getRowGroupName(child);

            float cellWidth = 100;//getWidthOfRow(columnCounter, 1, new ArrayList<Float>());

            boolean isGroup = model.isColumnGroup(child);

            float cellHeight = getHeightOfRow(child, fm);
            
            setLowestPoint(cellY, cellHeight);

            Rectangle2D rect = new Rectangle2D.Float(cellX, cellY, cellWidth, cellHeight);
            g2d.draw(rect);
            g2d.drawString(columnName,
                    cellX + cellWidth / 2 - fm.stringWidth(columnName) / 2,
                    cellY + cellHeight / 2 - fm.getHeight() / 2 + 12);        //12 - высота верхней панели окна

            if (isGroup) {
                columnCounter++;
                cellX += cellWidth;
                drawRows(child, cellX, cellY, columnCounter);
                columnCounter--;
                cellX -= cellWidth;
            }            
            cellY += cellHeight;            
        }
    }

    public void drawValues(FontMetrics fm, Graphics2D g2d) {

        for (int i = 0; i < cellYList.size(); i++) {
            for (int j = 0; j < cellXList.size(); j++) {
                String value = (model.getValueAt(j, i)).toString();
                g2d.drawString(value, cellXList.get(j) + cellWidthList.get(j) / 2 - fm.stringWidth(value) / 2, cellYList.get(i) + 15);
            }
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {

        g2d = (Graphics2D) g;
        fm = g2d.getFontMetrics();

        drawRows(null, 0, 0, 1);

    }
}
