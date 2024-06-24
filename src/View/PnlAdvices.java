package View;

import Base.Advice;
import Control.GUISeeming;
import Control.IDARE;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class PnlAdvices extends PnlVarietyForButton implements IPanel{
    ArrayList<Advice> liAdvices;

    public PnlAdvices(int adviceCount, String[] adviceTexts, ActionListener actForAdvices, ArrayList<Advice> advices){
        super(adviceCount, adviceTexts, actForAdvices, "Tavsiyeler", true);
        this.setPreferredSize(new Dimension(190, 370));
        this.liAdvices = advices;
        setTextSize(12.0f);
//        GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    //ÜRETİM YÖNTEMİ:
    public static PnlAdvices producePnlAdvices(ArrayList<Advice> advices, ActionListener actForAdvices){
        if(advices == null)
            advices = new ArrayList<Advice>();
        String[] texts = null;
        if(advices.size() > 0){
            texts = new String[advices.size()];
            for(int sayac = 0; sayac < advices.size(); sayac++){
                texts[sayac] = advices.get(sayac).getStrAdvice();
            }
        }
        return new PnlAdvices(advices.size(), texts, actForAdvices, advices);
    }

//ERİŞİM YÖNTEMLERİ:
    @Override
    public void updateDataFor(HashMap<String, Object> processDataPack){
        String process = (String) processDataPack.get("processType");
        if(process.equals("changedColumn")){
            int colNumber = (Integer) processDataPack.get("colNumber");
            String[] advs = (String[]) IDARE.getIDARE().getLastDataPack().get("advices");
            this.changeContent(advs);
        }
    }
}