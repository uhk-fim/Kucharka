/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kucharka.gui;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import kucharka.model.Recipe;

/**
 *
 * @author Michael
 */
public class ResultTableModel extends DefaultTableModel{
    int pocetSloupcu = 3;   
    private List<Recipe> data = null;

    @Override
    public void setColumnIdentifiers(Object[] newIdentifiers) {
        super.setColumnIdentifiers(newIdentifiers);
    }
    
    
    
    public void setData(List<Recipe> data){
        
        this.data= data;
       // fireTableStructureChanged();
       fireTableDataChanged();//REFRESH tabulky
    }
    
    
    public ResultTableModel(){
        setColumnCount(pocetSloupcu);
    }

    @Override
    public void setColumnCount(int columnCount) {
        super.setColumnCount(pocetSloupcu);
    }
    @Override
	public int getRowCount() {
		if (data != null) {
			return data.size();
		} 
			return 0;
		
		
	}

    @Override
    public Object getValueAt(int row, int column) {
        if (data!=null) {
            Recipe recept = data.get(row);
            switch(column){
                case 0:return recept.getName();
                case 1:return recept.getCategory();
                case 2:return recept.getId();    
            }
        }
        return null;
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
    
}
