/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gwopennlp;

import gwopennlp.OpenNlp.Nlp;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;



public class FXMLDocumentController implements Initializable {
    
    @FXML
    TextField txtFrase; 
    
    @FXML
    private Label label;
    
    @FXML
    private PieChart pieChart;
        
     @FXML
    private ProgressIndicator barProgreso;

    @FXML
    private Label lbnBarProgreso;
    
    @FXML
    private Button btnFileLoad;
    //
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        String sentenceIn = txtFrase.getText(); 
        Nlp ClaseProcesamientoTexto = new Nlp(); 
        String sentenceOut = ClaseProcesamientoTexto.validar(sentenceIn); 

        ObservableList<PieChart.Data> pierChartData = FXCollections.observableArrayList(
                new PieChart.Data(sentenceOut, 100)
        );
        pieChart.setData(pierChartData);
        pieChart.setLegendVisible(true);
        pieChart.setStartAngle(90);
    }
    
     @FXML
    void btnFileLoadClick(ActionEvent event) throws IOException, InterruptedException {
         barProgreso.visibleProperty().setValue(true);
        lbnBarProgreso.visibleProperty().set(true);
      
//        Thread circuloEspera = new LaPrueba(); 
//        circuloEspera.start();
        Nlp ClaseProcesamientoTexto = new Nlp(); 
        String resultPorcentage = ""; 
         List<String> envirLista =  ClaseProcesamientoTexto.leerCsv();
        resultPorcentage =  ClaseProcesamientoTexto.contarTiposVoz(envirLista);
        // Se splitea para obtener los resultados 
         String[] result = resultPorcentage.split(",");
          ObservableList<PieChart.Data> pierChartData = FXCollections.observableArrayList();
         for (int i = 0; i < result.length; i++) {
             pierChartData.add( new PieChart.Data(result[i], Integer.parseInt(result[i+1])) ); 
             i++; 
         }
        pieChart.setData(pierChartData);
        pieChart.setLegendVisible(true);
        pieChart.setStartAngle(90);
        barProgreso.visibleProperty().set(true);
        lbnBarProgreso.visibleProperty().set(true);
        
    }
//    public void segimosProbando(){
//         barProgreso.visibleProperty().setValue(true);
//        lbnBarProgreso.visibleProperty().set(true);
//    }
//      public  class LaPrueba  extends  Thread{
//
//        public LaPrueba() {
//           
//        }
//
//        @Override
//        public void run() {
//             segimosProbando();
//        }        
//    }

      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    
    
}
