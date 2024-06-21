package Control;

import View.ContactPanel;
import View.IPanel;
import View.MainFrame;
import View.PnlChooseData;
import View.PnlMain;
import View.PnlTable;
import View.PnlTopMenu;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;

public class GUIIdare{
    private static GUIIdare guiI;
    private IDARE idare;// Çalıştığı IDARE
    private MainFrame H;// Çalıştığı pencere
    private PnlMain MP;// Ana panel
    private PnlTopMenu TM;// Üst menü
    private Component SM;// Yan menü
    private ContactPanel contact;//İletişim paneli
    private HashMap<String, Component> compAndNames;
    private Component curr;//currentComp
    private BorderLayout compOrd;//componentOrder
    private boolean isGUIIdareActive = false;
    private boolean isTMActive = false;
    private boolean isSMActive = false;
    int hGap = 7, vGap = 4;//hGap = horizontalGap, vGap = verticalGap
    private InteractiveGUIStructs activeStructsIDARE;
    private ArrayList<Component> liCompsOnMPAsNotMain;// Ana panelin (MP) içerisinde ana görsel bileşen olarak bulunmayan görsel bileşenler

    private GUIIdare(IDARE idare, MainFrame frame){
        this.idare = idare;//Sistem idârecisini tanıyabilmek için kaydet
        this.H = frame;//Yazılım ana penceresini ata
    }

//İŞLEM YÖNTEMLERİ:
    //BAŞLANGIÇ YÖNTEMİ:
    public static GUIIdare startGUIIDARE(IDARE idare, MainFrame frame){
        if(idare != null){
            guiI = new GUIIdare(idare, frame);
            guiI.activateGUIIdare();//Başlangıç için işlemler
        }
        return guiI;
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void activateGUIIdare(){
        H.setLayout(getCompOrd());//Ana pencerenin görsel düzenini ayarla
        Dimension dimOfMF = H.getSize();//Ana pencerenin boyutlarını al
        MP = new PnlMain();
        MP.setPreferredSize(new Dimension(dimOfMF.width - 5, dimOfMF.height - 5));//Ana paneli ortala
        contact = ContactPanel.getContactPanel();//İletişim panelini oluştur
        H.add(MP, BorderLayout.CENTER);
        H.add(contact, BorderLayout.SOUTH);
        addTopMenu();
        InteractiveGUIStructs.startActiveManager(MP);
        isGUIIdareActive = true;
        addToMP(new PnlChooseData());
        H.setVisible(true);
    }
    public boolean addToMP(Component comp){
        if(comp == null)
            return false;
        String name = comp.getClass().getName();
        if(curr != null){
            String strCurrent = curr.getClass().getName();
            removeWith(strCurrent);
            removeCurrentComp();
        }
        getCompAndNames().put(name, comp);
        Add.addComp(MP, comp, 0, 0, 4, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, 1.0, 1.0, 2, 5, 2, 5);
        addWith(name);
        curr = comp;
        refresh();
        return true;
    }
            // Alttaki yöntemi geliştir:
    public boolean addToMPWithMeasuresWithoutRemoveCurrentOnes(Component comp, GuiConstraint constraints){
        if(comp == null)
            return false;
        String name = comp.getClass().getName();
        getCompAndNames().put(name, comp);
        getLiCompsOnMPAsNotMain().add(comp);
        Add.addComp(MP, comp, constraints.constraints.gridx, constraints.constraints.gridy, constraints.constraints.gridwidth, constraints.constraints.gridheight, constraints.constraints.anchor,
                constraints.constraints.fill, constraints.constraints.weightx, constraints.constraints.weighty, 2, 5, 2, 5);
        refresh();
        return true;
    }
    protected void refresh(){// BURADA BAŞKA İŞLEME GEREK VAR MI?
        H.repaint();
        H.setVisible(true);
    }
    protected void addSideMenu(Component sideMenu, boolean isForRightSide){
        if(sideMenu == null)
            return;
        if(SM != null){// Daha önce bir yan menü eklendiyse
            //.;.
        }
        SM = sideMenu;
        String loc = BorderLayout.EAST;
        if(!isForRightSide)
            loc = BorderLayout.WEST;
        H.add(SM, loc);
        isSMActive = true;
    }
    protected String[] getActivePanelNames(){
        ArrayList<String> actives = new ArrayList<String>();
        actives.add(getCurr().getClass().getName());
        //.;. : 
//        for(String s : )
        String[] strLiActives = new String[actives.size()];
        actives.toArray(strLiActives);
        return strLiActives;
    }
    protected Component[] getActivePanels(){
        ArrayList<Component> actives = new ArrayList<Component>();
//        actives.add(getCurr());
        for(Component cmp : MP.getComponents()){
            actives.add(cmp);
//            System.err.println("cmp.name : " + cmp.getClass().getName());
        }
        Component[] cmps = new Component[actives.size()];
        actives.toArray(cmps);
        return cmps;
    }
    protected void updateDataOnActivePanels(){
        HashMap<String, Object> processDataPack = idare.getLastProccessInfo();
        for(Component cmp : getActivePanels()){
            if(cmp.getClass().getName().equals(PnlTable.class.getName())){
                IPanel pnl = (IPanel) cmp;
                pnl.updateDataFor(processDataPack);
            }
        }
        if(isSMActive){
            ((IPanel) SM).updateDataFor(processDataPack);
        }
    }
    protected void updateDataOnSelectedPanels(ArrayList<String> panelNames){
        if(panelNames == null)
            return;
        for(int sayac = 0; sayac < panelNames.size(); sayac++){
            IPanel pnl = null;
            if(panelNames.get(sayac) == null)
                continue;
            Component target = this.getCompAndNames().get(panelNames.get(sayac));
            if(target == null){
                if(SM.getClass().getName().equals(panelNames.get(sayac)))
                    target = SM;
                else
                    continue;
            }
            try{
                pnl = (IPanel) target;
                pnl.updateDataFor(IDARE.getIDARE().getLastProccessInfo());
            }
            catch(ClassCastException exc){
                System.err.println("Verilen panel istenilen fonksiyonu desteklemiyor");
            }
        }
    }
    public GuiConstraint produceGuiConstraint(){
        return new GuiConstraint();
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void addTopMenu(){
        if(TM == null)
            TM = new PnlTopMenu();
        H.add(TM, BorderLayout.NORTH);
        isTMActive = true;
    }
    private void removeCurrentComp(){
        MP.remove(curr);
    }
    private void removeWith(String componentName){//Panelle berâber kaldırılacak yan panel vs. bileşenleri kaldır
        if(componentName.equals(PnlTable.class.getName())){
            if(SM != null)
                H.remove(SM);
        }
        /*else if(){
            
        }*/
    }
    private void addWith(String componentName){//Panelle eklenecek yan panel vs. bileşenleri ekle
        switch(componentName){
            case "s" :{
                
                break;
            }
            case "" :{
                
                break;
            }
        }
    }

//ERİŞİM YÖNTEMLERİ:
    //ANA ERİŞİM YÖNTEMİ:
    public static GUIIdare getGUIIDARE(){
        return guiI;
    }
    public HashMap<String, Component> getCompAndNames(){
        if(compAndNames == null){
            compAndNames = new HashMap<String, Component>();
        }
        return compAndNames;
    }
    public Component getCurr(){
        return curr;
    }
    public BorderLayout getCompOrd(){
        if(compOrd == null){
            compOrd = new BorderLayout(hGap, vGap);
        }
        return compOrd;
    }
    public int gethGap(){
        return hGap;
    }
    public int getvGap(){
        return vGap;
    }
    public Dimension getDimensionOfMainPanel(){
        if(MP == null)
            return new Dimension(0, 0);
        return MP.getPreferredSize();
    }
    protected PnlTopMenu getTopMenu(){
        return TM;
    }
    protected InteractiveGUIStructs getActiveStructsIDARE(){
        if(activeStructsIDARE == null){
            activeStructsIDARE = InteractiveGUIStructs.getActiveManager();
        }
        return activeStructsIDARE;
    }
    protected ArrayList<Component> getLiCompsOnMPAsNotMain(){
        if(liCompsOnMPAsNotMain == null){
            liCompsOnMPAsNotMain = new ArrayList<Component>();
        }
        return liCompsOnMPAsNotMain;
    }
    
// İÇ SINIF:
    public class GuiConstraint{
        GridBagConstraints constraints;
        boolean isOnXAxis = false;
        public GuiConstraint (){
            this.constraints = new GridBagConstraints();
        }

        // İŞLEM YÖNTEMLERİ:
        public void setAnchorIsCenter(){
            this.constraints.anchor = GridBagConstraints.CENTER;
        }
        public void setAnchorIsSouth(){
            this.constraints.anchor = GridBagConstraints.SOUTH;
        }
        public void setAnchorIsNorth(){
            this.constraints.anchor = GridBagConstraints.NORTH;
        }
        public void setAnchorIsWest(){
            this.constraints.anchor = GridBagConstraints.WEST;
        }
        public void setAnchorIsEast(){
            this.constraints.anchor = GridBagConstraints.EAST;
        }
        public void setFillTypeHorizontal(){
            this.constraints.fill = GridBagConstraints.HORIZONTAL;
        }
        public void setFillTypeVertical(){
            this.constraints.fill = GridBagConstraints.VERTICAL;
        }
        public void setFillTypeNone(){
            this.constraints.fill = GridBagConstraints.NONE;
        }
        public void setFillTypeBoth(){
            this.constraints.fill = GridBagConstraints.BOTH;
        }
        public void setWantedHeightAsPercent(double percentHeight){
            this.constraints.weighty = (percentHeight / 100);
        }
        public void setWantedWidthAsPercent(double percentWidth){
            this.constraints.weightx = (percentWidth / 100);
        }
        public void setOnXAxis(){
            this.isOnXAxis = true;
        }
        public void setOnYAxis(){
            this.isOnXAxis = false;
        }
    }
}