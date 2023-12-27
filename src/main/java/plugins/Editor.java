package plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import imgui.ImGui;
import imgui.extension.texteditor.*;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import nixstudio.Feater.Exceptions.FeatherLanguageException;
import nixstudio.Feather.Core.Gui;
import nixstudio.Feather.Core.GuiMain;
import nixstudio.Feather.Core.StudioWindow;
import nixstudio.Feather.utils.LanguageUtils;
import nixstudio.Feather.utils.LanguageUtils.Language;

import org.apache.commons.io.*;

public class Editor extends StudioWindow{

	public Editor(GuiMain main) {
		super(main);
		// TODO Auto-generated constructor stub
	}

	public int selected = -1;
	
	public ArrayList<File> currentFiles = new ArrayList<>();
	public ArrayList<ImBoolean> currentTabs = new ArrayList<>();
	public ArrayList<String[]> fileContents = new ArrayList<>();
	public ArrayList<Boolean> edits = new ArrayList<>();
	
	static TextEditor EDITOR = new TextEditor();
	static {
		TextEditorLanguageDefinition lang = TextEditorLanguageDefinition.c();
		EDITOR.setLanguageDefinition(lang);
	}
	
	public void addFile(File f) {
		if(currentFiles.contains(f)) return;
		currentFiles.add(f);
		currentTabs.add(new ImBoolean(false));
		fileContents.add(load(f));
		edits.add(false);
	}
	
	public void remove(int i) {
		currentFiles.remove(i);
		currentTabs.remove(i);
		fileContents.remove(i);
	}
	
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Editor";
	}
	
	
	public File currentFile() {
		return currentFiles.get(selected);
	}

	@Override
	public void guiUpdate() {
		// TODO Auto-generated method stub
		if(currentFiles.size() > 0 && selected != -1) {
			Gui.sameLine();
			Language language = LanguageUtils.language(FilenameUtils.getExtension(currentFile().getName()));
			if(Gui.button("Run " + language + " Program")) {
				runFile(language, currentFile());
			}
		}
		String text = EDITOR.getText();
		if(currentFiles.size() > 0) {
			if(Gui.beginTabBar("tabs")) {
				for(int i = 0; i < currentFiles.size(); i++) {
					if(Gui.beginTabItem((edits.get(i) ? "* " : "") + currentFiles.get(i).getName())) {
						selected = i;
						Gui.endTabItem();
					}	
				}
				Gui.endTabBar();
			}
			EDITOR.setTextLines(fileContents.get(selected));
			EDITOR.render("editor");
			if(text != "" && !EDITOR.getText().equals(text)) {
				fileContents.set(selected, EDITOR.getTextLines());
				edits.set(selected, true);
			}
			
		}
		else {
			noFiles("No Files Selected");
		}
	}
	
	public void runFile(Language language, File f) {
		try {
			LanguageUtils.agent(f).compileAndRun();
		}
		catch (FeatherLanguageException e) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
		}
	}
	
	void noFiles(String text) {
	    float windowWidth = ImGui.getWindowSize().x;
	    float textWidth = ImGui.calcTextSize(text).x;
	    
	    float windowHeight = ImGui.getWindowSize().y;
	    float textHeight = ImGui.calcTextSize(text).y;

	    ImGui.setCursorPosX((windowWidth - textWidth) * 0.5f);
	    ImGui.setCursorPosY((windowHeight - textHeight) * 0.5f - 20);
	    ImGui.text(text);
	    ImGui.setCursorPosX((windowWidth - ImGui.calcTextSize("Open Folder").x) * 0.5f);
	    if(ImGui.button("Open Folder")) {
	    	if(application.getWindow(Explorer.class) != null) {
	    		application.getWindow(Explorer.class).openFolder();
	    	}
	    }
	}
	
	@SuppressWarnings("deprecation")
	public void save(int index) {
		if(index != -1) {
			File f = currentFiles.get(index);
			try {
				FileUtils.write(f, String.join("\n", fileContents.get(index)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			edits.set(index, false);
		}
	}
	
	public void save() {
		for(int i = 0; i < currentFiles.size(); i++) {
			save(i);
		}
	}
	
	public String[] load(File f) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			result = (ArrayList<String>) FileUtils.readLines(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] arr = new String[result.size()];
		
		int i = 0;
		for(String s : result) {
			arr[i] = s;
			i++;
		}
		
		return arr;
	}
}


