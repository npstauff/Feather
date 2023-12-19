package nixstudio.Feather;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.*;
import plugins.Editor;
import plugins.Explorer;

public class GuiMain extends Application{
	
	public static final String PLUGIN_PATH = "src/main/java/plugins";
	public static final File basePluginFolder() {
		return new File(PLUGIN_PATH);
	}
	
	public static void main(String[] args) {
		launch(new GuiMain());
	}
	
	Configuration config = null;
	
	ArrayList<Class<? extends StudioWindow>> windowTypes = new ArrayList<>();
	
	ArrayList<Pair<Boolean, StudioWindow>> windows = new ArrayList<>();
	
	@Override
	protected void configure(Configuration config) {
		config.setTitle("Feather");
		this.config = config;
	}
	
	@Override
	protected void preRun() {
		ImGui.getIO().setConfigFlags(ImGuiConfigFlags.DockingEnable);
		loadPlugins();
	}
	
	public void loadPlugins() {
		File f = new File(PLUGIN_PATH);
		if(f.exists()) {
			for(File file : f.listFiles()) {
				if(FilenameUtils.getExtension(file.getName()).equals("java")) {
					System.out.println(file.getName());
					loadPlugin(file);
				}
			}
		}
		else {
			System.out.println("Plugin path not found!");
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends StudioWindow> void loadPlugin(File f) {
		String className = "plugins." + FilenameUtils.getBaseName(f.getName());
		try {
			Class<T> klass = (Class<T>) Class.forName(className);
			this.windowTypes.add(klass);
		} catch (Exception e) {
			error("Unable to load plugin: " + f.getName(), e);
		}
	}
	
	public <T extends StudioWindow> void openWindow(Class<T> klass) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Constructor<T> ctor = klass.getConstructor(GuiMain.class);
		T object = ctor.newInstance(new Object[] {this});
		windows.add(new Pair<Boolean, StudioWindow>(true, object));
	}
	
	@SuppressWarnings("unchecked")
	public <T extends StudioWindow> T getWindow(Class<T> klass) {
		for(Pair<Boolean, StudioWindow> window : windows) {
			if(klass.isInstance(window)) {
				return (T)window.second;
			}
		}
		return null;
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		setUpDockspace();
		
		for(int i = windows.size() - 1; i >= 0; i--) {
			Pair<Boolean, StudioWindow> window = windows.get(i);
			ImGui.begin(window.second.getTitle());
			ImGui.sameLine();
			if(ImGui.button("X")) {
				windows.remove(i);
			}
			window.second.guiUpdate();
			ImGui.end();
		}
		
		ImGui.end();
	}
	
	void setUpDockspace() {
		int windowFlags = 0;
		ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
		ImGui.setNextWindowSize(config.getWidth(), config.getHeight());
		windowFlags |= ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize |
				ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;
		
		
		
		ImGui.begin("Feather", new ImBoolean(true), windowFlags);
		
		if(ImGui.beginMenuBar()) {
			if(ImGui.beginMenu("File")) {
				if(ImGui.menuItem("Save")) {
					getWindow(Editor.class).save(getWindow(Editor.class).selected);
				}
				if(ImGui.menuItem("Save All")) {
					getWindow(Editor.class).save();
				}
				ImGui.separator();
				if(ImGui.menuItem("Open Folder")) {
					getWindow(Explorer.class).openFolder();
				}
				ImGui.endMenu();
			}
			if(ImGui.beginMenu("Window")) {
				for(Class<? extends StudioWindow> klass : windowTypes) {
					if(ImGui.menuItem(klass.getName().replace("plugins.", ""))) {
						try {
							openWindow(klass);
						} catch (Exception e) {
							error("Unable to open window '" + klass.getName() + "'", e);
						}
					}
				}
				ImGui.separator();
				if(ImGui.menuItem("Open Plugins Folder")) {
					try {
						Process p = new ProcessBuilder("explorer.exe", "/select,",PLUGIN_PATH.replaceAll("/", "\\\\")).start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				ImGui.endMenu();
			}
			ImGui.endMenuBar();
		}
		
		if(windows.size() <= 0) {
			ImGui.text("No Windows Open");
		}
		
		ImGui.dockSpace(ImGui.getID("Dockspace"));
	}
	
	public void error(String message, Exception e) {
		System.out.println(message + "\nReason: " + e.getMessage());
	}
}