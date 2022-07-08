/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.nyc;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.nyc.model.City;
import it.polito.tdp.nyc.model.CityDistance;
import it.polito.tdp.nyc.model.Model;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaLista"
    private Button btnCreaLista; // Value injected by FXMLLoader

    @FXML // fx:id="cmbProvider"
    private ComboBox<String> cmbProvider; // Value injected by FXMLLoader

    @FXML // fx:id="cmbQuartiere"
    private ComboBox<City> cmbQuartiere; // Value injected by FXMLLoader

    @FXML // fx:id="txtMemoria"
    private TextField txtMemoria; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML // fx:id="clQuartiere"
    private TableColumn<CityDistance, String> clQuartiere; // Value injected by FXMLLoader
 
    @FXML // fx:id="clDistanza"
    private TableColumn<CityDistance, Double> clDistanza; // Value injected by FXMLLoader
    
    @FXML // fx:id="tblQuartieri"
    private TableView<CityDistance> tblQuartieri; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	cmbQuartiere.getItems().clear();
    	
    	String p = cmbProvider.getValue();
    	
    	if(p == null) {
    		txtResult.appendText("SELEZIONARE UN PROVIDER!");
    		return;
    	}
    	
    	txtResult.appendText(model.creaGrafo(p));
    	
    	cmbQuartiere.getItems().addAll(model.getVertici());
    }

    @FXML
    void doQuartieriAdiacenti(ActionEvent event) {
    	
    	City c = cmbQuartiere.getValue();
    	
    	if(!model.isCreato()) {
    		txtResult.appendText("CREARE PRIMA IL GRAFO!");
    		return;
    	}
    	
    	if(c == null) {
    		txtResult.appendText("SELEZIONARE UN QUARTIERE");
    		return;
    	}
    	
    	List<CityDistance> adiacenti = model.getAdiacenti(c);
    	
    	tblQuartieri.setItems(FXCollections.observableArrayList(adiacenti));
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	if(!model.isCreato()) {
    		txtResult.appendText("CREARE PRIMA IL GRAFO!");
    		return;
    	}
    	try {
    		String n = txtMemoria.getText();
    		int N = Integer.parseInt(n);
    		City c = cmbQuartiere.getValue();
	    	if(c == null) {
	    		txtResult.appendText("SELEZIONARE UN QUARTIERE!");
	    	}
	    	model.Simula(c, N);
	    	List<Integer> revisionati = model.getRevisionati();
	    	int durata = model.getDurata();
	    	for(int i=0; i<revisionati.size();i++) {
	    		txtResult.appendText("Il tecnico " + i + " ha revisionato " + revisionati.get(i) + " hotspot\n");
	    	}
	    	txtResult.appendText("Durata simulazione: " + durata +" minuti");
    	}catch(NumberFormatException e){
    		txtResult.appendText("INSERIRE UN NUMERO VALIDO!");
    		return;
    	}
	    	
    	

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaLista != null : "fx:id=\"btnCreaLista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbProvider != null : "fx:id=\"cmbProvider\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbQuartiere != null : "fx:id=\"cmbQuartiere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMemoria != null : "fx:id=\"txtMemoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert clDistanza != null : "fx:id=\"clDistanza\" was not injected: check your FXML file 'Scene.fxml'.";
        assert clQuartiere != null : "fx:id=\"clQuartiere\" was not injected: check your FXML file 'Scene.fxml'.";
        
        clQuartiere.setCellValueFactory(new PropertyValueFactory<CityDistance, String>("c"));
        clDistanza.setCellValueFactory(new PropertyValueFactory<CityDistance, Double>("distance"));

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbProvider.getItems().addAll(model.getProviders());
    }

}
