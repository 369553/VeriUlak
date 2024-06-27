package Control;

import Base.DataB;
import Base.DataSplitter;
import View.ContactPanel;
import View.PnlDataSpliterSave;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

public class ActOnPnlDataSplitterSave implements ActionListener{
    private PnlDataSpliterSave pnl;
    private String path  = "";

    public ActOnPnlDataSplitterSave(PnlDataSpliterSave panel){
        this.pnl = panel;
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == pnl.getBtnOpenDirChooser()){
            pnl.getDirChooser().showDialog(this.pnl, "Veri kayıt yeri seçin");
        }
        else if(e.getSource() == pnl.getBtnSaveData()){
            if(path.isEmpty()){
                ContactPanel.getContactPanel().showMessage("Lütfen önce bir dosya yolu seçin", "Warning");
                return;
            }
            HashMap<String, Object[][]> sets = DataSplitter.splitTrainTestSet(IDARE.getIDARE().getData(), (double) pnl.getSpinRate().getValue(), 0, false);
            if(sets == null){
                ContactPanel.getContactPanel().showMessage("Veri setleri oluşturulmadı!", "Warning");
                return;
            }
            if(IDARE.getIDARE().exportData(getPath(), (String) pnl.getCmboxFileTypes().getSelectedItem(), sets))
                ContactPanel.getContactPanel().showMessage("Veriler başarıyla dışarı aktarıldı", "Successful");
            else
                ContactPanel.getContactPanel().showMessage("Veriler dışarı aktarılamadı!", "Warning");
        }
        else if(e.getSource() == pnl.getDirChooser()){
            File file = pnl.getDirChooser().getSelectedFile();
            if(file == null)
                return;
            this.path = file.getAbsolutePath();//Dizin adresini al
            System.err.println("path : " + path);
        }
    }
    public String[] getNameOfFileTypes(){
        return DataB.getdBase().getOutputFileTypesAsStr();
    }
    public Dimension getDimensionOfMainPanel(){
        return GUIIdare.getGUIIDARE().getDimensionOfMainPanel();
    }

//ERİŞİM YÖNTEMLERİ:
    public String getPath(){
        return this.path;
    }
}