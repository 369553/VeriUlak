package View;

import Control.Movements;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MnPreferences extends JMenu{
    ArrayList<JMenuItem> mnObjects;//Menü nesneleri
    JMenuItem mnObjOptions;
    Movements movementAct;

    public MnPreferences(){
        super("SEÇENEKLER");
        addMenuObjects();
        //GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void addMenuObjects(){
        for(JMenuItem obj : getMnObjects()){
            this.add(obj);
        }
    }

//ERİŞİM YÖNTEMLERİ:
    public ArrayList<JMenuItem> getMnObjects(){
        if(mnObjects == null){
            mnObjects = new ArrayList<JMenuItem>();
            mnObjects.add(getMnObjOptions());
        }
        return mnObjects;
    }
    public JMenuItem getMnObjOptions(){
        if(mnObjOptions == null){
            mnObjOptions = new JMenuItem("Ayarlar");
            mnObjOptions.addActionListener(actionListener);
            mnObjOptions.addMouseListener(getMovementActs());
        }
        return mnObjOptions;
    }
    public Movements getMovementActs(){
        if(movementAct == null){
            movementAct = new Movements();
        }
        return movementAct;
    }
}