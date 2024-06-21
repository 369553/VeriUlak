package Service;

import java.util.ArrayList;

public class CSVReader implements IReader{
    private Object[][] data;//[Satır][Sütun]
    private String content;
    private boolean isContentChanged;
    private String sep;// Ayraç

    public CSVReader(String content){
        this.content = content;
        sep = ",";
        readData();
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public boolean readData(){
        if(content == null)
            return false;
        if(content.isEmpty())
            return false;
        if(!content.contains(sep))
            return false;
        ArrayList<ArrayList<String>> rowData = new ArrayList<ArrayList<String>>();
        for(String row : content.split("\n")){
            //System.out.println("Satır verisi : " + row);
            ArrayList<String> colData = new ArrayList<String>();
            for(String col : row.split(sep)){
                colData.add(col);
                //System.out.println("colData : " + col);
            }
            //System.err.println("\n-----------\n");
            rowData.add(colData);
        }
        assignData(rowData);
        return true;
    }
    @Override
    public String showLastError(){
        return null;
    }
    public void setContent(String content){
        isContentChanged = true;
        this.content = content;
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void assignData(ArrayList<ArrayList<String>> asLi){
        int sayac = 0;
        data = new Object[asLi.size()][];
        for(ArrayList<String> row : asLi){
            Object[] dR = new Object[row.size()];
            int s2 = 0;
            for(String ww : row){
                //System.err.println(sayac + " : iç sayım");
                dR[s2] = ww;
                s2++;
            }
            data[sayac] = dR;
            sayac++;
        }
    }

//ERİŞİM YÖNTEMLERİ:
    public String getContent(){
        return content;
    }
    @Override
    public Object[][] getData(){
        return data;
    }
}