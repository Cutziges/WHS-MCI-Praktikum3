
package praktikum3;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Optional;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import praktikum3.MaskField;

/**
 * FXML Controller class.
 *
 * @author Sarah Grugiel
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private ComboBox<String> farbe;
    @FXML
    private ComboBox<String> land;
    @FXML
    private ComboBox<String> region;
    @FXML
    private CheckBox alkoholfrei;
    @FXML
    private Spinner<String> alkohol;
    @FXML
    private TextArea beschreibung;
    @FXML
    private MaskField tfNummer;
    @FXML
    private TextField tfName;
    @FXML
    private MaskField tfJahrgang;
    @FXML
    private ComboBox<String> groessenwahl;
    @FXML
    private TextField tfFlaschenpreisNetto;
    @FXML
    private TextField tfFlaschenpreisBrutto;
    @FXML
    private Button bRunter;
    @FXML
    private Button bRauf;
    @FXML
    private Button bAbbrechen;
    @FXML
    private Button bSpeichern;
    @FXML
    private TextField tfLiterpreis;
    @FXML
    private Spinner sLagerdauer;
    @FXML
    private VBox diagram;
    @FXML
    private Rectangle rectPast;
    @FXML
    private Rectangle rectNow;
    @FXML
    private Rectangle rectFuture;
    @FXML
    private Rectangle rectTooEarly;
    @FXML
    private Rectangle rectRising;
    @FXML
    private Rectangle rectGood;
    @FXML
    private Rectangle rectDecline;
    @FXML
    private Label lbVintage;
    @FXML
    private Label lbDecline;
    @FXML
    private Label lbPast;
    @FXML
    private Label lbNow;
    
    /**
     * Abfragevariablen.
     */
    private boolean isBottleCalcLast = false;
    
    private boolean isLiterCalcLast = false;
    
    // tausender-Trenner und dezimal
    private static final String REGEX = "(\\d*,?\\d*)|(\\d{0,3}"
            + "(\\.\\d{3})*,?\\d*)";
    
    // Werte für Diagramm
    int jahrgang = 2018;
    int lagerdauer = 1;
    
    // Diagramm Variablen
    private static final double DIAGRAM_WIDTH = 0.98;
    private static final double DIAGRAM_HEIGHT = 0.5;

    private static final double TOO_EARLY_RATIO = 0.125;
    private static final double GOOD_RATIO = 0.5;
    private static final double RISING_RATIO = 1 - TOO_EARLY_RATIO - GOOD_RATIO;

    private static final String DURATION_EXCEPTION = 
            "Lagerdauer muss größer als 0 sein.";
    private static final String VINTAGE_EXCEPTION = 
            "Jahrgang kann nicht in der Zukunft liegen.";
    
    // Diagramm Stati
    private double tooEarly;
    private double rising;
    private double good;
    private double decline;

    // Diagramm Epochs
    private double past;
    private double now;
    private double future;
    
    // Diagramm Jahresangaben
    Calendar cal = new GregorianCalendar();
    private int aktuellesJahr = cal.get(Calendar.YEAR);
    
    // Liste mit gängigen Flaschengrößen
    ObservableList<String> groessenListe =
            FXCollections.observableArrayList(
                    "0,187 l", "0,25 l", "0,375 l", "0,5 l", "0,62 l", 
                    "0,7 l", "0,75 l", "0,8 l", "1 l", "1,5 l" 
            );
    
    // Liste mit Weinfarben
    ObservableList<String> farbListe = 
            FXCollections.observableArrayList(
                    "weiß", "rot", "rosé"
            );

    // Liste mit Weinanbauländern
    ObservableList<String> laenderListe = 
            FXCollections.observableArrayList(
                    "Argentinien", "Australien", "Österreich", "Brasilien",
                    "Bulgarien", "Chile", "China", "Deutschland", "England",
                    "Frankreich", "Georgien", "Griechenland", "Italien",
                    "Kanada", "Kroatien", "Mexiko", "Neuseeland", "Portugal",
                    "Schweiz", "Spanien", "Unbekannt"
            );
    
    // Listen mit den entsprechenden Regionen
    ObservableList<String> argentinienListe = 
            FXCollections.observableArrayList(
                    "Buenos Aires", "Catamarca", "Chubut", "Cordoba", 
                    "Entre Rios", "La Pampa", "La Rioja", "Mendoza",
                    "Misiones", "Neuquen", "Rio Negro", "Santiago del Estero",
                    "Salta", "San Juan", "San Luis", "Tucuman", "Unbekannt"
            );
    
    ObservableList<String> australienListe = 
            FXCollections.observableArrayList(
                    "Adelaide Hills", "Barossa Valley", "Beechworth", "Bendigo",
                    "Blackwood Valley", "Clare Valley", "Coonawarra", "Cowra",
                    "Derwent Valley", "Geographe", "Grampians", "Hunter Valley", 
                    "King Valley", "Mudgee", "North West", "Orange", 
                    "Peel", "Perth Hills", "Pipers River", "Riverina", 
                    "Riverland", "Southern", "Sunbury", "Swan Valley",
                    "Tamar Valley", "Unbekannt"
            );
        
    ObservableList<String> oesterreichListe = 
            FXCollections.observableArrayList(
                    "Carnuntum", "Eisenberg", "Kamptal", "Kremstal", 
                    "Leithaberg", "Mittelburgenland", "Neusiedlersee",
                    "Rosalia", "Südsteiermark", "Thermenregion", "Traisental",
                    "Vulkanland Steiermark", "Wachau", "Wagram", "Weinviertel",
                    "Weststeiermark", "Wien", "Unbekannt"
            );
        
    ObservableList<String> brasilienListe = 
            FXCollections.observableArrayList(
                    "Rio Grande do Sul", "Serra Gaucha", "Vale dos Vinhedos", 
                    "Unbekannt"
            );
        
    ObservableList<String> bulgarienListe = 
            FXCollections.observableArrayList(
                    "Donauebene", "Oberthrakische Tiefebene", "Rosental", 
                    "Struma-Tal", "Schwarzmeerregion", "Unbekannt"
            );
        
    ObservableList<String> chileListe = 
            FXCollections.observableArrayList(
                    "Valle de Casablanca", "Valle de Choapa", "Valle de Curico",
                    "Valle de Limari", "Valle de Rapel", "Valle de San Antonio",
                    "Valle del Aconcagua", "Valle del Bio-Bio", 
                    "Valle del Elqui", "Valle del Itata", "Valle del Maipo",
                    "Valle del Malleco", "Valle del Maule", "Unbekannt"
            );
        
    ObservableList<String> chinaListe = 
            FXCollections.observableArrayList(
                    "Bohai", "Gansus", "Gaochang", "Ningxias", "Tonghua",
                    "Yunnan-Guizhou-Plateau", "Zhangjiakou", "Unbekannt"
            );
        
    ObservableList<String> deutschlandListe = 
            FXCollections.observableArrayList(
                    "Ahr", "Baden", "Franken", "Hessische Bergstraße", 
                    "Mittelrhein", "Mosel", "Nahe", "Pfalz", "Rheingau", 
                    "Rheinhessen", "Saale-Unstrut", "Sachsen", "Würtemberg", 
                    "Unbekannt"
            );
        
    ObservableList<String> englandListe = 
            FXCollections.observableArrayList(
                    "East Anglia", "Mercia", "Southwest and Wales", 
                    "Thames and Chiltern", "Weald and Downland", "Wessex", 
                    "Unbekannt"
            );
        
    ObservableList<String> frankreichListe = 
            FXCollections.observableArrayList(
                    "Beaujolais", "Bordeaux", "Burgund", "Champagne", "Elsass",
                    "Jura", "Korsika", "Languedoc-Roussillon", "Loire", 
                    "Lothringen", "Provence", "Rhone", "Savoyen", "Südwesten", 
                    "Unbekannt"
            );
    
    // Liste mit Alkoholgehalt
    ObservableList<String> alkoholgehalt = FXCollections.observableArrayList (
                    "7,5 Vol%", "8,0 Vol%", "8,5 Vol%", "9,0 Vol%", "9,5 Vol%", 
                    "10,0 Vol%", "10,5 Vol%", "11,0 Vol%", "11,5 Vol%", 
                    "12,0 Vol%", "12,5 Vol%", "13,0 Vol%", "13,5 Vol%", 
                    "14,0 Vol%", "14,5 Vol%", "15,0  Vol%", "15,5 Vol%", 
                    "16,0 Vol%", "16,5 Vol%", "17,0 Vol%", "17,5 Vol%",
                    "18,0 Vol%", "18,5 Vol%", "19,0 Vol%", "19,5 Vol%",
                    "20,0 Vol%", "20,5 Vol%", "21,0 Vol%", "21,5 Vol%", 
                    "22,0 Vol%", "22,5 Vol%", "23,0 Vol%", "23,5 Vol%", 
                    "24,0 Vol%", "24,5 Vol%", "25,0 Vol%"
    );
    
    // Liste mit Lagerdauer
    ObservableList<String> angabeLagerdauer = FXCollections.observableArrayList (
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11",
            "12", "13", "14", "15", "16", "17", "18", "19", "20", "21",
            "22", "23", "24", "25", "26", "27", "28", "29", "30", "31",
            "32", "33", "34", "35", "36", "37", "38", "39", "40"
    );
    
    /**
     * Initializes the controller class.
     * @param url URL
     * @param rb ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        farbe.setItems(farbListe);
        groessenwahl.setItems(groessenListe);
        groessenwahl.setValue("0,75 l");
        land.setItems(laenderListe);
        region.setDisable(true);
        tfLiterpreis.setAlignment(Pos.CENTER_RIGHT);
        tfFlaschenpreisNetto.setAlignment(Pos.CENTER_RIGHT);
        tfFlaschenpreisBrutto.setAlignment(Pos.CENTER_RIGHT);
        
        SpinnerValueFactory<String> alkoholSpinner =
               new SpinnerValueFactory.
                       ListSpinnerValueFactory<String>(alkoholgehalt);
        alkoholSpinner.setValue("7,5 Vol%");
        alkohol.setValueFactory(alkoholSpinner);
        
        SpinnerValueFactory<String> lagerdauerSpinner =
               new SpinnerValueFactory.
                       ListSpinnerValueFactory<String>(angabeLagerdauer);
        lagerdauerSpinner.setValue("1");
        sLagerdauer.setValueFactory(lagerdauerSpinner);
        
        // Wenn Land gewählt wird, werden entsprechende Regionen freigeschaltet
        land.valueProperty().addListener((ov, oldV, newV) -> {
            if (land.getValue() == "Argentinien") {
                region.setDisable(false);
                region.setItems(argentinienListe);
            } else if (land.getValue() == "Australien") {
                region.setDisable(false);
                region.setItems(australienListe);
            } else if (land.getValue() == "Österreich") {
                region.setDisable(false);
                region.setItems(oesterreichListe);
            } else if (land.getValue() == "Brasilien") {
                region.setDisable(false);
                region.setItems(brasilienListe);
            } else if (land.getValue() == "Bulgarien") {
                region.setDisable(false);
                region.setItems(bulgarienListe);
            } else if (land.getValue() == "Chile") {
                region.setDisable(false);
                region.setItems(chileListe);
            } else if (land.getValue() == "China") {
                region.setDisable(false);
                region.setItems(chinaListe);
            } else if (land.getValue() == "Deutschland") {
                region.setDisable(false);
                region.setItems(deutschlandListe);
            } else if (land.getValue() == "England") {
                region.setDisable(false);
                region.setItems(englandListe);
            } else if (land.getValue() == "Frankreich") {
                region.setDisable(false);
                region.setItems(frankreichListe);
            }
        });
        
        // Prüfung, ob Jahrgang in Zukunft liegt
        tfJahrgang.textProperty().addListener((ov, oldV, newV) -> {
            if (!newV.contains("_")) {
                if (Integer.parseInt(newV) > aktuellesJahr) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Eingabefehler");
                    alert.setHeaderText(null);
                    alert.setContentText("Jahrgang liegt in der Zukunft.");
                    alert.showAndWait();
                    
                    tfJahrgang.requestFocus();
                    tfJahrgang.setText(oldV);
                    tfJahrgang.selectAll();
                } if (Integer.parseInt(newV) < 1650) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Eingabefehler");
                    alert.setHeaderText(null);
                    alert.setContentText("Jahrgang liegt zu weit zurück.");
                    alert.showAndWait();
                    
                    tfJahrgang.requestFocus();
                    tfJahrgang.setText(oldV);
                    tfJahrgang.selectAll();
                } else {
                    jahrgang = Integer.parseInt(newV);
                    showDiagram(jahrgang, lagerdauer, aktuellesJahr);
                }
            }
        });
        
        // Erstellung des Diagramms mit Standardwerten
        showDiagram(jahrgang, lagerdauer, aktuellesJahr);
        
        
        // Großbuchstaben für Bestellnummer
        tfNummer.textProperty().addListener((ov, oldV, newV) -> {
            tfNummer.setText(newV.toUpperCase());
        });
        
        
        // Listener für Flaschenpreise
        tfFlaschenpreisNetto.textProperty().addListener((ov, oldV, newV) -> {
            if (tfFlaschenpreisNetto.isFocused()) {
                tfFlaschenpreisBrutto.setText("");
                tfLiterpreis.setText("");
                isBottleCalcLast = false;
                isLiterCalcLast = false;
            } if (tfFlaschenpreisNetto.isFocused()
                    && tfFlaschenpreisNetto.getText().isEmpty()) {
                tfLiterpreis.setText("");
                isBottleCalcLast = false;
                isLiterCalcLast = false;
            }    
        });
        
        tfFlaschenpreisNetto.focusedProperty().addListener((ov, oldV, newV) -> {
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);
            
            if (!tfFlaschenpreisNetto.getText().isEmpty()
                    && tfFlaschenpreisBrutto.getText().isEmpty()) {
                try {
                    double dPrice = nf.parse(tfFlaschenpreisNetto.getText())
                            .doubleValue();
                    double newPrice = (dPrice) * 1.19;
                    tfFlaschenpreisBrutto.setText(nf.format(newPrice));
                    isBottleCalcLast = false;
                    isLiterCalcLast = true;
                } catch (java.text.ParseException ex) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Eingabefehler");
                    alert.setHeaderText(null);
                    alert.setContentText("Bitte geben Sie den netto "
                            + "Flaschenpreis im Format x,xx ein.");
                    alert.showAndWait();

                    tfFlaschenpreisNetto.selectAll();
                    tfFlaschenpreisNetto.requestFocus();
                }
            }
        });
        
        tfFlaschenpreisBrutto.focusedProperty().addListener((ov, oldV, newV) -> {
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);
            
            if (!tfFlaschenpreisBrutto.getText().isEmpty()
                    && tfFlaschenpreisNetto.getText().isEmpty()) {
                try {
                    double dPrice = nf.parse(tfFlaschenpreisBrutto.getText())
                            .doubleValue();
                    double newPrice = (dPrice) / 1.19;
                    tfFlaschenpreisNetto.setText(nf.format(newPrice));
                    isBottleCalcLast = false;
                    isLiterCalcLast = true;
                } catch (java.text.ParseException ex) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Eingabefehler");
                    alert.setHeaderText(null);
                    alert.setContentText("Bitte geben Sie den brutto "
                            + "Flaschenpreis im Format x,xx ein.");
                    alert.showAndWait();

                    tfFlaschenpreisBrutto.selectAll();
                    tfFlaschenpreisBrutto.requestFocus();
                }
            }
        });
        
        tfFlaschenpreisBrutto.textProperty().addListener((ov, oldV, newV) -> {
            if (tfFlaschenpreisBrutto.isFocused()) {
                tfFlaschenpreisNetto.setText("");
                tfLiterpreis.setText("");
                isBottleCalcLast = false;
                isLiterCalcLast = false;
            } if (tfFlaschenpreisBrutto.isFocused()
                    && tfFlaschenpreisBrutto.getText().isEmpty()) {
                tfLiterpreis.setText("");
                isBottleCalcLast = false;
                isLiterCalcLast = false;
            }    
        });
        
        
        // Listener für Literpreis
        tfLiterpreis.textProperty().addListener((ov, oldV, newV) -> {
            if (tfLiterpreis.isFocused()) {
                tfFlaschenpreisNetto.setText("");
                tfFlaschenpreisBrutto.setText("");
                isBottleCalcLast = false;
                isLiterCalcLast = false;
            }
        });
        
        // Disablen des Alkohol Spinners falls alkoholfrei gewählt wurde
        alkoholfrei.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed (ObservableValue ov,Boolean old_val, Boolean new_val) {
                if (alkoholfrei.isSelected()) {
                    alkohol.setDisable(true);
                } else {
                    alkohol.setDisable(false);
                }
            }
        });
        
        // Übergabe der Lagerdauer an Diagramm
        sLagerdauer.valueProperty().addListener((ov, oldV, newV) -> {
            String s = sLagerdauer.getValue().toString();
            lagerdauer = Integer.parseInt(s);
            showDiagram(jahrgang, lagerdauer, aktuellesJahr);
        });
        
    }

    /**
     * ComboBox Action.
     * @param event Auswahl
     */
    @FXML
    private void boxAction(ActionEvent event) {
        if (isBottleCalcLast
                && !tfFlaschenpreisBrutto.getText().isEmpty()
                && !tfFlaschenpreisNetto.getText().isEmpty()) {
            berechneLiterpreis();
        }
        if (isLiterCalcLast) {
            berechneFlaschenpreisNetto();
            berechneFlaschenpreisBrutto();
        }
    }

    /**
     * Aktion beim Klicken des runter Buttons.
     * @param event Event
     */
    @FXML
    private void bRunterAction(ActionEvent event) {
        berechneLiterpreis();   
    }
    
    /**
     * Aktion beim Klicken des rauf Buttons.
     * @param event Event
     */
    @FXML
    private void bRaufAction(ActionEvent event) {
        tfFlaschenpreisNetto.setDisable(false);
        tfFlaschenpreisBrutto.setDisable(false);
        berechneFlaschenpreisNetto();
        berechneFlaschenpreisBrutto();
    }
     
    
    /**
     * Methode zur berechnung des brutto Literpreises.
     */
    private void berechneLiterpreis() {
        
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        
        if (!tfFlaschenpreisNetto.getText().matches(REGEX)) {
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eingabefehler");
            alert.setHeaderText(null);
            alert.setContentText("Bitte geben Sie den netto Flaschenpreis im "
                    + "Format x,xx ein.");
            alert.showAndWait();
            
            tfFlaschenpreisNetto.selectAll();
            tfFlaschenpreisNetto.requestFocus();
        } else if (!tfFlaschenpreisBrutto.getText().matches(REGEX)) {
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eingabefehler");
            alert.setHeaderText(null);
            alert.setContentText("Bitte geben Sie den brutto Flaschenpreis im "
                    + "Format x,xx ein.");
            alert.showAndWait();
            
            tfFlaschenpreisBrutto.selectAll();
            tfFlaschenpreisBrutto.requestFocus();
            
        } else if (tfFlaschenpreisNetto.getText().matches(REGEX)) {
            try {
                
                double dSize = nf.parse(groessenwahl.getValue())
                        .doubleValue();
                double dPrice = nf.parse(tfFlaschenpreisNetto.getText())
                        .doubleValue();
                double newPrice = (dPrice / dSize) * 1.19;
                tfLiterpreis.setText(nf.format(newPrice));
                
                isBottleCalcLast = true;
                isLiterCalcLast = false;
            
            } catch (java.text.ParseException ex) {
                
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Eingabefehler");
                alert.setHeaderText(null);
                alert.setContentText("Bitte geben Sie den netto Flaschenpreis"
                        + " im Format x,xx ein.");
                alert.showAndWait();
                
                tfFlaschenpreisNetto.selectAll();
                tfFlaschenpreisNetto.requestFocus();
            }
            
        } else if (tfFlaschenpreisBrutto.getText().matches(REGEX)) {
            try {
                
                double dSize = nf.parse(groessenwahl.getValue())
                        .doubleValue();
                double dPrice = nf.parse(tfFlaschenpreisBrutto.getText())
                        .doubleValue();
                double newPrice = (dPrice / dSize);
                tfLiterpreis.setText(nf.format(newPrice));
                
                isBottleCalcLast = true;
                isLiterCalcLast = false;
            
            } catch (java.text.ParseException ex) {
                
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Eingabefehler");
                alert.setHeaderText(null);
                alert.setContentText("Bitte geben Sie den brutto Flaschenpreis"
                        + " im Format x,xx ein.");
                alert.showAndWait();
                
                tfFlaschenpreisBrutto.selectAll();
                tfFlaschenpreisBrutto.requestFocus();
            }
        }
    }
    
    /**
     * Methode zur Berechnung des netto Flaschenpreises.
     */
    private void berechneFlaschenpreisNetto() {
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        
        if (!tfLiterpreis.getText().matches(REGEX)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eingabefehler");
            alert.setHeaderText(null);
            alert.setContentText("Bitte geben Sie den Literpreis im "
                    + "Format x,xx ein.");
            alert.showAndWait();
            
            tfLiterpreis.selectAll();
            tfLiterpreis.requestFocus();
        } else {
            try {
                double dSize = nf.parse(groessenwahl.getValue()).doubleValue();
                double dPrice = nf.parse(tfLiterpreis.getText()).doubleValue();
                double newPrice = (dPrice * dSize) / 1.19;
                tfFlaschenpreisNetto.setText(nf.format(newPrice));
                tfFlaschenpreisNetto.setAlignment(Pos.CENTER_RIGHT);
                isBottleCalcLast = false;
                isLiterCalcLast = true;

            } catch (java.text.ParseException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Eingabefehler");
                alert.setHeaderText(null);
                alert.setContentText("Bitte geben Sie den Literpreis im "
                    + "Format x,xx ein.");
                alert.showAndWait();
                
                tfLiterpreis.selectAll();
                tfLiterpreis.requestFocus();
            }
        }
    }
    
    /**
     * Methode zur Berechnung des brutto Flaschenpreises.
     */
    private void berechneFlaschenpreisBrutto() {
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        
        if (!tfLiterpreis.getText().matches(REGEX)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eingabefehler");
            alert.setHeaderText(null);
            alert.setContentText("Bitte geben Sie den Literpreis im "
                    + "Format x,xx ein.");
            alert.showAndWait();
            
            tfLiterpreis.selectAll();
            tfLiterpreis.requestFocus();
        } else {
            try {
                double dSize = nf.parse(groessenwahl.getValue()).doubleValue();
                double dPrice = nf.parse(tfLiterpreis.getText()).doubleValue();
                double newPrice = (dPrice * dSize);
                tfFlaschenpreisBrutto.setText(nf.format(newPrice));
                tfFlaschenpreisBrutto.setAlignment(Pos.CENTER_RIGHT);
                isBottleCalcLast = false;
                isLiterCalcLast = true;

            } catch (java.text.ParseException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Eingabefehler");
                alert.setHeaderText(null);
                alert.setContentText("Bitte geben Sie den Literpreis im "
                    + "Format x,xx ein.");
                alert.showAndWait();
                
                tfLiterpreis.selectAll();
                tfLiterpreis.requestFocus();
            }
        }
    }
    
    /**
     * Methode zur Anzeige des Diagramms.
     * @param vintage Jahrgang
     * @param duration Lagerdauer
     * @param presentYear aktuelles Jahr
     */
    private void showDiagram(int vintage, int duration, int presentYear) {
        if (duration < 1) {
            throw new IllegalArgumentException(DURATION_EXCEPTION);
        }
        if (presentYear < vintage) {
            throw new IllegalArgumentException(VINTAGE_EXCEPTION);
        }
        computeStates(vintage, duration, presentYear);
        bindStates(vintage, duration);
        computeEpochs(vintage, duration, presentYear);
        bindEpochs(presentYear);
    }

    private void computeStates(int vintage, int duration, int presentYear) {
        double yearsInStock = duration + 1;
        double yearsTotal = duration + 2;
        this.tooEarly = TOO_EARLY_RATIO * yearsInStock / yearsTotal;
        this.rising = RISING_RATIO * yearsInStock / yearsTotal;
        this.good = GOOD_RATIO * yearsInStock / yearsTotal;
        this.decline = 1 / yearsTotal;
    }

    private void bindStates(int vintage, int duration) {
        rectTooEarly.widthProperty().bind(diagram.widthProperty()
                .multiply(DIAGRAM_WIDTH * this.tooEarly));
        rectTooEarly.heightProperty().bind(diagram.heightProperty()
                .multiply(DIAGRAM_HEIGHT));
        rectRising.widthProperty().bind(diagram.widthProperty()
                .multiply(DIAGRAM_WIDTH * this.rising));
        rectRising.heightProperty().bind(diagram.heightProperty()
                .multiply(DIAGRAM_HEIGHT));
        rectGood.widthProperty().bind(diagram.widthProperty()
                .multiply(DIAGRAM_WIDTH * this.good));
        rectGood.heightProperty().bind(diagram.heightProperty()
                .multiply(DIAGRAM_HEIGHT));
        rectDecline.widthProperty().bind(diagram.widthProperty()
                .multiply(DIAGRAM_WIDTH * this.decline));
        rectDecline.heightProperty().bind(diagram.heightProperty()
                .multiply(DIAGRAM_HEIGHT));
        lbVintage.setText("" + vintage);
        lbVintage.prefWidthProperty().bind(diagram.widthProperty()
                .multiply(DIAGRAM_WIDTH * (this.tooEarly 
                        + this.rising + this.good)));
        lbVintage.minWidthProperty().bind(diagram.widthProperty()
                .multiply(DIAGRAM_WIDTH * (this.tooEarly 
                        + this.rising + this.good)));
        lbDecline.setText("" + (vintage + duration + 1));
    }

    private void computeEpochs(int vintage, int duration, int presentYear) {
        double yearsInStock = duration + 1;
        double yearsTotal = duration + 2;
        if (presentYear == vintage) {
            this.past = 0.0;
            this.now = 1 / yearsTotal;
        } else if (presentYear > vintage + yearsInStock) {
            this.past = 1.0;
            this.now = 0.0;
        } else {
            this.past = (presentYear - vintage) * 1 / (yearsTotal);
            this.now = 1 / yearsTotal;
        }
        this.future = 1 - this.past - this.now;
    }

    private void bindEpochs(int presentYear) {
        rectPast.widthProperty().bind(diagram.widthProperty()
                .multiply(DIAGRAM_WIDTH * past));
        rectPast.heightProperty().bind(diagram.heightProperty()
                .multiply(DIAGRAM_HEIGHT));
        rectNow.widthProperty().bind(diagram.widthProperty()
                .multiply(DIAGRAM_WIDTH * now));
        rectNow.heightProperty().bind(diagram.heightProperty()
                .multiply(DIAGRAM_HEIGHT));
        rectFuture.widthProperty().bind(diagram.widthProperty()
                .multiply(DIAGRAM_WIDTH * future));
        rectFuture.heightProperty().bind(diagram.heightProperty()
                .multiply(DIAGRAM_HEIGHT));
        lbPast.prefWidthProperty().bind(diagram.widthProperty()
                .multiply(DIAGRAM_WIDTH * past));
        lbPast.minWidthProperty().bind(diagram.widthProperty()
                .multiply(DIAGRAM_WIDTH * past));
        if (now > 0.0) {
            lbNow.setText("" + presentYear);
        }
    }
    
    // Button Aktionen
    
    /**
     * Aktion beim Klicken des abbrechen Buttons.
     * @param event Event
     */
    @FXML
    private void bAbbrechenAction(ActionEvent event) {
        Stage stage = (Stage) bAbbrechen.getScene().getWindow();
        
        if (!tfNummer.getText().matches("2018-")
                || !tfName.getText().isEmpty()
                || !tfJahrgang.getText().matches("____")
                || farbe.getValue() != null
                || land.getValue() != null
                || (alkohol.isDisabled() && alkoholfrei.isSelected())
                || alkohol.getValue() != "7,5 Vol%"
                || !beschreibung.getText().isEmpty()
                || !"0,75 l".equals(groessenwahl.getValue())
                || (!tfFlaschenpreisNetto.getText().isEmpty() 
                || !tfFlaschenpreisBrutto.getText().isEmpty())
                || !tfLiterpreis.getText().isEmpty()
                || sLagerdauer.getValue() != "1") {
            
            // Abfragen, ob Eingaben verworfen werden sollen
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eingaben gefunden");
            alert.setResizable(false);
            alert.setHeaderText(null);
            alert.setContentText("Es wurden Eingaben vorgenommen. \n"
                    + "Wollen Sie die Anwendung dennoch schließen?");

            Optional<ButtonType> result = alert.showAndWait();
            ButtonType button = result.orElse(ButtonType.CANCEL);

            if (button == ButtonType.OK) {
                // Schließen des Fensters
                stage.close();
            } else if (button == ButtonType.CANCEL) {
                stage.show();
            }
        
        } else { 
            stage.close();
        }
    }
    
    
    /**
     * Aktion beim Klicken des abbrechen Buttons.
     * @param event Event
     */
    @FXML
    private void bSpeichernAction(ActionEvent event) {
        Stage stage = (Stage) bSpeichern.getScene().getWindow();
        
        if ("2018-".equals(tfNummer.getText())
                || tfNummer.getText().contains("_")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Keine Bestellnummer eingegeben");
            alert.setHeaderText(null);
            alert.setContentText("Bitte geben Sie die Bestellnummer"
                    + " des Weins ein.");
            alert.showAndWait();
            tfNummer.requestFocus();
            
        } else if (tfName.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Keinen Namen eingegeben");
            alert.setHeaderText(null);
            alert.setContentText("Bitte geben Sie den Namen des Weins ein.");
            alert.showAndWait();
            tfName.requestFocus();
            
        } else if (tfJahrgang.getText().matches("____")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Keinen Jahrgang angegeben");
            alert.setHeaderText(null);
            alert.setContentText("Bitte geben Sie den Jahrgang des Weins an.");
            alert.showAndWait();
            tfJahrgang.requestFocus();
            
        } else if (tfJahrgang.getText().contains("_")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Keinen gültigen Jahrgang angegeben");
            alert.setHeaderText(null);
            alert.setContentText("Bitte geben Sie den vollständigen "
                    + "Jahrgang des Weins im Format JJJJ ein.");
            alert.showAndWait();
            tfJahrgang.requestFocus();
            
        } else if (farbe.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Keine Farbe gewählt");
            alert.setHeaderText(null);
            alert.setContentText("Bitte wählen Sie die Farbe des Weins aus.");
            alert.showAndWait();
            farbe.requestFocus();
            
        } else if (land.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Kein Land gewählt");
            alert.setHeaderText(null);
            alert.setContentText("Bitte wählen Sie das Land des Weins aus.");
            alert.showAndWait();
            land.requestFocus();
            
        } else if (region.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Keine Region gewählt");
            alert.setHeaderText(null);
            alert.setContentText("Bitte wählen Sie die Region des Weines aus.");
            alert.showAndWait();
            region.requestFocus();
            
        } else if (tfFlaschenpreisNetto.getText().isEmpty()
                && tfFlaschenpreisBrutto.getText().isEmpty()
                && tfLiterpreis.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Keinen Flaschenpreis gefunden");
            alert.setHeaderText(null);
            alert.setContentText("Bitte geben Sie den den Preis des Weines "
                    + "an.");
            alert.showAndWait();
            tfFlaschenpreisNetto.requestFocus();
        /*
        } else if (tfFlaschenpreisNetto.getText().isEmpty()
                && tfFlaschenpreisBrutto.isDisabled()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Keinen Flaschenpreis gefunden");
            alert.setHeaderText(null);
            alert.setContentText("Bitte geben Sie den netto Flaschenpreis"
                    + " des Weins an.");
            alert.showAndWait();
            tfFlaschenpreisNetto.requestFocus();
        
        } else if (tfFlaschenpreisBrutto.getText().isEmpty()
                && tfFlaschenpreisNetto.isDisabled()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Keinen Flaschenpreis gefunden");
            alert.setHeaderText(null);
            alert.setContentText("Bitte geben Sie den brutto Flaschenpreis"
                    + " des Weins an.");
            alert.showAndWait();
            tfFlaschenpreisBrutto.requestFocus();
            
        } else if (tfLiterpreis.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Keinen Literpreis gefunden");
            alert.setHeaderText(null);
            alert.setContentText("Bitte geben Sie den Literpreis"
                    + " des Weins an.");
            alert.showAndWait();
            tfLiterpreis.requestFocus();
            */
        } else if (tfJahrgang.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Keinen Jahrgang angegeben");
            alert.setHeaderText(null);
            alert.setContentText("Bitte geben Sie den Jahrgang"
                    + " des Weins an.");
            alert.showAndWait();
            tfJahrgang.requestFocus();
            
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Bestätigung");
            alert.setHeaderText(null);
            alert.setContentText("Der Wein wurde erfolgreich gespeichert.");

            Optional<ButtonType> result = alert.showAndWait();
            ButtonType button = result.orElse(ButtonType.CANCEL);

            if (button == ButtonType.OK) {
                // Ausgabe der Informationen
                System.out.printf("Der folgende Wein wurde soeben gespeichert: \n");
                System.out.printf("Bestellnummer: %s \n", tfNummer.getText());
                System.out.printf("Name des Weins: %s \n", tfName.getText());
                System.out.printf("Jahrgang: %s \n", tfJahrgang.getText());
                System.out.printf("Farbe: %s \n", farbe.getValue());
                System.out.printf("Land: %s \n", land.getValue());
                System.out.printf("Region: %s \n", region.getValue());
                
                if (alkohol.isDisabled()) {
                    System.out.printf("Alkoholgehalt: alkoholfrei \n");
                } else {
                    System.out.printf("Alkoholgehalt: %s \n", 
                            alkohol.getValue());
                }
                
                System.out.printf("Beschreibung: %s \n", 
                        beschreibung.getText());
                System.out.printf("Flaschengröße: %s \n", 
                        groessenwahl.getValue());
                
                if (tfFlaschenpreisBrutto.isDisabled()) {
                    System.out.printf("Flaschenpreis (netto): %s \n", 
                            tfFlaschenpreisNetto.getText());
                } else if (tfFlaschenpreisNetto.isDisabled()) {
                    System.out.printf("Flaschenpreis (brutto): %s \n", 
                            tfFlaschenpreisBrutto.getText());
                }
                if(!tfLiterpreis.getText().isEmpty()) {
                   System.out.printf("Literpreis (brutto): %s \n", 
                           tfLiterpreis.getText()); 
                }
                System.out.printf("Lagerdauer: %d \n", lagerdauer);
            
                // Entfernen der Eingaben
                tfNummer.clear();
                tfNummer.setMask("2018-AAA-DDDD");
                tfName.selectAll();
                tfName.setText(null);
                tfName.clear();
                tfJahrgang.clear();
                tfJahrgang.setMask("DDDD");
                farbe.getSelectionModel().clearSelection();
                land.getSelectionModel().clearSelection();
                region.getSelectionModel().clearSelection();
                alkoholfrei.setSelected(false);
                SpinnerValueFactory<String> alkoholSpinner =
                        new SpinnerValueFactory.
                       ListSpinnerValueFactory<String>(alkoholgehalt);
                alkoholSpinner.setValue("7,5 Vol%");
                alkohol.setValueFactory(alkoholSpinner);
                beschreibung.clear();
                groessenwahl.setValue("0,75 l");
                tfFlaschenpreisNetto.clear();
                tfFlaschenpreisBrutto.clear();
                tfLiterpreis.clear();
                SpinnerValueFactory<String> lagerdauerSpinner =
                        new SpinnerValueFactory.
                                ListSpinnerValueFactory<String>(angabeLagerdauer);
                lagerdauerSpinner.setValue("1");
                sLagerdauer.setValueFactory(lagerdauerSpinner);
            }
        }
    }
  
}