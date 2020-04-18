import java.lang.reflect.Field; 
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

public class ConfigLoader {
	private Wini iniHandle;
	
	public Wini getIniHandle() {
		return iniHandle;
	}
	
	public ConfigLoader(String configFilePath) throws InvalidFileFormatException, IOException {
		iniHandle = new Wini(new File(configFilePath));
	}
	
	public void loadSection(String sectionName, Class klass, Object instance) {
		Ini.Section section = iniHandle.get(sectionName);
		Set<Entry<String, String>> entrySet = section.entrySet();
		Iterator<Entry<String, String>> it = entrySet.iterator();
		while(it.hasNext()) {
			Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String valueString = entry.getValue();
			Field field;
			try {
				field = klass.getDeclaredField(key);
				field.setAccessible(true);
			} catch (NoSuchFieldException | SecurityException e) {
				System.err.println("section \""+sectionName+"\", unknown keyword \""+key+"\": "+e);
				continue;
			}
			Class fieldType = field.getType();
			try {
				if (fieldType == int.class) {				
					field.setInt(instance, Integer.valueOf(valueString));
				} else if (fieldType == String.class) {
					field.set(instance, valueString);
				} else if (fieldType == double.class) {
					field.setDouble(instance, Double.valueOf(valueString));
				} else if (fieldType == boolean.class){
					field.setBoolean(instance, Boolean.valueOf(valueString));
				} else {
					System.err.println("section \""+sectionName+"\", unsupported type "+fieldType+" for key \""+key+"\"");
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				System.err.println("section \""+sectionName+"\", cannot set value for key \""+key+"\": "+e);
			}
		}
	}
}
