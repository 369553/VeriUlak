package View;

import Base.Statistic;
import Control.ActOnPnlSideMenu;
import Control.Add;
import Control.GUISeeming;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class PnlSideMenu extends JPanel implements IPanel{
    JPanel pnlContent;// Merkezde gösterilen ve için içeriği barındıran panel
    JScrollPane scrpaneContent;// pnlContent için kaydırılabilir görsel bileşen
    BorderLayout compOrder;
    private ActOnPnlSideMenu act;
    int hGap = 5, vGap = 3;
    JPanel pnlSlideButtons;// Seçili sütunu değiştirmek için düğmelerin bulunduğu panel
    JButton btnSlideToBack, btnSlideToNext;// Seçili sütunu değiştirmek için düğmeler
    BtnColumn btnMain;// Temel bilgileri gösteren bir özel düğme
    PnlStatistic pnlStats;// İstatistikleri gösteren panel
    PnlBasic pnlBasic;// Temel bilgileri gösteren panel
    PnlManipulations pnlManp;// Yapılabilecek işlemlere dâir bir panel

    public PnlSideMenu(HashMap<String, Object> dataPack){
        /*
            dataPack içerisinde olması gerekenler:
            1) Sütun bilgileri (DataAnalyzer analyzer.getColumnsDetail()[sütunNo])
            2) Sütun istatistik bilgileri (DataAnalyzer analyzer.getStatisticForColumn(sütunNo))
        */
        act = new ActOnPnlSideMenu(this, dataPack);
        compOrder = new BorderLayout(hGap, vGap);
        this.setLayout(compOrder);
        this.add(getBtnMain(), BorderLayout.NORTH);
        this.add(getScrpaneContent(), BorderLayout.CENTER);
        this.add(getPnlSlideButtons(), BorderLayout.SOUTH);
        addingGUIParts();
        GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public void updateDataFor(HashMap<String, Object> processDataPack){
        getAct().updateDataFor(processDataPack);
    }
    public void refreshGUISeeming(){
        GUISeeming.appGUI(getPnlBasic());
        GUISeeming.appGUI(getPnlManp());
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void addingGUIParts(){
//        liPanels.add(new PnlBasic(IDARE.getIDARE().getColumnDetails(0), getAct()));
        Add.addComp(getPnlContent(), getPnlBasic(), 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        Add.addComp(getPnlContent(), getPnlStats(), 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        Add.addComp(getPnlContent(), getPnlManp(), 0, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        
    }

//ERİŞİM YÖNTEMLERİ:
    public ActOnPnlSideMenu getAct(){
        return act;
    }
    public JPanel getPnlContent(){
        if(pnlContent == null)
            pnlContent = new JPanel(new GridBagLayout());
        return pnlContent;
    }
    public JPanel getPnlSlideButtons(){
        if(pnlSlideButtons == null){
            pnlSlideButtons = new JPanel(new GridLayout(1, 2, 4, 4));
            pnlSlideButtons.setPreferredSize(new Dimension(200, 20));
            pnlSlideButtons.add(getBtnSlideToBack());
            pnlSlideButtons.add(getBtnSlideToNext());
        }
        return pnlSlideButtons;
    }
    public JButton getBtnSlideToBack(){
        if(btnSlideToBack == null){
            btnSlideToBack = new JButton("ÖNCEKİ");
            btnSlideToBack.addActionListener(getAct());
        }
        return btnSlideToBack;
    }
    public JButton getBtnSlideToNext(){
        if(btnSlideToNext == null){
            btnSlideToNext = new JButton("SONRAKİ");
            btnSlideToNext.addActionListener(getAct());
        }
        return btnSlideToNext;
    }
    public BtnColumn getBtnMain(){
        if(btnMain == null){
            btnMain = new BtnColumn((String) getAct().getDataPack().get("name"), (String) getAct().getDataPack().get("dataType"), (int) getAct().getDataPack().get("size"), getAct());
        }
        return btnMain;
    }
    public KeyListener getKeyAct(){
        return (KeyListener) getAct();
    }
    public JScrollPane getScrpaneContent(){
        if(scrpaneContent == null){
            scrpaneContent = new JScrollPane(getPnlContent(),
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//            scrpaneContent.setPreferredSize(new Dimension(this.getWidth() - 4, this.getHeight()));
            scrpaneContent.getHorizontalScrollBar().setUnitIncrement(26);
            scrpaneContent.getVerticalScrollBar().setUnitIncrement(36);
        }
        return scrpaneContent;
    }
    public PnlBasic getPnlBasic(){
        if(pnlBasic == null){
            pnlBasic = new PnlBasic(getAct().getDataPack(), getAct());
        }
        return pnlBasic;
    }
    public PnlStatistic getPnlStats(){
        if(pnlStats == null){
            pnlStats = new PnlStatistic((Statistic) getAct().getDataPack().get("statistic"));
            pnlStats.getBtnMain().addActionListener(getAct());
        }
        return pnlStats;
    }
    public PnlManipulations getPnlManp(){
        if(pnlManp == null){
            pnlManp = new PnlManipulations(getAct().getListOfManipulationsAsStr(), getAct(), getKeyAct(), getAct().getIsNumber(), getAct().getFullListOfManipulationsAsStr());
        }
        return pnlManp;
    }
    
}