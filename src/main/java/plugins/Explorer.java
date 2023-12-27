package plugins;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import imgui.ImGui;
import imgui.type.ImBoolean;
import nixstudio.Feather.Core.Gui;
import nixstudio.Feather.Core.GuiMain;
import nixstudio.Feather.Core.Pair;
import nixstudio.Feather.Core.Plugin;
import nixstudio.Feather.Core.StudioWindow;

public class Explorer extends StudioWindow implements Plugin{
	
	public Explorer(GuiMain main) {
		super(main);
		// TODO Auto-generated constructor stub
	}

	public File srcPath = null;
	ArrayList<Pair<File, ImBoolean>> tree = new ArrayList<>();
	
	
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Explorer";
	}

	public String getName(){
		return "Explorer";
	}

	@Override
	public void guiUpdate() {
		// TODO Auto-generated method stub
		if(srcPath != null) {
			Gui.text(srcPath.getName());
			Gui.indent();
			listFiles(srcPath);
		}
		else {
			Gui.text("No folder open");
		}
	}
	
	public void openFolder() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int option = chooser.showOpenDialog(new JFrame());
		if(option == JFileChooser.APPROVE_OPTION) {
			srcPath = chooser.getSelectedFile();
		}
	}
	
	public boolean hasFile(File file) {
		for(Pair<File, ImBoolean> p : tree) {
			if(p.first.getPath().equals(file.getPath())) {
				return true;
			}
		}
		return false;
	}
	
	public ImBoolean getStatus(File file) {
		for(Pair<File, ImBoolean> p : tree) {
			if(p.first.getPath().equals(file.getPath())) {
				return p.second;
			}
		}
		return null;
	}
	
	public void setStatus(File file, boolean b) {
		for(Pair<File, ImBoolean> p : tree) {
			if(p.first.getPath().equals(file.getPath())) {
				p.second.set(b);
			}
		}
	}

	private void listFiles(File file) {
		for(File f : file.listFiles()) {
			if(f.isDirectory()) {
				if(!hasFile(f)) {
					tree.add(new Pair<File, ImBoolean>(f, new ImBoolean(false)));
				}
				if(f.listFiles().length > 0) {
					Gui.checkbox(f.getName(), getStatus(f));
					if(getStatus(f).get()) {
						ImGui.indent();
						listFiles(f);
						ImGui.unindent();
					}
				}
				else {
					Gui.bulletText(f.getName());
				}
			}
		}
		for(File f : file.listFiles()) {
			if(!f.isDirectory()) {
				if(Gui.button(f.getName())) {
					application.getWindow(Editor.class).addFile(f);
				}
			}
		}
	}
	
}
