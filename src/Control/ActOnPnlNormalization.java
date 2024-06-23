package Control;

import Base.DataB;
import View.PnlNormalization;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ActOnPnlNormalization implements ActionListener, ListSelectionListener{
    PnlNormalization pnl;// Görsel ekran
    String selectedSolution = "";// Seçilen çözüm
    ArrayList<String> liSolutions;// Çözümler listesi
    HashMap<String, String> mapSolutionToInfo;// Seçilen çözüm hakkında bilgi
    private int colIndex;// Sütun numaraso

    public ActOnPnlNormalization(PnlNormalization panel, int colIndex){
        this.pnl = panel;
        this.colIndex = colIndex;
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == pnl.getBtnComplete()){
            if(selectedSolution.isEmpty())
                return;
            boolean isNormalization = false;
            if(selectedSolution.equalsIgnoreCase("normalizasyon"))
                isNormalization = true;
            boolean isSuccess = IDARE.getIDARE().requestNormalization(colIndex, isNormalization);
        }
    }
    @Override
    public void valueChanged(ListSelectionEvent e){
        String value = pnl.getPnlSolutions().getGuiLiContent().getSelectedValue();
        if(this.selectedSolution.equals(value))
            return;
        this.selectedSolution = pnl.getPnlSolutions().getGuiLiContent().getSelectedValue();
        pnl.getPnlInfo().changeContent(getInfoAboutSolution());
    }
    

//ERİŞİM YÖNTEMLERİ:
    public String getInfoAboutSolution(){
        String info = getMapSolutionToInfo().get(selectedSolution);
        if(info == null)
            info = "";
        return info;
    }
    public ArrayList<String> getLiSolutions(){
        if(liSolutions == null)
            liSolutions = DataB.getdBase().getLiSolutionsOfNormalization();
        return liSolutions;
    }
    public String getSelectedSolution(){
        return selectedSolution;
    }
    public HashMap<String, String> getMapSolutionToInfo(){
        if(mapSolutionToInfo == null){
            mapSolutionToInfo = DataB.getdBase().getMapSolutionOfNormalizationToInfo();
        }
        return mapSolutionToInfo;
    }
}