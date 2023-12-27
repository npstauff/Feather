package nixstudio.Feather.language;

import java.io.File;

public abstract class LanguageAgent{
	//Source file to run
	protected File source = null;
	protected boolean compiled = false;
	
	public LanguageAgent(File source) {
		this.source = source;
	}
	
	public LanguageAgent() {
		
	}
	
	public boolean isCompiled() {
		return compiled;
	}
	
	public abstract String getId();
	
	//Override this for each language compilation
	protected abstract void compile();
	
	//Override this for running each language
	public abstract void run();
	
	public void compileAndRun() {
		compile();
		run();
	}
}
