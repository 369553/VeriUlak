package View;

import Base.Statistic;
import javax.swing.JList;

public class PnlStatistic extends PnlVariety{
    private Statistic stats;

    public PnlStatistic(Statistic statistic){
        super(Statistic.getStatisticAsList(statistic), "İstatistikler");
        stats = statistic;
        super.setMinimumSize(120, 330);
    }

//İŞLEM YÖNTEMLERİ:
    public void changeContent(Statistic statistics){
        this.stats = statistics;
        this.liContent = Statistic.getStatisticAsList(stats);
        super.reReplaceContent();
    }

//ERİŞİM YÖNTEMLERİ:
    public Statistic getStats(){
        return stats;
    }
    public JList<String> getGuiLiStatistic(){
        return super.getGuiLiContent();
    }
}