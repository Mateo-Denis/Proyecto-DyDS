package views.storageView;

import models.storageModel.IStorageModel;
import presenters.storagePresenter.IStoragePresenter;
import presenters.storagePresenter.StoragePresenter;
import views.storageView.IStorageView;

import javax.swing.*;

public class StorageView implements IStorageView {
	private JComboBox<String> savedSearchesComboBox;
	private JTextPane savedSearchPane;
	private JPanel storagePanel;

	private final IStoragePresenter presenter;

	public StorageView(IStorageModel model) {
		presenter = new StoragePresenter(this, model);

		savedSearchesComboBox.addActionListener(e -> presenter.onSavedSearchSelected((String) savedSearchesComboBox.getSelectedItem()));

		JPopupMenu storedInfoPopup = new JPopupMenu();

		JMenuItem deleteItem = new JMenuItem("Delete!");
		deleteItem.addActionListener(e -> presenter.onDeleteSavedSearch((String) savedSearchesComboBox.getSelectedItem()));
		storedInfoPopup.add(deleteItem);

		JMenuItem saveItem = new JMenuItem("Save Changes!");
		saveItem.addActionListener(e -> presenter.onSaveChanges((String) savedSearchesComboBox.getSelectedItem(), savedSearchPane.getText()));
		storedInfoPopup.add(saveItem);

		savedSearchPane.setComponentPopupMenu(storedInfoPopup);
	}

	@Override
	public void setSavedSearches(String[] savedSearches) {
		savedSearchesComboBox.setModel(new DefaultComboBoxModel<>(savedSearches));
	}

	@Override
	public void setSavedSearch(String text) {
		savedSearchPane.setText(MainWindow.textToHtml(text));
	}

	@Override
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(storagePanel, message);
	}

	public JPanel getStoragePanel() {
		return storagePanel;
	}
}