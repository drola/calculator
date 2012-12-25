package si.drola.calculator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;

/**
 * Manage data about units, functions...
 * 
 */
public class Data {
	protected Units units;
	private Functions functions;

	public Data() {
		this.units = new Units();
		this.functions = new Functions();
	}

	public void loadUnitsFromFile(String filename) {
		char PRIMITIVECHAR = '!'; /* Character that marks irreducible units */
		char COMMENTCHAR = '#'; /* Comments marked by this character */
		char COMMANDCHAR = '!'; /* Unit database commands marked with this */
		char UNITSEPCHAR = ';'; /* Separator for unit lists. Include this */
		/* char in rl_basic_word_break_characters */
		/* and in nonunitchars defined in parse.y */
		char FUNCSEPCHAR = ';'; /* Separates forward and inverse definitions */
		String HELPCOMMAND = "help"; /* Command to request help at prompt */
		String SEARCHCOMMAND = "search"; /*
										 * Command to request text search of
										 * units
										 */
		String UNITMATCH = "?"; /* Command to request conformable units */
		String DEFAULTPAGER = "more"; /* Default pager program */
		String DEFAULTLOCALE = "en_US"; /* Default locale */
		int MAXINCLUDE = 5; /* Max depth of include files */
		int MAXFILES = 25; /* Max number of units files on command line */
		String NODIM = "!dimensionless"; /*
										 * Marks dimensionless primitive units,
										 * such
										 */
		/* as the radian, which are ignored when */
		/* doing unit comparisons */
		
		String WHITE = " \t\n";

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new BOMInputStream(
					new FileInputStream(filename))));
			String line;
			// Read File Line By Line
			int linenumber = 0;
			String unitname;
			while ((line = in.readLine()) != null) {
				linenumber++;
				
				//Strip comments
				int commentIndex = line.indexOf(COMMENTCHAR);
				if(commentIndex>=0) {
					line = line.substring(0, commentIndex);
				}
				
				int tokenpos = StringUtils.indexOfAny(line, WHITE);
				unitname = null;
				if(line.charAt(0) == COMMANDCHAR && tokenpos>0) {
					unitname = line.substring(1, tokenpos-1);
					if(unitname.length()<1) {
						continue;
						//Error
					}
					
					//TODO!
				}
				
				
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}

	}
}
