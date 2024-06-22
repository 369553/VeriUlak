package View;

import Control.ActForPnlTable;
import Control.Add;
import Control.GUISeeming;
import Control.IDARE;
import Service.ClassStringDoubleConverter;
import Service.MatrixFunctions;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
//addRow, isEditables, Classes, InfoPanel, Act mes'elelerini hâllet
//Kategorik olan verilerin düzenlenmesi esnasında kategoriler çıkarsa daha kaliteli olur
public class PnlTable extends JPanel implements IPanel{
    private DefaultTableModel mdlForTblData;
    private JTable tblData;
    private Object[][] data;
    private int colCount, rowCount;// Veri satır sayısında veyâ sütun sayısında değişiklik olursa bunlar da değişmeli
    private String[] columnNames;
    private Class[] typesOfColumns;
    private JScrollPane scrpaneTable;
    GridBagLayout compOrder;
    int hGap = 8, vGap = 2;
    //BottomInfoPanel:
    private PnlInfoStickForTable pnlBottomInfo;
    private ActForPnlTable act;
    private boolean isTypesNotified = false;
    private boolean[] isEditables;
    

    public PnlTable(Object[][] data, String[] columnNames){//Veride 'null' sütun olmamalı; satır - sütun uzunluğu aynı olmalı
        this.data = data;
        colCount = data[0].length;
        rowCount = data.length;
        this.columnNames = columnNames;
        isEditables = MatrixFunctions.produceBooleanArray(columnNames.length, false);
        this.setLayout(getCompOrder());
        Add.addComp(this, getScrpaneTable(), 0, 0, 3, 3, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.7, 10.5);
        Add.addComp(this, getPnlBottomInfo(), 0, 4, 3, 1, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, 0.3, 0.001, 0, 5, 5, 5);
        //getMdlForTblData().setDataVector(data, columnNames);
        GUISeeming.appGUI(this);
    }
    public PnlTable(Object[][] data, String[] columnNames, Class[] columnTypes){
        this(data, columnNames);
        if(columnTypes != null){
            isTypesNotified = true;
            this.typesOfColumns = columnTypes;
        }
    }

//İŞLEM YÖNTEMLERİ:
    public void setTypesOfColumns(Class[] types){
        this.typesOfColumns = types;
        isTypesNotified = true;
        //getTblData().validate();//Bu yöntemi araştır
    }
    public void setEditableOfCells(boolean[] valuesOfEditableAttributesOfColumns){
        int len = 0;
        if(isEditables.length == valuesOfEditableAttributesOfColumns.length)
            len = isEditables.length;
        for(int sayac = 0; sayac < len; sayac++){
            isEditables[sayac] = valuesOfEditableAttributesOfColumns[sayac];
        }
    }
    @Override
    public void updateDataFor(HashMap<String, Object> processDataPack){// !i3i
        String processType = (String) processDataPack.get("processType");
        int colNumber = -1;
        boolean delete = false;
        boolean changeColName = false;
        if(processType.equals("deleteCol")){
            colNumber = (int) processDataPack.get("colNumber");
            delete = true;
        }
        else if(processType.equals("changeColName"))
            changeColName = true;
        if(delete){// Bir sütun silindiyse yapılacak değişiklikler
            int[] delIndex = new int[]{colNumber};
            isEditables = MatrixFunctions.deleteSelectedMembers(isEditables, delIndex);
            this.data = MatrixFunctions.deleteSelectedCols(data, delIndex);
            this.columnNames = MatrixFunctions.deleteSelectedMembers(columnNames, delIndex, String[].class);
            this.typesOfColumns = MatrixFunctions.deleteSelectedMembers(typesOfColumns, delIndex, Class[].class);
            getMdlForTblData().setDataVector(data, columnNames);
        }
        if(changeColName){
            this.columnNames[(int) processDataPack.get("colNumber")] = (String) IDARE.getIDARE().getLastDataPack().get("name");
            getMdlForTblData().setColumnIdentifiers(columnNames);
        }
        if(processType.equals("changeDataType")){
            colNumber = (int) IDARE.getIDARE().getLastDataPack().get("number");
            String strDataType = (String) IDARE.getIDARE().getLastDataPack().get("dataType");
            try{
                this.typesOfColumns[colNumber] = Class.forName(strDataType);
                data = (Object[][])IDARE.getIDARE().getLastDataPack().get("data");
                getMdlForTblData().setDataVector(data, columnNames);
            }
            catch(ClassNotFoundException exc){
                System.err.println("Beklenmedik hatâ : istenen sınıf bulunamadı!" + exc.toString());
            }
        }
        else if(processType.equals("fillEmptyCells")){
            data = (Object[][]) IDARE.getIDARE().getLastDataPack().get("data");
            colNumber = (int) IDARE.getIDARE().getLastDataPack().get("number");
            getMdlForTblData().setDataVector(data, columnNames);
        }
        else if(processType.equals("codeColumn")){
            boolean multipleAffected = (boolean) processDataPack.get("isMultipleColumnAffected");
            if(multipleAffected){
                columnNames = IDARE.getIDARE().getColumnNamesAsArray();
                this.typesOfColumns = IDARE.getIDARE().getDataTypes();
                boolean setTrue = false;
                if(getAct().getIsCanEditingTable())
                    setTrue = true;
                isEditables = MatrixFunctions.produceBooleanArray(IDARE.getIDARE().getColumnCount(), setTrue);
            }
            else{
                String strDataType = (String) IDARE.getIDARE().getLastDataPack().get("dataType");
                colNumber = (int) IDARE.getIDARE().getLastDataPack().get("number");
                this.typesOfColumns[colNumber] = ClassStringDoubleConverter.getService().getClassFromFullName(strDataType);
            }
            data = (Object[][]) IDARE.getIDARE().getLastDataPack().get("data");
            getMdlForTblData().setDataVector(data, columnNames);
        }
    }

//ERİŞİM YÖNTEMLERİ:
    public JTable getTblData(){
        if(tblData == null){
            tblData = new JTable(getMdlForTblData());
            tblData.setCellSelectionEnabled(true);
            tblData.setColumnSelectionAllowed(true);
            tblData.addMouseListener(getAct());
        }
        return tblData;
    }
    public DefaultTableModel getMdlForTblData(){
        if(mdlForTblData == null){
            mdlForTblData = new DefaultTableModel(data, columnNames){
            @Override
            public Class getColumnClass(int columnIndex){
                return getTypesOfColumns()[columnIndex];
            }
            @Override
            public boolean isCellEditable(int row, int column){
                return isEditables[column];
            }
            /*@Override
            public void addRow(Object[] rowData){
//                /if(isTypesNotified){//Sanırım buna gerek yok
//                    
//                }
                //this.addRow(rowData);
            }*/
            };
            mdlForTblData.addTableModelListener(getAct());
        }
        return mdlForTblData;
    }
    public int getRowCount(){
        return rowCount;
    }
    public int getColCount(){
        return colCount;
    }
    public Object[][] getData(){
        return data;
    }
    public String[] getColumnNames(){
        return columnNames;
    }
    public int getHGap(){
        return hGap;
    }
    public int getVGap(){
        return vGap;
    }
    public GridBagLayout getCompOrder(){
        if(compOrder == null){
            compOrder = new GridBagLayout();
        }
        return compOrder;
    }
    public JScrollPane getScrpaneTable(){
        if(scrpaneTable == null){
            scrpaneTable = new JScrollPane(getTblData());
            scrpaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrpaneTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        }
        return scrpaneTable;
    }
    public PnlInfoStickForTable getPnlBottomInfo(){
        if(pnlBottomInfo == null){
            pnlBottomInfo = new PnlInfoStickForTable(getAct());
            pnlBottomInfo.setRowCountString(rowCount);
            pnlBottomInfo.setColCountString(colCount);
            pnlBottomInfo.setPreferredSize(new Dimension(getScrpaneTable().getWidth(), 80));
        }
        return pnlBottomInfo;
    }
    public ActForPnlTable getAct(){
        if(act == null){
            act = new ActForPnlTable(this);
        }
        return act;
    }
    public Class[] getTypesOfColumns(){
        if(typesOfColumns == null){
            typesOfColumns = new Class[columnNames.length];
            for(int sayac = 0; sayac < typesOfColumns.length; sayac++){
                typesOfColumns[sayac] = String.class;
            }
        }
        return typesOfColumns;
    }
}
/*
public ArrayList<JTextField> getTxtColumnNames() {
        if(txtColumnNames == null){
            txtColumnNames = new ArrayList<>();
            for(int index = 0; index < getTableColumnNames().length; index++){
                txtColumnNames.add(new JTextField());
                txtColumnNames.get(index).setPreferredSize(new Dimension(45, 25));
                txtColumnNames.get(index).setText(tableColumnNames[index].toString());
                txtColumnNames.get(index).addKeyListener(getKeyForThis());
                Theme.AppTheme(txtColumnNames.get(index));
            }
        }
        return txtColumnNames;
    }
*/