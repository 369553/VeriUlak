package Control;

import Base.DataB;
import Service.ClassStringDoubleConverter;
import View.PnlInterestWrongData;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ActOnPnlInterestWrongData implements ActionListener, ListSelectionListener{
    private PnlInterestWrongData pnl;// Panel
    private String currentColName = "";// Seçili sütunun ismi
    private Class dType;// Seçili sütunun veri tipi
    private ArrayList<String> listOfProcesses;
    private boolean isUpdate = false;// Şu andaki bilgiler seçili elemanın bilgileri 'true' olmalıdır
    private Integer[] empties;// Sütundaki boş hücrelerin indeksleri
    private HashMap<String, Method> mapMethodsFromProcesses;
    private int currentColIndex;
    private Object value;

    public ActOnPnlInterestWrongData(PnlInterestWrongData panel){
        this.pnl = panel;
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == pnl.getBtnComplete()){
            String selectedSolution = pnl.getPnlSolutions().getGuiLiContent().getSelectedValue();
            if(selectedSolution == null)
                return;
            if(selectedSolution.equals("Özel bir değerle doldur")){
                value = pnl.getTxtValue().getText();
                // Dönüşüm kontrolüne gerek yok; zâten hedef veri tipine uygun veri girilmezse engelleniyor ('KeyListenerForDataType' ile)
            }
            boolean isSuccess = false;
            if(selectedSolution.equals("Eksik verilerin bulunduğu satırları sil")){
                Integer[] indexesAsObj = IDARE.getIDARE().getIndexesOfEmptyCellsOnColumn(currentColIndex);
                int[] rowIndexes = new int[indexesAsObj.length];
                for(int sayac = 0; sayac < rowIndexes.length; sayac++){
                    rowIndexes[sayac] = indexesAsObj[sayac];
                }
                isSuccess = IDARE.getIDARE().requestDeleteRows(rowIndexes);
            }
            else{
                isSuccess = IDARE.getIDARE().requestFillEmptiesOnColumn(currentColIndex, selectedSolution, value);
            }
             
            if(isSuccess)
                pnl.setColData(IDARE.getIDARE().getColumnData(currentColName), currentColName, dType);
            System.err.println("isSuccessful . " + isSuccess);
        }
    }
    @Override
    public void valueChanged(ListSelectionEvent e){
        if(e.getSource() == pnl.getPnlColumns().getGuiLiContent()){// Sütun seçildiğinde
            isUpdate = false;
            String selected = pnl.getPnlColumns().getGuiLiContent().getSelectedValue();
            if(currentColName.equals(selected))
                return;
            currentColName = selected;
            currentColIndex = IDARE.getIDARE().getColIndexByName(currentColName);
            dType = IDARE.getIDARE().getDataTypeOfColumn(currentColName);
            pnl.setColData(IDARE.getIDARE().getColumnData(currentColName), currentColName, dType);
            pnl.getLblTextOfDataType().setText("Veri tipi : " + ClassStringDoubleConverter.getService().getShortName(dType));
            pnl.getPnlSolutions().changeContent(getListOfProcesses());
            highLightInterestedCells();
            pnl.getKeyActForTxtValue().setDataType(dType);
            pnl.getPnlSolutions().getGuiLiContent().clearSelection();
            pnl.getTxtValue().setEnabled(false);
        }
        else if(e.getSource() == pnl.getPnlSolutions().getGuiLiContent()){// Çözüm seçildiğinde 
            String str = pnl.getPnlSolutions().getGuiLiContent().getSelectedValue();
            if(str == null)
                return;
            if(str.equals("Özel bir değerle doldur")){
                pnl.getKeyActForTxtValue().clearTextField();
                pnl.getTxtValue().setEnabled(true);
                pnl.getTxtValue().requestFocus();
            }
            else{
                value = null;
            }
            pnl.refresh();
        }
    }
    public ArrayList<String> getListOfProcesses(){
        if(!isUpdate)
            fetchListOfProcesses();
        else if(listOfProcesses == null)
            listOfProcesses = new ArrayList<String>();
        return listOfProcesses;
    }
    public void highLightInterestedCells(){//Seçilen sütundaki boş hücreleri vurgula
        pnl.getTblColData().getSelectionModel().clearSelection();
        empties = IDARE.getIDARE().getIndexesOfEmptyCellsOnColumn(currentColIndex);
        if(empties == null)
            return;
        for(int sayac = 0; sayac < empties.length; sayac++){
            if(empties[sayac] < 0)
                continue;
            pnl.getTblColData().getSelectionModel().addSelectionInterval(empties[sayac], empties[sayac]);
        }
        pnl.getTblColData().requestFocus();
    }
    public void setHighlightColor(){
        pnl.getTblColData().setSelectionBackground(Color.decode(DataB.getdBase().getHighLigthedColorForCellBackground()));
        pnl.getTblColData().setSelectionForeground(Color.decode(DataB.getdBase().getHighLigthedColorForCellForeground()));
    }
    public String getInfoText(){
        return DataB.getdBase().getInfoForWrongData();
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void fetchListOfProcesses(){
        if(currentColName == null)
            listOfProcesses = new ArrayList<String>();
        if(currentColName.isEmpty()){
            listOfProcesses = new ArrayList<String>();
            return;}
        listOfProcesses = DataB.getdBase().getLiInterestingOnWrongDataFor(dType);
        isUpdate = true;
    }

//ERİŞİM YÖNTEMLERİ:
    //GİZLİ ERİŞİM YÖNTEMLERİ:
    public HashMap<String, Method> getMapMethodsFromManipulations(){
        if(mapMethodsFromProcesses == null){
            mapMethodsFromProcesses = new HashMap<String, Method>();
            try{
                 mapMethodsFromProcesses.put("Veri tipini dönüştür", this.getClass().getDeclaredMethod("openPnlChangeDataType", null));
                 mapMethodsFromProcesses.put("Eksik verilerle ilgilen", this.getClass().getDeclaredMethod("openPnlInterestWrongData", null));
            }
           catch(NoSuchMethodException | SecurityException exc){
               System.err.println("yöntem alınırken hatâ alındı : " + exc.toString());
           }
        }
        return mapMethodsFromProcesses;
    }
}