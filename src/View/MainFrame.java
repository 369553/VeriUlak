package View;

import Control.IDARE;
import Service.CryptService;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

public class MainFrame extends JFrame{
    private static MainFrame frameMain;
    private static BorderLayout guiOrder;

    public MainFrame(){
        
    }

//İŞLEM YÖNTEMLERİ:
    public static void resetFrame(String code){
        if(CryptService.getService().getMd5(code).equals(IDARE.getIDARE().getRunningCodeAsMd5())){
            System.err.println("Doğru kod.\nAPencere sıfırlanacak inşâAllÂH");
            frameMain.removeAll();
            frameMain.setVisible(false);
            frameMain = null;
            guiOrder = null;
            System.err.println("Bu satır yazdırılıyorsa bir üst satırda hatâ yok demektir bi iznillâh");
        }
    }

//ERİŞİM YÖNTEMLERİ:
    //ANA ERİŞİM YÖNTEMİ:
    public static MainFrame getFrameMain(){
        if (frameMain == null){
            frameMain = new MainFrame();
            frameMain.setLayout(getFrameLayout());
            frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            /*Dimension systemSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(systemSize.width / 4, systemSize.height / 4, systemSize.width / 2, systemSize.height / 2);
            Frame_Main.setPreferredSize(new Dimension(systemSize.width / 2, systemSize.height / 2));*/
            frameMain.setBounds(240, 120, 825, 500);
            //Gerek yok, çünkü frame de boşlu yok Frame_Main.setBackground( Color.decode (SystemSettings.getSettings().getCurrentTheme().getBackgroundColor() ) );
            frameMain.setMinimumSize(new Dimension (825, 400)) ;
        }
        return frameMain ;
    }
    //GİZLİ ERİŞİM YÖNTEMLERİ:
    public static BorderLayout getFrameLayout(){
        if(guiOrder == null){
            guiOrder = new BorderLayout(5, 5);
        }
        return guiOrder;
    }
}