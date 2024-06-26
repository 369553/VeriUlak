package Control;

import Base.DataB;
import Base.OutlierDetection;
import Service.MatrixFunctions;
import View.ContactPanel;
import View.PnlDetectOutlier;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ActOnPnlDetectOutlier implements ActionListener, ListSelectionListener, ChangeListener{
    private PnlDetectOutlier pnl;// Ana görsel eleman
    private ArrayList<String> liNameOfsolutions;// Aykırı veri tespit yöntemlerinin isimleri
    private OutlierDetection.SOLUTION selectedSolution;// 
    private OutlierDetection detector;// Aykırı veri işleyicisi
    private double percentValue;// Aykırı verilerin ne kadarının silineceği yüzdelik oranı
    private int countOfOutliers = 0;// Seçilen yüzdelik orana göre kaç satırın silinmesi gerektiği bilgisi
    private int colIndex;// Sütun numarası
    private Class dType;// Sütunun veri tipi

    public ActOnPnlDetectOutlier(PnlDetectOutlier panel, Object[] colData, int colIndex, Class dataType){
        this.pnl = panel;
        this.colIndex = colIndex;
        this.dType = dataType;
        detector = OutlierDetection.produceOutlierDetection(colData);
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == pnl.getBtnDeleteOutliers()){
            Object[] outliersRemoved = detector.setNullOutliersDependPercentValue(selectedSolution, percentValue);
            if(saveWithIDARE(outliersRemoved)){
                pnl.setColData(outliersRemoved, pnl.getTblColData().getColumnName(0), dType);
                detector.setValues(outliersRemoved);
                pnl.getSpinRate().setValue(0.0);
                countOfOutliers = 0;
                pnl.updateBtnRowNumber();
                highLightOutliers();
                ContactPanel.getContactPanel().showMessage("Aykırı veriler başarıyla silindi", "Successful");
            }
            else
                ContactPanel.getContactPanel().showMessage("Aykırı verilerin silinmesi işlemi başarısız oldu", "Warning");
        }
    }
    @Override
    public void valueChanged(ListSelectionEvent e){
        if(e.getSource() == pnl.getPnlSolutions()){
            OutlierDetection.SOLUTION selectedNow = getSolutionFromText(pnl.getPnlSolutions().getGuiLiContent().getSelectedValue());
            if(selectedNow == selectedSolution)
                return;
            selectedSolution = selectedNow;
        }
    }
    @Override
    public void stateChanged(ChangeEvent e){
        if(e.getSource() == pnl.getSpinRate()){
            percentValue = (double) pnl.getSpinRate().getValue();
            int rowCountNew = detector.getRowCountOfOutlierDependPercentValue(selectedSolution, (double) pnl.getSpinRate().getValue());
            if(countOfOutliers == rowCountNew)
                return;
            countOfOutliers = rowCountNew;
            pnl.updateBtnRowNumber();
        }
    }
    public void highLightOutliers(){//Seçilen sütundaki boş hücreleri vurgula
        pnl.getTblColData().getSelectionModel().clearSelection();
        int[] empties = getOutlierIndexes();
        if(empties == null)
            return;
        for(int sayac = 0; sayac < empties.length; sayac++){
            if(empties[sayac] < 0)
                continue;
            pnl.getTblColData().getSelectionModel().addSelectionInterval(empties[sayac], empties[sayac]);
        }
    }
    public void setHighlightColor(){
        pnl.getTblColData().setSelectionBackground(Color.decode(DataB.getdBase().getHighLigthedColorForCellBackground()));
        pnl.getTblColData().setSelectionForeground(Color.decode(DataB.getdBase().getHighLigthedColorForCellForeground()));
    }
    public void updateSelectedSolution(){
        selectedSolution = getSolutionFromText(pnl.getPnlSolutions().getGuiLiContent().getSelectedValue());
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private int[] getOutlierIndexes(){
        return detector.getOutlierIndexesDependPercentValue(selectedSolution, 100.0);
    }
    private OutlierDetection.SOLUTION getSolutionFromText(String text){
        switch(text){
            case "Z skoru" :{
                return OutlierDetection.SOLUTION.Z_SCORE;
            }
        }
        return null;
    }
    private boolean saveWithIDARE(Object[] outliersRemoved){
        return IDARE.getIDARE().requestSetColumnData(outliersRemoved, dType, colIndex);
    }

//ERİŞİM YÖNTEMLERİ:
    public ArrayList<String> getLiNameOfsolutions(){
        if(liNameOfsolutions == null)
            liNameOfsolutions = DataB.getdBase().getLiSolutionsForOutliner();
        return liNameOfsolutions;
    }
    public String getInfoText(){
        return DataB.getdBase().getInfoForOutliner();
    }
    public int getCountOfOutliers(){
        return countOfOutliers;
    }
}
