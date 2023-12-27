package nixstudio.Feather.utils;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import nixstudio.Feater.Exceptions.FeatherLanguageException;
import plugins.Console;

public class LanguageUtils{
	public enum Language {
		C,
		Java,
		Unknown
	}
	
	public static String extension(File f) {
		return FilenameUtils.getExtension(f.getName());
	}
	
	public static Language language(String extension) {
		switch(extension) {
		case "c": {
			return Language.C;
		}
//		case "java": {
//			return Language.Java;
//		}
		default: return Language.Unknown;
		}
	}
	
	public static LanguageUtils create() {
		return new LanguageUtils();
	}
	
	public static LanguageAgent agent(File file) throws FeatherLanguageException {
		switch(language(extension(file))) {
		case C:
			return LanguageUtils.create().new CAgent(file);
		case Java:
			return LanguageUtils.create().new JavaAgent(file);
		default:
			throw new FeatherLanguageException("Language '" + language(extension(file)) + "' not supported");
		}
	}
	
	public abstract class LanguageAgent{
		//Source file to run
		File source = null;
		protected boolean compiled = false;
		
		public LanguageAgent(File source) {
			this.source = source;
		}
		
		public boolean isCompiled() {
			return compiled;
		}
		
		//Override this for each language compilation
		protected abstract void compile();
		
		//Override this for running each language
		public abstract void run();
		
		public void compileAndRun() {
			compile();
			run();
		}
	}
	
	class CAgent extends LanguageAgent{
		public CAgent(File source) {
			super(source);
			// TODO Auto-generated constructor stub
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
	}
	
	class JavaAgent extends LanguageAgent{

		public JavaAgent(File source) {
			super(source);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void compile() {
			// TODO Auto-generated method stub
			Console.enterCommand("javac " + source.getAbsolutePath());
			System.out.println("Compliling java file...");
			compiled = true;
			System.out.println("Compiled!");
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("Executing java file...");
			Console.enterCommand("java " + FilenameUtils.getBaseName(source.getName()));
			File file = new File(FilenameUtils.getBaseName(source.getName())+".class");
			file.delete();
			System.out.println("Executed!");
		}
		
		@Override
		public void compileAndRun() {
			new Thread(() -> {
				compile();
				run();
			}).start();
		}
	}
}