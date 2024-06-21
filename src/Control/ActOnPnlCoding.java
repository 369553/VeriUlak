package Control;

import Base.DataB;
import Service.ClassStringDoubleConverter;
import View.PnlCoding;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ActOnPnlCoding implements ActionListener, ListSelectionListener{
    PnlCoding pnl;
    private Object[] rawData;
    private String headTextOfRaw;
    private Class dType;
    private int colIndex;
    private String codingName;

    public ActOnPnlCoding(PnlCoding panel, Object[] colData, String headTextOfCol, Class dataType){
        this.pnl = panel;
        this.rawData = colData;
        this.headTextOfRaw = headTextOfCol;
        this.dType = dataType;
        this.colIndex = IDARE.getIDARE().getColIndexByName(headTextOfCol);
        updateTextOfDataType();
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == pnl.getBtnComplete()){
            String solutionName = pnl.getPnlSolutions().getGuiLiContent().getSelectedValue();
            if(solutionName == null)
                return;
            if(solutionName.isEmpty())
                return;
            HashMap<String, Object> confs = new HashMap<String, Object>();
            if(dType == Boolean.class){
                confs.put("codeTrueAs1", pnl.getChCodeTrueAs1().isSelected());
            }
            else if(solutionName.equals("Sıralı kodlama")){
                ArrayList<String> order = pnl.getPnlSpecialConf().getLiContent();
                String[] arrOrder = new String[order.size()];
                order.toArray(arrOrder);
                confs.put("order", arrOrder);
            }
            else if(dType == Integer.class || dType == Double.class){
                confs.put("code1AsTrue", pnl.getChCodeTrueAs1().isSelected());
            }
            boolean isSuccess = IDARE.getIDARE().requestCodingColumn(colIndex, solutionName, confs);
            if(isSuccess){
                // TAZELEMELERİ YAP:
                String colName = IDARE.getIDARE().getColumnName(colIndex);
                codingName = colName;
                pnl.setTableData(IDARE.getIDARE().getColumnData(colIndex), colName);
                dType = IDARE.getIDARE().getDataTypeOfColumn(colIndex);
                updateTextOfDataType();
                pnl.getPnlSolutions().changeContent(getLiSolutions());
                pnl.setConfigurationsOfCoding(null);
            }
        }
    }
    public void updateTextOfDataType(){
        pnl.getLblTextOfDataType().setText("Veri tipi : " + ClassStringDoubleConverter.getService().getShortName(dType));
    }
    @Override
    public void valueChanged(ListSelectionEvent e){
        if(e.getSource() == pnl.getPnlSolutions().getGuiLiContent()){
            String selected = pnl.getPnlSolutions().getGuiLiContent().getSelectedValue();
            if(selected == null)
                return;
            if(selected.equals(getCodingName()))// Aynı kodlama tipi için seçenekler aynıdır
                return;
            this.codingName = selected;
            pnl.setConfigurationsOfCoding(selected);
        }
    }
    public ArrayList<String> getUniqueDataAsStringList(){
        ArrayList<String> values = new ArrayList<String>();
        Object[] uniques = IDARE.getIDARE().getUniqueColValues(colIndex);
        for(int sayac = 0; sayac < uniques.length; sayac++){
            values.add(String.valueOf(uniques[sayac]));
        }
        return values;
    }

//ERİŞİM YÖNTEMLERİ:
    public Object[] getRawData(){
        return rawData;
    }
    public String getHeadTextOfRaw(){
        return headTextOfRaw;
    }
    public ArrayList<String> getLiSolutions(){
        return DataB.getdBase().getCodingSolutions(dType);
    }
    public Class getDataType(){
        return dType;
    }
    public String getCodingName(){
        if(codingName == null)
            codingName = "";
        return codingName;
    }
}