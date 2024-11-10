package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<Person> personListView;
    private MainWindow mainWindow;
    private final PersonDetailsWindow personDetailsWindow;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList} and {@code PersonDetailsWindow}.
     *
     * @param personList          The observable list of persons to display.
     * @param personDetailsWindow The window to display person details.
     */
    public PersonListPanel(ObservableList<Person> personList, PersonDetailsWindow personDetailsWindow, MainWindow mainWindow) {
        super(FXML);
        this.personDetailsWindow = personDetailsWindow;
        this.mainWindow = mainWindow;
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());

        // Ensure the ListView can receive key events
        personListView.setFocusTraversable(true);
        personListView.requestFocus();

        setEventHandlers();
    }

    /**
     * Sets event handlers for the person list view.
     */
    private void setEventHandlers() {
        personListView.setOnMouseClicked(this::handlePersonClick);
        personListView.setOnKeyPressed(this::handleKeyPress);
    }

    /**
     * Handles the mouse click event on the person list view.
     *
     * @param event The mouse event.
     */
    private void handlePersonClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            openPersonDetailsWindow();
        }
    }

    /**
     * Requests keyboard focus on the person list view component.
     *
     * This method transfers focus to the `personListView`, allowing it to receive keyboard input.
     */
    public void requestFocus() {
        personListView.requestFocus();
    }

    /**
     * Handles key press events on the person list view.
     *
     * @param event The key event.
     */
    private void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case ENTER:
                openPersonDetailsWindow();
                break;
            case UP:
                if (personListView.getSelectionModel().getSelectedIndex() == 0) {
                    mainWindow.focusOnCommandBox();
                    event.consume();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Selects the first item in the person list view.
     *
     * This method uses the selection model of `personListView` to select the first item in the list,
     * making it the current selection if available.
     */
    public void selectFirst() {
        personListView.getSelectionModel().selectFirst();
    }

    /**
     * Opens the PersonDetailsWindow for the selected person.
     */
    private void openPersonDetailsWindow() {
        Person selectedPerson = personListView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            personDetailsWindow.show(selectedPerson);
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1).getRoot());
            }
        }
    }
}
