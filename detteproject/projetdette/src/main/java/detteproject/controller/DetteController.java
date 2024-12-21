package detteproject.controller;

import java.util.List;
import detteproject.Repository.jpa.RepositorieJpaArticle;
import detteproject.Repository.jpa.RepositorieJpaDette;
import detteproject.Repository.jpa.RepositoryJpaClient;
import detteproject.State.EtatDette;
import detteproject.State.StateDette;
import detteproject.core.Factory.Impl.FactoryRepo;
import detteproject.core.Factory.Impl.FactoryService;
import detteproject.data.entities.Article;
import detteproject.data.entities.Client;
import detteproject.data.entities.DetailDette;
import detteproject.data.entities.Dette;
import detteproject.data.entities.User;
import detteproject.services.ArticleService;
import detteproject.services.ClientService;
import detteproject.services.DetteService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.util.StringConverter;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class DetteController extends BoutiquierController {

    @FXML
    private TableColumn<DetailDette, Void> updateColumn;

    @FXML
    private ComboBox<Article> articleComboBox; // Pour sélectionner un article
    @FXML
    private TextField quantityTextField; // Pour saisir la quantité
    @FXML
    private TableView<DetailDette> detailsTableView; // Tableau pour afficher les détails de la dette
    @FXML
    private Button addDetailButton; // Bouton pour ajouter un article
    @FXML
    private Button saveDebtButton; // Bouton pour enregistrer la dette

    @FXML
    private TextField montantTextField;

    @FXML
    private TableColumn<DetailDette, String> articleColumn; // TableColumn pour afficher le nom de l'article
    @FXML
    private TableColumn<DetailDette, Double> quantityColumn;

    @FXML
    private TableColumn<DetailDette, Void> actionColumn;

    private DetteService detteService;
    private ClientService clientService;
    private ArticleService articleService;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField addresseTextField;

    private ObservableList<DetailDette> detailsList = FXCollections.observableArrayList();
    private Dette currentDette = new Dette();
    double montant = 0;

    @FXML
    public void initialize() {

        montantTextField.setText(0 + "");
        FactoryRepo<Dette> detteFactoryRepo = new FactoryRepo<>(Dette.class);
        RepositorieJpaDette detteRepository = (RepositorieJpaDette) detteFactoryRepo.createRepository();
        FactoryRepo<Client> clientFactoryRepo = new FactoryRepo<>(Client.class);
        RepositoryJpaClient clientRepository = (RepositoryJpaClient) clientFactoryRepo.createRepository();

        FactoryService<Dette> factory = new FactoryService(Dette.class, detteRepository);
        FactoryService<Client> factory1 = new FactoryService(Client.class, clientRepository);
        detteService = (DetteService) factory.createService();
        clientService = (ClientService) factory1.createService();

        FactoryRepo<Article> articleFactory = new FactoryRepo<>(Article.class);
        RepositorieJpaArticle articleRepository = (RepositorieJpaArticle) articleFactory.createRepository();
        FactoryService<Article> factoryService = new FactoryService<>(Article.class, articleRepository);
        articleService = (ArticleService) factoryService.createService();
        // Remplir les ComboBoxes avec les clients et les articles
        addActionColumn();
        addUpdateColumn();

        articleComboBox.setItems(FXCollections.observableArrayList(getArticles()));
        articleComboBox.setConverter(new StringConverter<Article>() {
            @Override
            public String toString(Article article) {
                return article != null ? article.getLibelle() : ""; // Affiche le nom du client
            }

            @Override
            public Article fromString(String string) {
                return null; // Vous n'avez pas besoin d'implémenter cela si vous ne l'utilisez pas
            }
        });

        // Configurez les colonnes du tableau

        articleColumn
                .setCellValueFactory(
                        cellData -> new SimpleStringProperty(cellData.getValue().getArticle().getLibelle()));

        quantityColumn.setCellValueFactory(
                cellData -> new SimpleDoubleProperty(cellData.getValue().getQte()).asObject());

        detailsTableView.setItems(detailsList);

        // Événement pour ajouter un article
        addDetailButton.setOnAction(event -> addDetail());
        // Événement pour enregistrer la dette
        saveDebtButton.setOnAction(event -> saveDebt());
    }

    Client client;

    public void Search() {
        String tel = telephoneField.getText();
        client = clientService.find(tel);
        if (client == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Information");
            alert.setHeaderText("Client non trouvé");
            alert.setContentText("Le client n'a pas été dans la base de données");
            alert.showAndWait();
        } else {
            nomTextField.setDisable(false);
            addresseTextField.setDisable(false);
            quantityTextField.setDisable(false);
            articleComboBox.setDisable(false);
            addDetailButton.setDisable(false);
            saveDebtButton.setDisable(false);
            nomTextField.setText(client.getNom());
            addresseTextField.setText(client.getAdresse());
        }

    }

    private void addActionColumn() {
        actionColumn = new TableColumn<>("Action");

        // Utilisation d'une `cell factory` pour ajouter des boutons
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                // Style du bouton (optionnel)
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> {
                    DetailDette detail = getTableView().getItems().get(getIndex());
                    removeDetail(detail); // Supprimer le détail correspondant
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        detailsTableView.getColumns().add(actionColumn);
    }

    private void removeDetail(DetailDette detail) {
        montant -= detail.getQte() * detail.getArticle().getPrix();
        montantTextField.setText(String.valueOf(montant));
        detailsList.remove(detail); // Supprimer le détail de la liste observable
        // Vous pouvez également ajouter une logique supplémentaire si nécessaire
    }

    private void addUpdateColumn() {
        updateColumn = new TableColumn<>("Mettre à jour");

        // Utilisation d'une `cell factory` pour ajouter des boutons
        updateColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Mettre à jour");

            {
                // Style du bouton (optionnel)
                updateButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                updateButton.setOnAction(event -> {
                    DetailDette detail = getTableView().getItems().get(getIndex());
                    updateDetail(detail); // Appeler la méthode pour mettre à jour l'élément
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });

        detailsTableView.getColumns().add(updateColumn);
    }

    private void updateDetail(DetailDette detail) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Mettre à jour l'article");
        dialog.setHeaderText("Mettre à jour les informations");
        dialog.setContentText("Valeur :");

        dialog.showAndWait().ifPresent(newValue -> {
            try {
                double valeur = Double.parseDouble(newValue);
                detail.setQte(valeur);
                montant = montant + detail.getQte() * detail.getArticle().getPrix();
                montantTextField.setText(String.valueOf(montant));
                detailsTableView.refresh();
            } catch (NumberFormatException e) {
                // Gérer l'erreur de saisie
                System.out.println("Erreur de saisie : " + e.getMessage());
            }
        });
    }

    private void addDetail() {
        Article selectedArticle = articleComboBox.getValue();
        String quantityText = quantityTextField.getText();

        if (selectedArticle != null && !quantityText.isEmpty()) {
            int quantity = Integer.parseInt(quantityText);

            // Vérifier si l'article existe déjà dans la liste
            DetailDette existingDetail = detailsList.stream()
                    .filter(detail -> detail.getArticle().equals(selectedArticle))
                    .findFirst()
                    .orElse(null);

            if (existingDetail != null) {
                // Si l'article existe, mettre à jour la quantité et le montant
                existingDetail.setQte(existingDetail.getQte() + quantity);
                montant += selectedArticle.getPrix() * quantity;
            } else {
                // Si l'article n'existe pas, créer un nouveau détail
                DetailDette detail = new DetailDette();
                detail.setArticle(selectedArticle);
                detail.setQte(quantity);
                detail.setDette(currentDette);
                montant += selectedArticle.getPrix() * quantity;
                detailsList.add(detail); // Ajoute l'article à la liste

                currentDette.setDetails(detail);
            }

            // Mettre à jour le champ montant
            montantTextField.setText(String.valueOf(montant));

            // Rafraîchir la table pour refléter les modifications
            detailsTableView.refresh();

            // Réinitialiser les champs
            articleComboBox.getSelectionModel().clearSelection();
            quantityTextField.clear();
        } else {
            System.out.println("Veuillez sélectionner un article et saisir une quantité.");
        }
    }

    private void saveDebt() {
        if (client != null) {
            currentDette.setClient(client);
            currentDette.setEtat(EtatDette.VALIDER);
            currentDette.setState(StateDette.DESARCHIVER);

            // Enregistrez votre dette ici
            detteService.save(currentDette);
            System.out.println("Dette enregistrée pour le client: " + client.getNom());
            // Réinitialisez le formulaire si nécessaire
            detailsList.clear();
            currentDette = new Dette(); // Réinitialise l'objet Dette
        } else {
            System.out.println("Veuillez sélectionner un client.");
        }
    }

    // Méthodes pour obtenir les clients et articles
    private List<Client> getClients() {
        for (Client client : clientService.show()) {
            System.out.println(client);
        }
        return clientService.show();
    }

    private List<Article> getArticles() {
        return articleService.show();
    }
}
