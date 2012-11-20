/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kucharka.model;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author Michael
 */
public class Recipe {
    private String name = "";
    private String id;
    private String category = "";
    private String process = "";
    private List<String> ingredience;
    private URL obrazek;
    Icon icon = null;
    
    
    
    public Recipe(){
        
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {                     
        this.id = id;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the process
     */
    public String getProcess() {
        return process;
    }

    /**
     * @param process the process to set
     */
    public void setProcess(String process) {
        this.process = process;
    }

    /**
     * @return the ingredience
     */
    public List<String> getIngredience() {
        return ingredience;
    }

    /**
     * @param ingredience the ingredience to set
     */
    public void setIngredience(List<String> ingredience) {
        this.ingredience = ingredience;
    }

    /**
     * @return the obrazek
     */
    public URL getObrazek() {
        return obrazek;
    }

    public Icon getIcon() {
        return icon;
    }

    /**
     * @param obrazek the obrazek to set
     */
    public void setObrazek(String obrazek)  {
       
           // this.obrazek = new URL(obrazek);
            try {
                if (obrazek.length() > 0 ) {
                    if (!obrazek.equals("http://www.ceskahospodynka.cz/image/ss.jpg")) {
                      this.obrazek = new URL(obrazek);  
                    }else {
                    this.obrazek = null;
                    }  
                } else {
                    this.obrazek = null;
                }
            } catch (MalformedURLException ex) {
                //pohlcena vyjimka protoze s ji osetruji sam zapsanim hodnoty null do vysledku
               // Logger.getLogger(Recipe.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (obrazek.length() > 0) {
                    if (obrazek.equals("http://www.ceskahospodynka.cz/image/ss.jpg")) {
                        this.obrazek = null;
                    } 
            }
            }
    
        
    }
    
    
}
