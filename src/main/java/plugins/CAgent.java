package plugins;

import java.io.File;

import nixstudio.Feather.Core.Plugin;
import nixstudio.Feather.Core.PluginType;
import nixstudio.Feather.language.LanguageAgent;

public class CAgent extends LanguageAgent implements Plugin{
	public CAgent(File source) {
		super(source);
		// TODO Auto-generated constructor stub
	}
	
	public CAgent() {
		super();
	}

	@Override
	protected void compile() {
		// TODO Auto-generated method stub
		Console.enterCommand("gcc " + source.getAbsolutePath());
		compiled = true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Console.enterCommand("./a.exe");
		File file = new File("a.exe");
		file.delete();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "C Language Agent";
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return "c";
	}

	@Override
	public PluginType type() {
		// TODO Auto-generated method stub
		return PluginType.Language;
	}
}
