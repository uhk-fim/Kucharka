/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kucharka.gui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicListUI;
import javax.swing.table.DefaultTableCellRenderer;
import kucharka.model.IngredientService;
import kucharka.model.IngredientServiceImpl;
import kucharka.model.Recipe;
import kucharka.model.RecipeImpl;


//import sun.swing.table.DefaultTableCellHeaderRenderer;

//import sun.swing.table.DefaultTableCellHeaderRenderer;



/**
 *
 * @author Michael
 */
public class MainFrm extends javax.swing.JFrame {
     ExecutorService sr = Executors.newFixedThreadPool(1);
     boolean priznak = false;
     boolean prvniTab = true;
     boolean prvniHledani = true;
     boolean bla = false;
     List<String> tabs = new ArrayList<>();
     Object[] nazvySloupcu;
     private IngredientService indredience = new IngredientServiceImpl();

     
     public RecipesFrm oknoReceptu = new RecipesFrm();
     private synchronized void zastavVlakno(){
         priznak = true;
     }
     private synchronized boolean beziVlakno(){
         return vlakno.isAlive();
     }
    Thread vlakno = new Thread(){
        
            @Override
            public void run() {              
            SwingUtilities.invokeLater(new Runnable() {

         @Override
         public void run() {
                            try {
                                data = service.search(tfName.getText(),null, false);
                            } catch (Exception ex) {
                                Logger.getLogger(MainFrm.class.getName()).log(Level.SEVERE, null, ex);
                            }
             tableModel.setData(data);
             priznak = true;
             
             
   
         }
          });   
       }
    };
                
    private void initHlavickySloupcu(){
        Object[] nazvy = new Object[3];
        nazvy[0] = "NÁZEV";
        nazvy[1] = "KATEGORIE";
        nazvy[2] = "ID";
        tableModel.setColumnIdentifiers(nazvy);
    }
    private void initTable(){
        tbRecipe.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //nefunckni pri absolute layoutu
        tbRecipe.getColumnModel().getColumn(0).setPreferredWidth(200);//sirka prvniho sloupce
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();//render pro sloupce
     //  DefaultTableCellHeaderRenderer hlavicka = new DefaultTableCellHeaderRenderer();//render pro hlavicky sloupce
   // hlavicka.setHorizontalAlignment(JLabel.CENTER);//set render to
        render.setHorizontalAlignment(JLabel.CENTER);// set render to
        for (int i = 0; i < tbRecipe.getColumnCount(); i++) {//pro vsechny sloupce nastav tyto rendery
        tbRecipe.getColumnModel().getColumn(i).setHeaderRenderer(render); //tento radek zapricinuje oddelani vzledu hlavicky
        tbRecipe.getColumnModel().getColumn(i).setCellRenderer(render);
        }
        tbRecipe.setAutoCreateRowSorter(true);// inicializace default sortru
        tbRecipe.getTableHeader().setReorderingAllowed(false);// zakazani prehazovani sloupcu !!
        tbRecipe.getTableHeader().setResizingAllowed(false);// znefunkcnit resizable column
        jProgressBar.setStringPainted(true); //nastaveni progress baru tedy presneji cislic v nem
    }
    private RecipeImpl service = new RecipeImpl();
    private ResultTableModel tableModel = new ResultTableModel();
    private List<Recipe> data = new ArrayList<>();
    private ArrayList<String> suroviny = new ArrayList<>();
    private ArrayList<String> surovinyVybrane = new ArrayList<>();
    /**
     * Creates new form MainFrm
     */

    public MainFrm() {
        
       
        initComponents();
        initChovani();
        initListener();
        initHlavickySloupcu();
        initTable();
        initIngredient();
        suroviny.removeAll(suroviny);
        jProgressBar.setValue(0);
        
        jList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                
                if (!(e.getValueIsAdjusting())) {
                   
                    if ((jizExistuje(jList.getSelectedValue().toString()))) {
                        suroviny.add(jList.getSelectedValue().toString());
                        surovinyVybrane.add(jList.getSelectedValue().toString());
                        jVybrane.setListData(surovinyVybrane.toArray());
                    }
                    
                }
            }
        });
        
        jVybrane.addListSelectionListener(surList);
  
    }
    
 
        public boolean jizExistuje(String surovina){
        for (int i = 0; i < surovinyVybrane.size(); i++) {
            if (surovinyVybrane.get(i).equals(surovina)) {
                return false;
            }
        }
        return true;
    }
    private void startHledaniVse(Thread vlaknoVse) {
        if(vlaknoVse instanceof Runnable) {
            vlaknoVse.start();
        }
    }

    private void initChovani()throws java.lang.ClassCastException{
        //setResizable(false); 
        
        tfName.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    setjProgressBarMaximum(100); //set maximum baru
                    setjProgressBar(0); //vynulovani
                    jProgressBar.setValue(30); //progress 30%

            SwingUtilities.invokeLater(new Runnable() {

         @Override
         public void run() {
                    try {             
                        if (surovinyVybrane.size() != 0) {
                            data = service.search(null,suroviny, false);
                        } else {
                        data = service.search(tfName.getText(), null ,true);
                            
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(MainFrm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    tableModel.setData(data);
                   setjProgressBar(100); //progress 100%
   
                 } 
                 });
                }
            }
        });
    
        btSearch.addActionListener(new ActionListener() {
            
            
            @Override
            public void actionPerformed(ActionEvent e) {
                setjProgressBarMaximum(100); //set maximum baru
                    setjProgressBar(0); //vynulovani
                    jProgressBar.setValue(30); //progress 30%

            SwingUtilities.invokeLater(new Runnable() {

         @Override
         public void run() {
                    try {             
                        if (surovinyVybrane.size() != 0) {
                            data = service.search(null,suroviny, false);
                        } else {
                        data = service.search(tfName.getText(), null ,true);
                            
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(MainFrm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    tableModel.setData(data);
                   setjProgressBar(100); //progress 100%
   
                 } 
                 });
                   
        
           }
        });
        
        btnAll.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //initVlaknoVse();
                startHledaniVse(vlaknoVse);
            }
        });
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfName = new javax.swing.JTextField();
        btSearch = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbRecipe = new javax.swing.JTable();
        btnAll = new javax.swing.JButton();
        jProgressBar = new javax.swing.JProgressBar(0,100);
        jScrollPane2 = new javax.swing.JScrollPane();
        jList = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        jVybrane = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tfName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfNameKeyReleased(evt);
            }
        });

        btSearch.setText("Hledej");
        btSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSearchActionPerformed(evt);
            }
        });

        tbRecipe.setModel(tableModel);
        jScrollPane1.setViewportView(tbRecipe);

        btnAll.setText("Vše");

        jList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList);

        jVybrane.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jVybrane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAll, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 58, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSearch)
                    .addComponent(btnAll))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)))
                .addComponent(jProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public void initListener(){
        //tableModel.addTableModelListener(tabulkaListener);
        tbRecipe.getSelectionModel().addListSelectionListener(klikani);
    }
    private void btSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSearchActionPerformed
    
    }//GEN-LAST:event_btSearchActionPerformed

    private void tfNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfNameKeyReleased
       // vlaknoStart();
    }//GEN-LAST:event_tfNameKeyReleased

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
////        try {
////            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
////                if ("Nimbus".equals(info.getName())) {
////                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
////                    break;
////                }
////            }
////        } catch (ClassNotFoundException ex) {
////            java.util.logging.Logger.getLogger(MainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
////        } catch (InstantiationException ex) {
////            java.util.logging.Logger.getLogger(MainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
////        } catch (IllegalAccessException ex) {
////            java.util.logging.Logger.getLogger(MainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
////        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
////            java.util.logging.Logger.getLogger(MainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
////        }
//        //</editor-fold>
//
//        /* Create and display the form */
////        java.awt.EventQueue.invokeLater(new Runnable() {
////            public void run() {
////                new MainFrm().setVisible(true);
////            }
////        });
//    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btSearch;
    private javax.swing.JButton btnAll;
    private javax.swing.JList jList;
    public static javax.swing.JProgressBar jProgressBar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList jVybrane;
    private javax.swing.JTable tbRecipe;
    private javax.swing.JTextField tfName;
    // End of variables declaration//GEN-END:variables
    Thread vlaknoHledani = new Thread() {
        

        @Override
        public synchronized void start() {
            if ((vlaknoHledani instanceof Runnable)) {                
                super.start();           
            }

        }   
            @Override
            public void run() {  
                
                try {
                                
                                suroviny.add(tfName.getText());
                                data = service.search(suroviny.get(0), null ,true);
                                    
                                
                            } catch (Exception ex) {
                                Logger.getLogger(MainFrm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                
            SwingUtilities.invokeLater(new Runnable() {

         @Override
         public void run() {
                            
             if (prvniHledani) {
                 tableModel.setData(data);
                 prvniHledani = false;
                
             } else { 
                 tableModel.setData(data);
             }             
             priznak = true;
    } 
    });
                    }
        };
    ListSelectionListener klikani = new ListSelectionListener() {
            
            @Override
            public void valueChanged(ListSelectionEvent e) {
                oknoReceptu.setVisible(true);
               if (e.getValueIsAdjusting()) {
                   
                   int row = tbRecipe.getSelectedRow();
                   if (row != -1) {
                       pridejTab(data.get(row));
                      }
                    
                    }
            }
        
        
        };
    private int vratIndex(String name){
        for (int i = 0; i < oknoReceptu.tabbedPane.getTabCount(); i++) {
            if (oknoReceptu.tabbedPane.getTitleAt(i).equals(name)) {
                return i;
            }
        }
        return 0;
    }
    private boolean uzExistuje(Recipe recept){
            if (!tabs.isEmpty()) {
                for (String tabRecipe1 : tabs) {
                if (tabRecipe1.equals(recept.getName())) {
                    oknoReceptu.tabbedPane.setSelectedIndex(vratIndex(tabRecipe1));//pokud existuje nastav jako aktualni tab
                    //JOptionPane.showMessageDialog(oknoReceptu, "Již existuje", "Již existuje!", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                }
            }
                return false;
            }
            return false;
        }
    private void pridejTab(Recipe recept) {
            
            if (!uzExistuje(recept)) {
                if (recept.getObrazek() != null) {
                if (prvniTab == true) {
                       TabRecipe tab = new TabRecipe(recept);                      
                       tabs.add(recept.getName());
                       oknoReceptu.tabbedPane.addTab(tab.name,tab);
                       oknoReceptu.tabbedPane.setSelectedComponent(tab);                      
                       oknoReceptu.pack();
                       prvniTab = false;
                    
                  } else {
                       TabRecipe tab = new TabRecipe(recept);  
                       tabs.add(recept.getName());
                       oknoReceptu.tabbedPane.add(tab.name,tab); 
                       oknoReceptu.tabbedPane.setSelectedComponent(tab);
                       oknoReceptu.pack();
                   }                   
                } else {
                    if (prvniTab == true) {
                       TabRecipeNoImg tab = new TabRecipeNoImg(recept);                      
                       tabs.add(recept.getName());
                       oknoReceptu.tabbedPane.addTab(tab.getName(),tab);
                       oknoReceptu.tabbedPane.setSelectedComponent(tab);                      
                       oknoReceptu.pack();
                       prvniTab = false;
                    
                  } else {
                       TabRecipeNoImg tab = new TabRecipeNoImg(recept);  
                       tabs.add(recept.getName());
                       oknoReceptu.tabbedPane.add(tab.getName(),tab); 
                       oknoReceptu.tabbedPane.setSelectedComponent(tab);
                       oknoReceptu.pack();
                   } 
                }
            }
            
        }
     

//    private void startHledani(Thread vlaknoHledani) {
//        if (vlaknoHledani instanceof Runnable) {
//            vlaknoHledani.start();
//        }
//    }
    Thread vlaknoVse =  new Thread() {

            @Override
            public synchronized void start() {            
                super.start();
            }
           
            @Override
            public void run() {      
                try {
                                jProgressBar.setMaximum(3300);
                                jProgressBar.setValue(0);
                                ArrayList<Recipe> vsechnyRecepty = new ArrayList<>(3300);
                                Integer i = 1;
                                Recipe recept = service.searchById(i.toString());
                                while (i <= 3300) {  
                                    if (recept.getId() != null) {         
                                    try {
                                        System.out.println(i.toString());
                                        vsechnyRecepty.add(recept);
                                    } catch (java.lang.OutOfMemoryError e) {
                                        e.printStackTrace();
                                    }
                                    jProgressBar.setValue(i);
                                    i++;
                                    recept=service.searchById(i.toString());
                                   }   else if(recept.getId() == null){
                                    jProgressBar.setValue(i);
                                       i++;
                                       recept = service.searchById(i.toString());
                                    }  
                                }
                                System.out.println(recept.getId());
                                data = vsechnyRecepty;
                            } catch (Exception ex) {
                                Logger.getLogger(MainFrm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                
            SwingUtilities.invokeLater(new Runnable() {

             @Override
            public void run() {
                            
             if (prvniHledani) {
                 tableModel.setData(data);
                 prvniHledani = false;
             } else {
                 tbRecipe.getSelectionModel().removeListSelectionListener(klikani);
                 
                 tableModel.setData(data);
                 initListener();
             }
                            
             priznak = true;
    } 
    });
                    }
        };



    /**
     * @param jProgressBar the jProgressBar to set
     */
    public static void setjProgressBar(int aktualne) {
        MainFrm.jProgressBar.setValue(aktualne); //setr aktualni pozice progress baru
    }
    public static void setjProgressBarMaximum(int pocetZaznamu){
        MainFrm.jProgressBar.setMaximum(pocetZaznamu); //setr rozsahu progress baru
        
    }  

    private void initIngredient() {
        //pack();
        jList.removeAll();
        List<String> data = indredience.loadingredients();
        jList.setListData(data.toArray());
        jVybrane.removeAll();
        
    }
    ListSelectionListener surList = new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!(e.getValueIsAdjusting())) {
                    jVybrane.removeListSelectionListener(this);
                    surovinyVybrane.remove(jVybrane.getSelectedIndex());
                    jVybrane.removeAll();
                    jVybrane.setListData(surovinyVybrane.toArray());
                    jVybrane.addListSelectionListener(surList);
                }
            }
        };
}
