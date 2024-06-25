package com.mycompany.project_java_3try;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author zeitu
 */
public class PrimaryController implements Initializable {

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("user_role");
    private EntityManager entityManager = entityManagerFactory.createEntityManager();
    private static byte attemptCount = 0;
    static private long startTime = 0;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private List<Egzemplarze> carList;
    private List<Klienci> customersList;
    
    @FXML
    private ChoiceBox<String> myChoiceBox;
    
    @FXML
    private Button rentButton, userLoginButton;

    @FXML
    private Label lb1, lb2, lb3, lb4, lb5, lb6, rentPrice, rentPriceNotification, incorrect, incorrectRegister, carWasRent, dateReturnCar;

    @FXML
    private TextField loginRegistrationField, loginField, nameField, surnameField;

    @FXML
    private PasswordField loginPassword, registrationPassword, registrationRepeatPassword;

    /**
     *
     */
    public static String login;

    
    //Próba zalogowania się przy użyciu loginu i hasła, w razie powodzenia przechodzi do okienka "Wypożyczalnia"

    /**
     *
     * @param event
     * @throws IOException
     */
    public void LoginAttempt(ActionEvent event) throws IOException {
        if (startTime + 10_000_000_000L < System.nanoTime()) {
            if (attemptCount == 3) {
                attemptCount = 0;
            }
            if (!(loginField.getText().isEmpty()) && !(loginPassword.getText().isEmpty())) {
                entityManager.getTransaction().begin();
                List<Klienci> log = entityManager.createNativeQuery("SELECT * FROM \"Klienci\" WHERE login='" + loginField.getText() + "' AND haslo='" + loginPassword.getText() + "'", Klienci.class).getResultList();
                try {
                    login = log.get(0).getLogin();
                    displayScene("Logged.fxml", event, "Wypozyczalnia");
                    attemptCount = 0;
                } catch (IndexOutOfBoundsException e) {
                    incorrect.setVisible(true);
                    incorrect.setText("Niepoprawne dane, pozostałe próby: " + (2 - attemptCount));
                    incorrect.setFont(new Font(12));
                    if (++attemptCount > 2) {
                        startTime = System.nanoTime();
                        incorrect.setText("Przekroczono ilość prób!");
                    }
                    System.out.println(attemptCount);
                }
                entityManager.getTransaction().commit();
            } else {
                incorrect.setVisible(true);
                incorrect.setText("Nie wypełniono wszystkich pól");
            }
        } else {
            incorrect.setText("Spróbuj ponownie za: " + ((startTime - System.nanoTime()) / 1_000_000_000L + 10) + "s");
            incorrect.setFont(new Font(12));
        }
    }

    //Metoda wywołująca się po kliknięciu w przycisk "Zarejestruj się"

    /**
     *
     * @param event
     * @throws IOException
     */
    public void switchToRegister(ActionEvent event) throws IOException {

        displayScene("Register.fxml", event, "Rejestracja");
    }
    
    //Metoda wywołująca się po kliknięciu w przycisk "Wróć do logowania" lub przycisk "Wyloguj"

    /**
     *
     * @param event
     * @throws IOException
     */
    public void switchToLogin(ActionEvent event) throws IOException {
        displayScene("Login.fxml", event, "Logowanie");
    }
    
    //Metoda wywoływana wewnątrz metody "registrationAttempt" sprawdzająca poprawność loginu
    private boolean loginValidate(String login) {
        if (login.isEmpty()) {
            return false;
        }
        for (byte i = 0; i < login.length(); i++) {
            if (!(((int) login.charAt(i) > 96 && (int) login.charAt(i) < 123) || (login.charAt(i) > 47 && login.charAt(i) < 58))) {
                return false;
            }
        }
        return true;
    }

    //Metoda wywoływana wewnątrz metody "registrationAttempt" sprawdzająca poprawność hasła
    private boolean passwordValidate(String password) {
        if (password.isEmpty()) {
            return false;
        }
        for (byte i = 0; i < password.length(); i++) {
            if (password.charAt(i) == 32) {
                return false;
            }
        }
        return true;
    }
    
    //Metoda wywoływana wewnątrz metody "registrationAttempt" sprawdzająca poprawność Imienia i Nazwiska
    private boolean nameAndSurnameValidate(String text) {
        if (text.isEmpty()) {
            return false;
        }
        for (byte i = 0; i < text.length(); i++) {
            if (!(((int) text.charAt(i) > 96 && (int) text.charAt(i) < 123) || (text.charAt(i) > 54 && text.charAt(i) < 91))) {
                return false;
            }
        }
        return true;
    }

    //Próba zarejestrowania nowego użytkownika

    /**
     *
     * @param event
     * @throws IOException
     */
    public void registrationAttempt(ActionEvent event) throws IOException {
        boolean flag = true;
        if (registrationPassword.getText().equals(registrationRepeatPassword.getText())) { //czy hasła są takie same
            if (nameAndSurnameValidate(nameField.getText()) && nameAndSurnameValidate(surnameField.getText())) { //walidacja imienia i nazwiska
                if (loginValidate(loginRegistrationField.getText())) { //walidacja loginu
                    if (passwordValidate(registrationPassword.getText())) { //walidacja hasła
                        entityManager.getTransaction().begin();
                        List<Klienci> logins = entityManager.createNativeQuery("SELECT * FROM \"Klienci\"", Klienci.class).getResultList();
                        for (byte i = 0; i < logins.size() && flag; i++) {
                            if (logins.get(i).getLogin().equals(loginRegistrationField.getText())) {
                                flag = false;
                            }
                        }
                        if (flag) {
                            System.out.print("tak");
                            entityManager.createNativeQuery("INSERT INTO \"Klienci\" VALUES ('" + nameField.getText() + "','" + surnameField.getText() + "','" + loginRegistrationField.getText() + "','" + registrationPassword.getText() + "')").executeUpdate();
                            displayScene("Login.fxml", event, "Rejestracja");
                        } else {
                            incorrectRegister.setText("Podany login juz istnieje");
                        }
                        entityManager.getTransaction().commit();
                    } else {
                        incorrectRegister.setText("Haslo nie moze miec\nspacji lub być puste");
                        incorrectRegister.setFont(new Font(12));
                    }
                } else {
                    incorrectRegister.setText("W polu login mogą być\ntylko małe litery i cyfry");
                    incorrectRegister.setFont(new Font(11));

                }
            } else {
                incorrectRegister.setText("W polu imie i nazwisko\n mogą być tylko litery");
                incorrectRegister.setFont(new Font(12));
            }
        } else {
            incorrectRegister.setText("Podane hasła nie są identyczne");
            incorrectRegister.setFont(new Font(15));
        }

    }
    
    /**
     *
     * @param url
     * @param rb
     */
    @Override
    //Metoda która odpowiada za wczytanie danych do Checkboxa i możliwość wyboru opcji
    public void initialize(URL url, ResourceBundle rb) {

        if (myChoiceBox != null) {

            entityManager.getTransaction().begin();

            carList = entityManager.createNativeQuery("select * from \"Egzemplarze\"", Egzemplarze.class).getResultList();

            //Łączenie 2 List które zostaną dodane do checkboxa
            List<String> markaList = carList.stream()
                    .map(Egzemplarze::getMarka)
                    .collect(Collectors.toList());
            List<String> modelList = carList.stream()
                    .map(Egzemplarze::getModel)
                    .collect(Collectors.toList());          
            List <String> combinedList = new ArrayList<>();
            for(int i=0; i<markaList.size();i++){
                combinedList.add(markaList.get(i)+ " " + modelList.get(i));
            }

            myChoiceBox.getItems().addAll(combinedList);
            myChoiceBox.getItems().add("Wybierz samochod");
            myChoiceBox.setValue("Wybierz samochod");
            myChoiceBox.setOnAction(this::checkboxChange);

            entityManager.getTransaction().commit();

        }

        if (userLoginButton != null) {
            userLoginButton.setText(login);

        }

    }

    //Metoda wyświetlająca dane po wybraniu opcji w checkboxie

    /**
     *
     * @param event
     */
    public void checkboxChange(ActionEvent event) {
        int myCar = myChoiceBox.getSelectionModel().selectedIndexProperty().get();
        LocalDate dataZwrotu = null;

        if (myCar == carList.toArray().length) {
            setButtonsVisibility(false, false);
            lb1.setText("");
            lb2.setText("");
            lb3.setText("");
            lb4.setText("");
            lb5.setText("");
            lb6.setText("");

        } else {
            Date date = carList.get(myCar).getDataProdukcji();
            lb1.setText(carList.get(myCar).getMarka());
            lb2.setText(carList.get(myCar).getModel());
            lb3.setText(carList.get(myCar).getRejestracja());
            lb4.setText(date.getDate() + "." + (date.getMonth() + 1) + "." + (date.getYear() + 1900));
            lb5.setText(carList.get(myCar).getKolor());
            lb6.setText(carList.get(myCar).getCena().toString());
            rentPrice.setText(carList.get(myCar).getCena().toString() + "0zł");
            setButtonsVisibility(true, false);
            
            entityManager.getTransaction().begin();
            
            LocalDate todayDate = LocalDate.now();

            try {
                List<Wypozyczenie> results = entityManager.createNativeQuery("select * from \"Wypozyczenia\" WHERE rejestracja='" + carList.get(myCar).getRejestracja() + "' ORDER BY data_zwrotu DESC", Wypozyczenie.class).getResultList();
                dataZwrotu = results.get(0).getDataZwrotu();
                System.out.println(results.get(0).getDataZwrotu().toString());

                if (dataZwrotu.isBefore(todayDate)) {
                    setButtonsVisibility(true, false);
                } else {
                    setButtonsVisibility(false, true);
                    dateReturnCar.setText("Data oddania: " + dataZwrotu.toString());
                }
                
                entityManager.getTransaction().commit();
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Uniwersalna metoda zmiany sceny (okienka)
    void displayScene(String sceneName, ActionEvent event, String sceneTitle) throws IOException {
        root = FXMLLoader.load(getClass().getResource(sceneName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(sceneTitle);
        stage.show();

    }

    //Metoda pokazująca i ukrywająca widoczność pól przy wyborze auta

    /**
     *
     * @param visibilityPrice
     * @param visibilityCar
     */
    public void setButtonsVisibility(boolean visibilityPrice, boolean visibilityCar) {
        rentPriceNotification.setVisible(visibilityPrice);
        rentPrice.setVisible(visibilityPrice);
        rentButton.setVisible(visibilityPrice);
        carWasRent.setVisible(visibilityCar);
        dateReturnCar.setVisible(visibilityCar);
    }

    //Metoda dodająca do bazy danych informacje o wypożyczeniu auta

    /**
     *
     * @param event
     * @throws IOException
     */
    public void rentCar(ActionEvent event) throws IOException {
        entityManager.getTransaction().begin();

        int myCar = myChoiceBox.getSelectionModel().selectedIndexProperty().get();
        customersList = entityManager.createNativeQuery("select * from \"Klienci\"WHERE login='" + login + "'", Klienci.class).getResultList();
        Klienci k = customersList.get(0);
        Egzemplarze egz = carList.get(myCar);
        LocalDate dataWypozyczenia = LocalDate.now();
        LocalDate dataZwrotu = dataWypozyczenia.plusDays(30);
        String insertQuery = "INSERT INTO \"Wypozyczenia\" (login, rejestracja, data_wypozyczenia, data_zwrotu) "
                + "VALUES (:login, :rejestracja, :dataWypozyczenia, :dataZwrotu)";

        entityManager.createNativeQuery(insertQuery)
                .setParameter("login", k.getLogin())
                .setParameter("rejestracja", egz.getRejestracja())
                .setParameter("dataWypozyczenia", dataWypozyczenia)
                .setParameter("dataZwrotu", dataZwrotu)
                .executeUpdate();
        
        entityManager.getTransaction().commit();

        setButtonsVisibility(false, true);
        dateReturnCar.setText("Data oddania: " + dataZwrotu);
    }

}
