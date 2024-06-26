package veriulak;

import Control.IDARE;
import View.MainFrame;

public class VeriUlak{
    public static void main(String[] args){
        IDARE.startIDARE("code", MainFrame.getFrameMain());
//        runTests();
//        runTestsOrdered();
    }

//TEST YÖNTEMLERİ:
    public static void runTests(){// Hızlı başlangıç için DataAnalyzer anl = readAndStartAnalyzerFor("example - 2 data.xlsx");
        // Öncül yöntem : readAndStartAnalyzerFor
        Test test = new Test();
        //test.csvOkumaOrnegi();
        //test.xlsxDenemeler();
        //test.divideMatrix();
        //test.sortTest(false);
        //test.deleteSelectedRows();
        //test.xlsxOkumaOrnegi();
        //test.invokeMethodWithManyArgsAsList();
        //test.testIsNumericalCellIfCellIsEmpty();
        //test.testCanWrapperClassAsPrimitive();
        //test.canIntegerDetectDoubleValueNormally();
        //test.canDoubleDetectIntegerValueNormally();
//        test.exampleAnalysis();
//        test.testPnlTable();
        //test.controllingObjectValuesForDetectUnique();
        //test.testDeleteSelectedMembersFunctionFromMatrixFunc();
        //test.deleteSelectedCols();
        //test.deleteSelectedMembers();
        //test.exampleCouldCategorical();
//        test.testCalculatingUniqueValues();
//        test.exampleCouldCategorical();
//        test.exampleEmptyData();
//        test.exampleCouldCategorical2(true);
//        test.testFireScrollBarButton();
//        test.testJPopupMenu();
//        test.testPnlVariety();
//        test.testDetectingInteger();
//        test.testShiftArrayToLeftFunction();
//        test.testConversions();
//        test.testPnlVarietyForButton();
//        test.fillEmptyCells();
//        test.testExchangeElementOnTheList();
//        test.testPnlVarietyForOrderingTheList();
//        test.testMatrixFunctionAddNewRows();
//        test.testCategoricalVariableClassWithNoCodingType();
//        test.testCategoricalVariableClassWithOrdinalCodingType();
//        test.testCategoricalVariableClassWithOneHotVectorCodingType();
//        test.testCategoricalVariableClassWithBinomialCodingType();
//        test.testPnlVarietyForText();
//        test.testDataSplitter();
//        test.testSortWithIndexes();
//        test.deleteSelectedMembers3();
//        test.testOutlierDetection();
        
        
        
    }
    public static void runTestsOrdered(){
        TestOrdered aa = new TestOrdered();
//        aa.test2DetectCategoricalColumns();
    }
}