package Control;

import Base.DataAnalyzer;
import Service.MatrixFunctions;
import View.ContactPanel;
import View.PnlTable;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class ActForPnlTable implements ActionListener, TableModelListener, MouseListener{
    private PnlTable pnl;
    private boolean isCanEditingTable = false;
    private HashMap<String, Boolean> changedCellsLocation;//Yapısı : "satırNo,sütunNo", değiştirilmeDurumu(true || false). Misal : [2][3] hücresi için "2,3", true
    private DefaultTableModel mdlTable;

    public ActForPnlTable(PnlTable tablePanel){
        this.pnl = tablePanel;
        this.mdlTable = tablePanel.getMdlForTblData();
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public void tableChanged(TableModelEvent e){
        //DefaultTableModel mdl = (DefaultTableModel) pnl.getTblData().getModel();
        int col = e.getColumn();
        int row = e.getFirstRow();
        if(row == -1 || col == -1)
            return;
        Object cellCurrentValue = pnl.getTblData().getValueAt(row, col);
//        if(cellCurrentValue == null)// Bu nasıl mümkün olur, bilmiyorum; güvenlik amaçlı ekliyorum
//            return;
        if(cellCurrentValue != null)
            System.out.println("HÜCRE DEĞERİNİN SINIFI : " + cellCurrentValue.getClass().getName());
        Object value = pnl.getData()[row][col];
//        if(value == null)// BU NE İŞE YARIYORDU = ?
//            return;
        if(cellCurrentValue != null){// Yeni değer 'null' değilse
            if(String.valueOf(value).equals(String.valueOf(cellCurrentValue)))// Yeni değer eski değerle aynıysa bir şey yapma
                return;
        }
        if(canAcceptChangingRequest(cellCurrentValue, row, col)){
            getChangeCellsLocation().put(row + "," + col, true);// Değişiklikleri sonra IDARE verisine aktarabilmek için değiştirilen hücrelerin mevkîlerini kaydet
        }
        else{
            ContactPanel.getContactPanel().showMessage("VERİ TİPİ HATÂSI : [" + row + "][" + col + "] hücresindeki veri değiştirilemedi!", "Warning");
            mdlTable.setValueAt(value, row, col);
            return;
        }
        //System.out.println("[" + row + "][" + col + "] hücresinde şu olay gözlendi : " + getEventType(e.getType()));
//        System.out.println("Hareketlilik tespit edildi : " + e.getSource().getClass().getName());
   }
    @Override
    public void actionPerformed(ActionEvent e){
        Object objSource = e.getSource();
        if(e.getSource() == pnl){
            //.;.
        }
        if(((Component) objSource).getParent() == pnl.getPnlBottomInfo()){
            if(objSource == pnl.getPnlBottomInfo().getBtnEditAndSave()){
                JButton btnEditAndSave = (JButton) objSource;
                boolean[] arrIsEditableVlues = null;
                if(isCanEditingTable){
                    boolean areSuccess = saveWithIDARE();
                    if(!areSuccess)// Eğer başarısız olunduysa düzenleme kısmını kapatma
                        return;
                    btnEditAndSave.setText("DÜZENLE");
                    arrIsEditableVlues = MatrixFunctions.produceBooleanArray(pnl.getMdlForTblData().getColumnCount(), false);
                    isCanEditingTable = false;
                }
                else{
                    System.err.println("aqqekopegegewgwegwegwegewgwegewgewg");
                    arrIsEditableVlues = MatrixFunctions.produceBooleanArray(pnl.getMdlForTblData().getColumnCount(), true);
                    btnEditAndSave.setText("KAYDET");
                    isCanEditingTable = true;
                }
                pnl.setEditableOfCells(arrIsEditableVlues);
            }
        }
    }
    @Override
    public void mouseClicked(MouseEvent e){
        int row = pnl.getTblData().getSelectedRow();
        int col = pnl.getTblData().getSelectedColumn();
        Object objSelected = pnl.getTblData().getValueAt(row, col);
        Class type = pnl.getTypesOfColumns()[col];
        if(objSelected == null)
            type = null;
        pnl.getPnlBottomInfo().setCellDataTypeString(type);
        pnl.getPnlBottomInfo().setCellLocationString(row, col);
        System.err.println("Seçilen değer : " + objSelected);
    }
    @Override
    public void mousePressed(MouseEvent e){
    
    }
    @Override
    public void mouseReleased(MouseEvent e){
    
    }
    @Override
    public void mouseEntered(MouseEvent e){
    
    }
    @Override
    public void mouseExited(MouseEvent e){
    
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private String getEventType(int typeIntegerValue){
        switch(typeIntegerValue){
            case 0 : {
                return "UPDATE";
            }
            case 1 : {
                return "INSERT";
            }
            case -1 : {
                return "DELETE";
            }
        }
        return "";
    }
    private boolean saveWithIDARE(){
        boolean res;
        boolean allSuccess = true;// Eğer tüm değişiklikler başarılıysa 'true' olmalıdır
        int sayac = 0;
        for(String str : getChangeCellsLocation().keySet()){
            if(!getChangeCellsLocation().get(str))//
                continue;
            String[] values = str.split(",");// İlk değer satırı, ikinci değer sütunu ifâde ediyor
            int row = Integer.valueOf(values[0]);
            int col = Integer.valueOf(values[1]);
            if(row < 0 || col < 0)
                continue;
            Object value = pnl.getTblData().getValueAt(row, col);
            res = IDARE.getIDARE().requestChangingData(row, col, value);
            if(!res){
                allSuccess = false;// En az biri başarısızsa allSuccess 'false' olmalıdır
                String msg = "[" + row + "][" + col + "] hücresindeki veri değişikliği başarısız oldu! Veri formatını kontrol edin";
                pnl.getMdlForTblData().setValueAt(pnl.getData()[row][col], row, col);// Değişiklik başarısız olduysa önceki değeri yerine koy
                ContactPanel.getContactPanel().showMessage(msg, "Warning");
            }
            else{
                pnl.getData()[row][col] = value;
//                System.out.println("pnl.data." + row + "-" + col + " : " + pnl.getData()[row][col]);
            }
            sayac++;
        }
        return allSuccess;
    }
    private boolean canAcceptChangingRequest(Object value, int row, int col){
        if(row < 0 || col < 0)
            return false;
        if(value == null)
            return true;
        Class[] types = DataAnalyzer.wrapPrimitiveClass(new Class[]{value.getClass(), pnl.getTypesOfColumns()[col]});
        if(types[0] == types[1])
            return true;
        return false;
    }

//ERİŞİM YÖNTEMLERİ:
    public boolean getIsCanEditingTable(){
        return isCanEditingTable;
    }
    //GİZLİ ERİŞİM YÖNTEMLERİ:
    protected HashMap<String, Boolean> getChangeCellsLocation(){
        if(changedCellsLocation == null){
            changedCellsLocation = new HashMap<String, Boolean>();
        }
        return changedCellsLocation;
    }
}
    