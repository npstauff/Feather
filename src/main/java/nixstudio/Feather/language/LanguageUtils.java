package nixstudio.Feather.language;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import nixstudio.Feater.Exceptions.FeatherLanguageException;
import plugins.Console;

public class LanguageUtils{	
	public static String extension(File f) {
		return FilenameUtils.getExtension(f.getName());
	}
	
	public static LanguageUtils create() {
		return new LanguageUtils();
	}
	
	
}