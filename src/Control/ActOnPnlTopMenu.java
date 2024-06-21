package Control;

import View.PnlTopMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActOnPnlTopMenu implements ActionListener{
    PnlTopMenu pnl;

    public ActOnPnlTopMenu(PnlTopMenu pnl){
        this.pnl = pnl;
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == pnl.getBtnNext()){
            //.;.
        }
        if(e.getSource() == pnl.getBtnBack()){
            IDARE.getIDARE().goToBack();
        }
    }

//ERİŞİM YÖNTEMLERİ:
    
    
    
}
