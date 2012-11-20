/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kucharka.app;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kucharka.gui.MainFrm;

import kucharka.model.Recipe;
import kucharka.model.RecipeImpl;
import kucharka.model.RecipeService;

/**
 *
 * @author Michael
 */
public class KucharkaApp {

    private boolean volno;
        
 
    public static void main(String[] args) {
          
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                  RecipeService rs = new RecipeImpl();
//                  ArrayList<Recipe> recepty = new ArrayList<>();
                  new MainFrm().setVisible(true);
//                try {
//                    recepty = rs.search("Ohnivá pánev čerta Šmajstrla");
//                    new RecipeFrm(recepty.get(0));
//                } catch (Exception ex) {
//                    Logger.getLogger(KucharkaApp.class.getName()).log(Level.SEVERE, null, ex);
//                }
              
               
//                
//            }
//        });
    }






}
