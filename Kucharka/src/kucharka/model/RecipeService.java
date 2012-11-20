/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kucharka.model;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Michael
 */
public interface RecipeService {
    
    //po sobe jmeno, suroviny a operace
    public ArrayList<Recipe> search(String nazev, ArrayList<String> suroviny, boolean spojka) throws Exception;
    //public List<Recipe> searchBySurovina(List<String> suroviny, boolean priznak) throws Exception;
    //public Recipe searchById(String id) throws Exception;
    
    
}
