import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Scanner {
	
	private int row = 1; 
	private int col = 0;
	private int tempValue = 0;
	
	private BufferedReader reader;
	private List<Character> code;
	
	private OnScannedCallback callback;
	
	private int buffer_ctr = 0;
	private int status = -1;
	
	
	private final int STRING_CONS = 0;
	private final int NORMAL = 1;
	private final int COMMENT = 2;
	private final int SYMBOL = 3;
	
	private int comment_type = -1;
	private final int INLINE = 100;
	private final int MULTILINE = 101;
	
	private final String ALLOWED_CHARS[] = {"!", "#", "$", "%", "^", "&", "*", "()", "_", "+", "-", "=",
											"{", "}", "[", "]", ":", ";", "'", "<", ">", "?", ",", ".","~", "(",")"};
	
	private final String TERMINATORS[] = {" "};
	
	private StringBuilder builder;
	private Tester tester;
	
	public Scanner(OnScannedCallback callback, String filePath, Tester tester){ 
		this.callback = callback;	
		code = new ArrayList<Character>();
		this.tester = tester;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void startScan(){
		try {
			builder = null;
			builder = new StringBuilder();
			
			
			while ((tempValue =reader.read())!=-1){
				code.add((char)tempValue);
			}
			
			for (int x=0; x<code.size(); x++){
				col++;
				buffer_ctr++;
				
				
				
				if (status == -1){					
					if (code.get(x) == ' '||code.get(x) == '\t'){
						continue;
					}
					
					if (code.get(x)=='\n'||code.get(x)=='\r'){
						row++;
						col = 0;
						continue;
					}
					if (code.get(x)=='"' || code.get(x)=='\''){
					
						status = STRING_CONS;
						continue;
					}
					else if (isLetter(code.get(x))||isNumber(code.get(x))){
						status = NORMAL;
					}
					else if (isAllowedChar(code.get(x))){
						status = SYMBOL;
					}
					else if (code.get(x)=='@'){
						status = COMMENT;
						if (code.get(x)=='@'){
							comment_type = INLINE;
						}
						else if (code.get(x)=='~'){
							comment_type = MULTILINE;
						}
					}
					else {
						System.out.println("Symbol "+code.get(x)+" on line :"+row+" column :"+col+" is not recognized");
					}
				}
			
				switch(status){
				
				case NORMAL:
					if (isLetter(code.get(x))|| isNumber(code.get(x))){
						builder.append(code.get(x));
						continue;
					}
					else{
						x--;
						callback.onTokenFound(builder.toString());
						builder = null;
						builder = new StringBuilder();
						status = -1;
					}
					break;
				case STRING_CONS:
					if (code.get(x)=='"'|| code.get(x)=='\''){
						callback.onTokenFound(builder.toString());
						builder = null;
						builder = new StringBuilder();
						status = -1;
					}
					else{
						builder.append(code.get(x));
						continue;
					}
					break;
				case COMMENT:
					if (comment_type == INLINE){
						if (code.get(x) == '\n'||code.get(x) == '\r'){
							callback.onTokenFound(builder.toString());
							builder = null;
							builder = new StringBuilder();
							status = -1;
						}
						else{
							builder.append(code.get(x));
							continue;
						}
					}
					if (comment_type == MULTILINE){
						if (code.get(x) == '~' && code.get(buffer_ctr)=='@'){
							builder.append(code.get(x));
							x++;
							builder.append(code.get(x));							
							callback.onTokenFound(builder.toString());
							builder = null;
							builder = new StringBuilder();
							status = -1;
						}
						else{
							builder.append(code.get(x));
							continue;
						}
					}
					break;
				case SYMBOL:
					if (!tester.checkAvailable(builder.toString()+code.get(x))||code.get(x) == '\n'||code.get(x) == '\r'||code.get(x) == ' '||code.get(x) == '\t'){
						x=x-1;
						callback.onTokenFound(builder.toString());
						builder = null;
						builder = new StringBuilder();
						status = -1;
					}
					else{
						builder.append(code.get(x));
						continue;
					}
					break;
				}
	
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Callback Method
	public interface OnScannedCallback{
		public void onTokenFound(String token);
	}
	
	private boolean isLetter(int value){
		return (value=='_'||value >=65 && value<=90 || value >=97 && value<=122);
	}
	
	private boolean isNumber(int value){
		return (char)value == '0' ||(char)value == '1'||(char)value == '2'||(char)value == '3' ||(char)value == '4'||(char)value == '5'||(char)value == '6'||(char)value == '7'||(char)value == '8' ||(char)value == '9';
	}

	private boolean isAllowedChar(char value){
		String convertedString = Character.toString(value);
		
		for(String symbol:ALLOWED_CHARS){
			if (convertedString.equals(symbol)){
				return true;	
			}
		}
		return false;
	}
	
}
