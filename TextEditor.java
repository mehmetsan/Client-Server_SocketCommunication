/**
 * 
 */
/**
 * @author MehmetSanisoglu
 *
 */
import java.io.*;
import java.net.*;

public class TextEditor {
	
	String USERNAME = "bilkentstu";
	String PASS = "cs421s2020";
	String END = "\\r\\n";
	String MESSAGE = "";
	String RESPONSE = "";
	String tempVersion = "";
	String content = "";
	int version = 0;
	int lineCount = 0;
	boolean gotUpdate = false;

	public static void main(String[] args) throws Exception {

		TextEditor CLIENT = new TextEditor();
		CLIENT.run(args[0], args[1]);
		
	}
	
	public int getLineCount(String content) {
		
		int count = 0;
		for(int i=0; i<content.length(); i++)
		{
			if(content.charAt(i)=='\n') 
				count += 1;	
		}		
		return count;
	}
	
	public void appendText( String newText, PrintStream PS, BufferedReader BR ) throws Exception{
		MESSAGE = "APND "+version + " " + newText;
		PS.println(MESSAGE);
		BR.readLine(); //FOR APPEND MESSAGE
	}
	
	public void writeText( String newText, PrintStream PS, int lineNo, BufferedReader BR ) throws Exception{
		MESSAGE = "WRTE "+ version + " " + lineNo + " " + newText;
		PS.println(MESSAGE);
		BR.readLine(); //FOR WRITE MESSAGE
	}
	
	public String getContent( BufferedReader BR) throws Exception{
		String result = "";
		if(gotUpdate) {
			String tempContent = "";
			String line=BR.readLine();
			int check = line.length();

			while(check != 0 ) {
				tempContent = tempContent + "\n" + line;
				line= BR.readLine();
				check = line.length();
			}
			result = content + "\n" + tempContent;
		}
		else {
			result = content;
		}
		
		return result;
	}
	
	public void getUpdate( PrintStream PS, BufferedReader BR ) throws Exception{
		tempVersion = version +"";
		MESSAGE = "UPDT "+ tempVersion;
		PS.println(MESSAGE);
		RESPONSE = BR.readLine();
		gotUpdate = false;

		if(RESPONSE.charAt(0) == 'O') //IF WE HAVE DONE AN UPDATE
		{	
			gotUpdate = true;
			version = Integer.parseInt(RESPONSE.substring(3,5).trim());
			
			if(RESPONSE.length() > 5) {		//IF FILE IS NOT EMPTY
				content = RESPONSE.substring(5, RESPONSE.length());
				
				for( String line =BR.readLine(); !line.isEmpty(); line =BR.readLine()  ) 
					content = content + "\n" + line;
				
			}
					
			lineCount = getLineCount(content);
			
		}
	}

	public void run(String ipArg, String portArg) throws Exception{
		
		
		Socket SOCK = new Socket(ipArg,Integer.parseInt(portArg));	
		PrintStream PS = new PrintStream(SOCK.getOutputStream());
		InputStreamReader IR = new InputStreamReader(SOCK.getInputStream());
		BufferedReader BR = new BufferedReader(IR);
		

		MESSAGE = "USER "+USERNAME;
		PS.println(MESSAGE);
		RESPONSE = BR.readLine();

		
		MESSAGE = "PASS "+PASS;
		PS.println(MESSAGE);
		RESPONSE = BR.readLine();

		
		int i=0;
		while(i< 10) {
			getUpdate(PS, BR);
			appendText("Append message " + i,PS,BR);
			
			getUpdate(PS, BR);

			if(i % 2 == 0) 
				writeText("A write message " + i, PS, i, BR);
						
			getUpdate(PS, BR);
			i += 1;
			
		}
		
	}
	
		
}