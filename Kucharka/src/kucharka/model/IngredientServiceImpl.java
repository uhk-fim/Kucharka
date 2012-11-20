/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kucharka.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class IngredientServiceImpl implements IngredientService{

    @Override
    public List<String> loadingredients() {
        BufferedReader br = null;
        ArrayList<String> res = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader("data/ingredients.csv"));
            String line;
            while ((line = br.readLine()) != null) {                
                res.add(parseIngredient(line));
            }
        } catch (Exception ex) {
            Logger.getLogger(IngredientServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (br != null) {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(IngredientServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }               
            }
        }
       return res;
    }

    private String parseIngredient(String line) {
        return line.split(";")[0];
    }
    
    
}
