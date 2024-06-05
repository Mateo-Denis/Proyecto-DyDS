package views.storageView;

import presenters.storagePresenter.IStoragePresenter;
import presenters.storagePresenter.StoragePresenter;
import utils.HTMLFormatter;
import javax.swing.*;

public class StorageView extends JComponent implements IStorageView {
	private JComboBox<String> savedSearchesComboBox;
	private JTextPane savedSearchPane;
	private JPanel storagePanel;
	private JPanel container;
	private IStoragePresenter storagePresenter;
	private JPopupMenu storedInfoPopup;

	public StorageView() {
		savedSearchPane.setContentType("text/html");
	}

	@Override
	public void setStoragePresenter(StoragePresenter storagePresenter) {
		this.storagePresenter = storagePresenter;
	}

	public void start(){

		savedSearchesComboBox.addActionListener(e -> storagePresenter.onSavedSearchSelected((String) savedSearchesComboBox.getSelectedItem()));

		storedInfoPopup = new JPopupMenu();

		JMenuItem deleteItem = new JMenuItem("Delete saved series");
		deleteItem.addActionListener(e -> storagePresenter.onDeleteSavedSearch((String) savedSearchesComboBox.getSelectedItem()));
		storedInfoPopup.add(deleteItem);

		JMenuItem saveItem = new JMenuItem("Save changes");
		saveItem.addActionListener(e -> storagePresenter.onSaveChanges((String) savedSearchesComboBox.getSelectedItem(), savedSearchPane.getText()));
		storedInfoPopup.add(saveItem);

		savedSearchPane.setComponentPopupMenu(storedInfoPopup);
	}

	@Override
	public void setSavedSearches(String[] savedSearches) {
		savedSearchesComboBox.setModel(new DefaultComboBoxModel<>(savedSearches));
	}

	@Override
	public void setSavedSearch(String text) {
		savedSearchPane.setText(HTMLFormatter.textToHtml(text));
	}

	@Override
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(storagePanel, message);
	}


	public JPanel getStoragePanel() {
		return storagePanel;
	}
}