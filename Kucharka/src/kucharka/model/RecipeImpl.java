/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kucharka.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import kucharka.app.KucharkaApp;
import kucharka.gui.MainFrm;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 *
 * @author Michael
 */
public class RecipeImpl implements RecipeService{
    private final String RECEPTURL = "http://www.ceskahospodynka.cz/recepty-api/recepty?nazev=";
    private final String SUROVINYURL = "http://www.ceskahospodynka.cz/recepty-api/recepty?suroviny=";
    private final String IDURL = "http://www.ceskahospodynka.cz/recepty-api/recept/";
    private final String SPOJKAAND = "&logSpojkaSuroviny=AND";
    private final String SPOJKAOR = "&logSpojkaSuroviny=OR";
    SAXReader reader;
    
    public RecipeImpl(){
       //zde budu resit vyhledavani podle surovin apod 
        //pokud sem prijde to a to zavola se search takovej a takovej
    }
    /**
     *
     * @param suroviny
     * @param priznak
     * @return
     */
    
    public ArrayList<Recipe> searchBySurovina(List<String> suroviny, boolean priznak){
        ArrayList<Recipe> recepty = new ArrayList<>();
        String adresa = spojURL(suroviny, priznak);
      
        try {
            
            MainFrm.setjProgressBar(40);
            Document doc = new SAXReader().read(new URL(adresa));
            MainFrm.setjProgressBar(50);
            List<Node> recept = doc.selectNodes("//recepty/recept");     
           // MainFrm.setjProgressBarMaximum(recept.size());
            for (Iterator<Node> it = recept.iterator(); it.hasNext();) {
            //MainFrm.setjProgressBar(i);
            //i++;
            Node node = it.next();
            recepty.add(vratRecept(node));
        }
        
        } catch (Exception e) {
        }
//  TEST METODY       
//                for (int j = 0; j < recepty.size(); j++) {
//            System.out.println(recepty.get(j).getId());
//            System.out.println(recepty.get(j).getName());
//            System.out.println(recepty.get(j).getProcess());
//            System.out.println(recepty.get(j).getIngredience());
//            System.out.println(recepty.get(j).getObrazek());
//            
//        }
        MainFrm.setjProgressBar(70);
        return recepty;
    }
        /**
     *
     * @param id id receptu
     * @return
     * @throws Exception
     */
    
    public Recipe searchById(String id) throws Exception {
        ArrayList<Recipe> recepty = new ArrayList<>();
        String adresa = IDURL + id;
        Recipe recept = new Recipe();
        
        try {
            Document doc = new SAXReader().read(new URL(adresa));
            recept = vratRecept(doc.selectSingleNode("recept"));
        } catch (Exception e) {
        }
        
        //System.out.println(recept.getName()); //test metody pomoci vypisu
        return recept;
    }
    private boolean isNumber(String prvniZnak){
        char c = prvniZnak.charAt(0);
        return (c >= '0' && c <= '9');

    } 
    /**
     *
     * @param nazev receptu
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Recipe> search(String nazev, ArrayList<String> suroviny, boolean spojka) throws Exception {
        ArrayList<Recipe> recepty = new ArrayList<>();
        
        if (suroviny != null) {
            recepty = searchBySurovina(suroviny, spojka);
        }else if (isNumber(nazev)) {
            recepty.add(searchById(nazev));
        } else {
            recepty = searchByName(nazev);
        }
        
        return recepty;
    }
    private ArrayList<Recipe> searchByName(String nazev) throws Exception{
        ArrayList<Recipe> recepty = new ArrayList<>();
        String adresa = RECEPTURL + nazev;
        
        try {
        Document doc = new SAXReader().read(new URL(adresa));  
        List<Node> recept = doc.selectNodes("//recepty/recept");
        MainFrm.setjProgressBarMaximum(recept.size()); //zjisteni velikosti resultu pro urceni ProgressBaru
        int i= 0; //init cisla receptu
        for (Iterator<Node> it = recept.iterator(); it.hasNext();) {         
            i++; //inkrementace
            MainFrm.setjProgressBar(i); //nastaveni progressu
            Node node = it.next();
            recepty.add(vratRecept(node));
        }
        } catch (org.dom4j.DocumentException e) {
            JOptionPane.showMessageDialog(null, "Náčtení dokumentu selhalo! Zkontrolujte připojení na internet", "Načtení selhalo", JOptionPane.INFORMATION_MESSAGE);
        }
       
//TEST METODY        
//        for (int j = 0; j < recepty.size(); j++) {
//            System.out.println(recepty.get(j).getId());
//            System.out.println(recepty.get(j).getName());
//            System.out.println(recepty.get(j).getProcess());
//            System.out.println(recepty.get(j).getIngredience());
//            System.out.println(recepty.get(j).getObrazek());
//            
//        }
        
        return recepty;//kolekce receptu vyhledanych prirazenych
    }
    private Recipe vratRecept(Node nod){
        Recipe recept = new Recipe();
        //část kde parsuji recept
        recept.setId(nod.selectSingleNode("id").getStringValue());
        recept.setName(nod.selectSingleNode("nazev").getStringValue());
        recept.setCategory(nod.selectSingleNode("kategorie").getStringValue());
        recept.setProcess(nod.selectSingleNode("postup").getStringValue());
        //zde si nalistuji vsechny suroviny
        List<Node> suroviny = nod.selectNodes("suroviny/surovina");
        List<String> sur = new ArrayList<>();
        for (Iterator<Node> it = suroviny.iterator(); it.hasNext();) {
            Node node = it.next();
            sur.add(node.getStringValue());
        }
        recept.setIngredience(sur);//prirazeni seznamu ingredienci
        recept.setObrazek(nod.selectSingleNode("obrazek").getStringValue());
        
          
        return recept;
    }

    private String spojURL(List<String> suroviny, boolean priznak) {
 
        String adresa = SUROVINYURL;
         for (int i = 0; i < suroviny.size(); i++) {
             if (i != suroviny.size()-1) {
                 adresa += suroviny.get(i);
                 adresa += ","; 
             } else {
                 adresa += suroviny.get(i);
             }      
        }
        if (priznak == true) {
            adresa += SPOJKAAND;
        } else {
            adresa += SPOJKAOR;
        }             
        return adresa;
    }


    
}
