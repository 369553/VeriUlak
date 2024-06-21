package Control;

import View.MnPreferences;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActOnMnPreferences implements ActionListener{
    protected MnPreferences mn;

    public ActOnMnPreferences(){
        this.mn = mn;
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == mn.getMnObjOptions()){
            //.;.
        }
    }

//ERİŞİM YÖNTEMLERİ:
    
    
}