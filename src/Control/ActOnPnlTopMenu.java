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
            IDARE.getIDARE().goToNext(null);
        }
        else if(e.getSource() == pnl.getBtnBack()){
            IDARE.getIDARE().goToBack();
        }
        else if(e.getSource() == pnl.getBtnInfo()){
            GUIIdare.getGUIIDARE().getActiveStructsIDARE().showSpecialGUI(pnl.getPnlInfo(), "Hakkında", "", 350, 270);
        }
        else if(e.getSource() == pnl.getBtnChangeSeeming()){
            GUIIdare.getGUIIDARE().changeSeeming();
        }
        else if(e.getSource() == pnl.getBtnAdvices()){
            if(GUIIdare.getGUIIDARE().getIsSM2Opened())
                GUIIdare.getGUIIDARE().closeSecondSideMenu();
            else
                GUIIdare.getGUIIDARE().openSecondSideMenu();
        }
    }

//ERİŞİM YÖNTEMLERİ:
    
    
    
}
