package View;

import java.util.HashMap;

public interface IGUIElementOnList{

    //SOYUT İŞLEM YÖNTEMLERİ:
    public void changeContent(HashMap<String, Object> dataPack);// Bu görsel öğe ile hangi sütun için verilerin gösterileceğini belirtmek için..
}
/*
    Paneller için gönderilen paketlerin anahtarları:
        PnlBasic : name->Sütunun ismi, number->Sütunun numarası
        PnlStatistic : name->Sütunun ismi, statistic->Sütun istatistik bilgileri
*/