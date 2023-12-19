package nixstudio.Feather;

import imgui.app.Application;

public abstract class StudioWindow{
	
	public Main application;
	
	public StudioWindow(Main main) {
		this.application = main;
	}
	
	public abstract String getTitle();

	public abstract void guiUpdate();

}
