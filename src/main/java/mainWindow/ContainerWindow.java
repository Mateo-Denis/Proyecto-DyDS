package mainWindow;

import reviewedTabMVP.view.ReviewedViewImpl;
import searchTabMVP.view.SearchViewImpl;

import javax.swing.*;

public class ContainerWindow extends {
	private JPanel containerPanel;
	private JTabbedPane containerTabbedPane;

	public ContainerWindow() {
		containerTabbedPane.addTab("Reviewed", new ReviewedViewImpl());
		containerPanel.add(containerTabbedPane);
	}






}
