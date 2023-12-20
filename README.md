# Welcome
Feather is a lightweight IDE written in Java using the JNI-based ImGui renderer by SpaiR at: https://github.com/SpaiR

Features
--
So far the editor supports
* Multiple windows/docking
* 3 core windows
  * Explorer, which allows you to find files to edit
  * Editor, which allows you to open files and edit their code
  * Console, which displays the system output and allows you to run commands
* A plugin development system

Plugins
--
Plugins are a way to create user-designed windows and display them in the program dynamically.

All the core Feather windows are implemented using the plugin system, so they can be used as extra examples.

Developing A Plugin
--

To develop a plugin, make a class and place it in the `plugins` package

![image](https://github.com/nix3220/Feather/assets/80929841/d56ea92c-d0ba-4c91-9eba-417d5919ff73)

Next, edit the class to inherit from the `StudioWindow` class
```java
class MyWindow extends StudioWindow{

	public MyWindow(GuiMain main) {
		super(main);
	}

	@Override
	public String getTitle() {
		//Replace with the title of your window
		return "My Window";
	}

	@Override
	public void guiUpdate() {
		// Do all of your Gui here
		Gui.text("This is my window!");
	}
	
}
```

You will now see your window appear in the `Window` menu when you run the application

![image](https://github.com/nix3220/Feather/assets/80929841/b4af4608-d833-4923-8a61-d1da829e59e1)

That's all you have to do! The last step is to click on your window in the menu and it will open as a new dockable gui window.

![image](https://github.com/nix3220/Feather/assets/80929841/039f3a56-1c96-41e6-9ab3-8ec6b6928ff4)

Gui
--
The `Gui` class is an extension of the `ImGui` class provided by SpaiR.

Issues
--
- [ ] The editor has a bug where you have to double click on a recently added file to open it

TODO
--
Things to implement

- [ ] Editor run files
- [ ] Implement a debugger
- [ ] Plugin publishing and downloading from the web
