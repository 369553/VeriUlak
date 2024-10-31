# VeriUlak Kullanımı

## Veri Setini Yüklemek

Kullanıcı açılan ekranda veri setini seçmek “Veri setini yükleyin” tuşuna basar. Açılan dosya gezgininde kabûl edilen veri formatlarında olan veriler görünür; istenildiğinde alttaki bölmeden bu değiştirilebilmektedir. Eğer veri setinde veriler satır olarak dizilmemişse verilerin transpozunun alınması için (satırlar ve sütunların yer değiştirmesi için)  düğmenin altındaki tik işâreti kaldırılır.

<img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/01-starting.png" alt="" data-align="inline">

Kullanıcı veri seti seçtikten sonra kullanıcıya iki soru sorulur; bu soruların cevâbına göre veri seti analizcisi (‘DataAnalyzer’)nin ilgili yapıcı fonksiyonu çağrılır.

<img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/02-questionOnUploadingDataSet-1.png" alt="" width="309"><img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/02-questionOnUploadingDataSet-2.png" alt="" width="308" data-align="inline">

## Uygulama Ana Ekranı – Genel Görünümü

<img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/04-applicationGeneralSeeming.png" alt="">

## Panelleri tanıyalım

Uygulamada verilerin göründüğü bir tablo pencerenin ana bileşenidir. Veri üzerinde yapılan değişiklikler tabloya yansır ve kullanıcı yaptığı değişiklikleri tabloda görebilir. Tablo ayrıca düzenlemeye açıktır. Tablonun altında gezinmeye yardımcı bir küçük panel vardır:

<img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/05-TableDirectionStick.png" alt="">

Bu panel veri büyüklüğü, tablo üzerinden seçilen hücrenin konumu gibi bilgileri gösteriyor. Tablodaki hücreler düzenlemeye kapalıdır. Bu panelin en sağındaki “DÜZENLE” düğmesine basıldığında tablodaki hücreler çift tıklayarak düzenlenebilir; düğmeye basıldığında düğme üzerinde “KAYDET” yazar. Tablo hücrelerine girilebilecek veri hücrenin bulunduğu sütunun veri tipine uygun olmalıdır; yoksa düzenleme yapılamaz; geri alınır. Düzenleme işlemleri bittiğinde “KAYDET” düğmesine basılarak veri değişiklikleri kaydedilir; kullanıcı bir şekilde sütun veri tipine aykırı bir veri girdiyse kaydetme işlemi başarılı olmamaktadır.

<img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/06-ColumnInfoSide.png" alt=""><img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/07-StatisticPanel.png" alt=""><img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/08-ProcessPanel.png" alt="" data-align="center">

Yan menü 5 bileşeni barındırıyor. Birincisi sütun düğmesi. Sütun düğmesi en üstte yer alır ve sütundaki toplam dolu veri sayısını, sütun veri tipini ve sütun ismini gösterir. Bu düğmeye basıldığında sütunu bir seçenek menüsü açılır; bu seçenek menüsünde “Sütunu sil” ve “Veri tipini otomatik ata” seçenekleri bulunur. Kullanıcı sütunu silmek isterse bu seçeneğe tıklar; ardından açılan onay kutusunda “Evet”e tıklar. Bunun altında sütun ismini düzenlemek için bir arayüz vardır. Onun altında da istatistikleri gösteren bir panel vardır. İstatistik paneli sütun veri tipinin tipine göre istatistikleri gösterir. Eğer sütun kategorik olarak kodlanırsa bu panelde kategorik istatistikler gösterilir. İstatistik panelinin altında işlemlerin olduğu bir panel vardır. Bu panel de sütun veri tipine göre farklı işlemleri gösterir. Onun altında da sütunlar arasında geçmeye yarayan sütun geçiş tuşları vardır.

<img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/17-UpperMenuPanel.png" alt="" data-align="center">

Kullanıcı ekranın üst tarafındaki menüden uygulamanın görünüm rengini değiştirebilir. Uygulama hakkında eklenen bilgiye erişebilir; ekranlar arasında ileri – geri ilerleyebilir; “Tavsiyeler” panelini açıp – kapatabilir. Tavsiyeler paneli ekranın sol yanında açılan bir menüdür. Bu menü sütun için önerilen bâzı tavsiyeleri göstermektedir. Sağ yan menüdeki sütun değiştirme tuşuyla sütun değiştikçe ilgili sütunun tavsiyeleri de görünmektedir.

## Eksik Verilerle İlgilenmek

Eğer kullanıcı eksik verilerle ilgilenmek istiyorsa sağdaki işlem listesinin olduğu menüden “Eksik verilerle ilgilen” düğmesine basar.

<img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/09-InterestingOnEmptyData.png" alt="" data-align="center">

Açılan pencerenin sağ üst tarafında sütunların seçilebildiği bir liste vardır. Sütunlar seçildiğinde ilgili sütunun veri tipi panelin en üstünde gözükür; sütun verileri resimde göründüğü gibi tabloda görünür; eksik veriler tabloda kırmızı renkle işâretlenmiştir. Sağ altta sütunun veri tipine göre uygulanabilecek çözümler yer almaktadır; onun yanında ise bilgilendirme paneli yer almaktadır. Panelin alt kısmında “Özel değer : ” yazan alan, çözümler kısmından özel değerle doldurma seçeneği seçildiğinde etkinleşen bir yazı girdisi alanıdır. Tercih yapıldıktan sonra “Tamâmla” düğmesine basılır.

## Sütun Veri Tipini Değiştirme

Yazılımda güvenlik açısından sütunda birden fazla tipte veri barınmamaktadır. Birden fazla veri barındığı durumda sütun veri tipi “String” olarak atanmakta ve veriler “String” tipinde tutulmaktadır. Sütun veri tipini yeniden otomatik atamak için sütunun ana düğmesine basarak açılan panelden “Veri tipini otomatik ata” seçeneği seçilebilir. Eğer sütun veri tipi dönüştürülmek isteniyorsa işlemlerin olduğu kısımdaki “Veri tipini dönüştür” yazan düğmeye tıklanır. Burada sütun veri tipine göre sütun veri tipinin dönüştürülebileceği muhtemel veri tipleri ve dönüşümle ilgili bâzı parametreler girmeye yardımcı arayüzler vardır.

<img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/10ConvertionDataType.png" alt="" data-align="center">

Sütunun mevcut veri tipi sol üstte gösterilmektedir. Sağ taraftaki listede de sütun veri tipinin dönüştürülebileceği veri tipleri yer almaktadır. Bu veri tipleri arasından seçim yapıldığında o dönüşüm tipine özgü yapılabilecek ayarlar panelin ortasında yer alır. Misal, kullanıcı noktalı sayı barındıran bir sütunu tam sayı tipine çevirmek istediğinde, eğer uyumsuzluk varsa “Küçük uyumsuzlukları dikkate alma” seçeneğini işâretler ve uyumsuz verilerin yuvarlanma politikasını açılan arayüzden seçer. Eğer noktalı sayı veri tipine dönüşüm yapılmak isteniyor ve sayının uzun olan kesirli kısmının basamak sayısını belirtilmek isteniyorsa, yine “Küçük uyumsuzlukları dikkate alma” seçeneği işâretlenir ve arayüzden kesir basamak sayısını seçer.

## Sütunu Kategorik Kodlama

Veri biliminde kategorik / ayrık verilerin nasıl ele alınacağı önemli bir konudur. Hâzırlanan yazılım bu tip veriler için kategorik dönüşüm özelliğine sâhiptir. Bunun için işlemlerin olduğu sağ yandaki menüden “Sütunu kodla” seçeneği seçilir.

<img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/11-ColumnCategoricalEncoding.png" alt="" data-align="center">

Açılan panelde kategorik kodlama çözümleri, seçilen çözüme göre kodlama yapılandırma ayarı ve sağ üstte sütun verileri görünür. Kullanıcı uygun çözümü seçer ve dilerse kodlama yapılandırma ayarını değiştirir. Bu ayarlar değişime göre farklılık göstermektedir. Resimde “Sıralı kodlama” çözümü seçilmiştir. Misal, resimdeki yapılandırma ayarı hangi kategorinin daha büyük tamsayı değeri olacağı ayarıdır. Kullanıcı sütun tekil verilerinin göründüğü listeden dilediği indisi seçer ve yandaki tuşlarla kategorinin sıralı kodlamadaki sıra numarasını değiştirebilir.
Eğer sütun “Tek nokta vektörü (One Hot Encoding) biçiminde kodlama” seçeneği seçilerek kodlanırsa her kategori için bir sütun oluşturulur ve ilgili dönüşüm tabloya yansıtılır. Aşağıdaki resimde örnek veri setindeki[1] “transmission” (vites tipi) sütununun tek nokta vektörü biçiminde kodlanmış hâli görünmektedir.

<img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/12-ColumnPanelWhichEncodingCategorical.png" alt="" data-align="center">

## Veri Ölçeklendirme

Verileri standardize veyâ normalize etmek için sağ yan menüdeki “Normalizasyon ve Standardizasyon” düğmesine tıklanır. Açılan paneldeki istenilen çözüm seçilir, kullanıcı dilerse bilgilendirmeleri okuyabilir.

<img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/13-DataScalizationPanel.png" alt="" data-align="center">

## Aykırı Veri Tespiti

Kullanıcı sütundaki aykırı verileri tespit etmek için sağ yan menüdeki “Aykırı verilerle ilgilen” düğmesine basar. Açılan pencerede aykırı verilerin kırmızıyla işâretlendiği bir tablo, aykırı veri tespit yöntemi listesi, aykırı verilerin yüzde kaçının silineceğinin bildirilmesi için bir arayüz ve bilgilendirme paneli vardır.

<img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/14-OutlierDetectingPanel.png" alt="" data-align="center">

Kullanıcı satır silme tuşunun sağındaki arayüzden aykırı verilerin yüzde kaçının silimesini istediğini belirtebilir. Orada değişiklik yapıldığında hemen solundaki silme tuşuna ilgili yüzdenin kaç satıra tekâbül ettiği bilgisi kullanıcıya gösterilmektedir. Alt tarafta bilgilendirme paneli yer almaktadır. Aykırı verilerin silinmesiyle ilgili maâlesef fazlaca yöntem sunulmamıştır.

## Görünüm Rengini Değiştirme

Kullanıcı uygulamanın üst menüsündeki “Görünümü değiştir” düğmesine tıklayarak gündüz ve gece görünümleri arasında geçiş yapabilir

<img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/18-NightSeeming.png" alt="" data-align="center">

Uygulamanın en alt tarafında ise kullanıcıya yaptığı işlemin başarısız olup, olmadığı bilgisini vermek gibi iletiler göstermek için bir iletişim paneli vardır.

## Verileri Dışa Aktarmak

Kullanıcı yapmak istediği işlemleri tamâmladıktan sonra üst menüdeki “İLERİ” düğmesine basarak verileri dışarı aktarma ekranına geçiş yapabilir.

<img title="" src="https://github.com/369553/VeriUlak/blob/main/imagesForMdFiles/19-DataTransferingToOutPanel.png" alt="" data-align="center">

Bu ekranda kullanıcıya veri setini ikiye ayırma imkânı sunulmaktadır. Kullanıcı test verisi oranını yüzdelik cinsten belirtmek için arayüz elemanını kullanır. Ardından verilerin kayıt tipini seçer; bu işlem için ayrılan düğmenin hemen altındaki düğmeye tıklayarak dosyaların kayıt dizinini seçer; ardından “Veriyi kaydet” yazılı düğmeye basarak veriyi dışarı aktarma işlemini sonlandırır. Yazılım seçilen dizinde seçilen uzantıda “train” ve “test” isimli dosyaları oluşturuyor; eğer seçilen dizinde bu isimde bir dosya varsa yazılım o dizinde üretilen dosya isminde bir dosya bulunmayana kadar dosya isminin sonuna “ - ” ekledikten sonra 2’den başlayarak numara yazar.

*Şahsî zamân kısıtından ötürü geliştirilme işlemi sekteye uğrayan yazılımı geliştirirerek hedeflenen kalitede bir ön işleme yazılımı olmasını hedeflemekteyim.*

[1] : [100,000 UK Used Car Data set | Kaggle](https://www.kaggle.com/datasets/adityadesai13/used-car-dataset-ford-and-mercedes)


