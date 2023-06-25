package proiect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Tabel extends JTable {
    private DefaultTableModel model;
    public Tabel(){
        model=new DefaultTableModel();
        setModel(model);
        this.setEnabled(false);
    }
    public void addRow(String[] newRow){
        model.addRow(newRow);
    }
    public void addRow(String id, boolean state){
        Object[] data = {id, state};
        model.addRow(data);
    }
    public void addColumn(String columnName){
        model.addColumn(columnName);
    }

    public boolean isJTableEmpty() {
        if (this != null && this.getModel() != null)
            return this.getModel().getRowCount() == 0;
        return true;
    }
}
