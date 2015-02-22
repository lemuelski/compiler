import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Tester implements Scanner.OnScannedCallback{

	//initialize final variable on constructor
	private final String FILEPATH;
	
	private final String RESERVED_WORDS[] = {"Oo", "Hindi", "ENROLL", "GRADUATE", "AB", "Law", "AMV", "Eng", "Eccle", "ShiftSa"
	                                       	,"PASOK", "LABAS", "PresentBa", "LateBa", "EdiAbsent", "Uwi",
	                                       	"Exam", "Tama", "Bonus", "Mali", "Oo", "Hindi", "Balog",  
	                                       	"Presec", "Assessment"};
	
	private final String RESERVED_WORDS_TOKEN[] = {"BOOL_CON","BOOL_CON", "BEGIN", "END", "STRING", "BOOLEAN", "INTEGER", "FLOAT", "CHARACTER", "CONVERTER"
													,"INPUT", "OUTPUT", "IF", "ELSE-IF", "ELSE", "END-IF",
													"SWITCH", "CASE", "DEFAULT", "BREAK", "TRUE", "FALSE", "FINAL",  
													"GETCHAR", "SUBSTRING"};
	
	private final String OPERATORS[] = {"<",">",">=","<=","==","!=","&&","||","!","-","+","++","--","*","/","%"
			,"^","&","=","~",",","@@","@~","~@","(",")","{","}",":"};
	
	private final String OPERATORS_TOKEN[] = {"LT","GT","GE","LE","EQ","NE","AND","OR","NOT","MATH_UN","MATH_UN","MATH_T0","MATH_T0","MATH_T2","MATH_T2","MATH_T2"
										,"MATH_T3","CONCAT_OP","ASSIGN_OP","TERMINATOR","COMMA_SYM","LCOM","LBCOM","RBCOM","LPAREN","RPAREN","LBRACE","RBRACE","COLON"};
	
	private Set<String> identifiers;
	
	private Map<String, String> tokenTable; 
	
	private Scanner scanner;
	
	public Tester(String filePath){
		this.FILEPATH = filePath;
		tokenTable = new HashMap<String, String>();
		
		identifiers = new HashSet<String>();
		
		scanner = new Scanner(this, FILEPATH, this);
		populateHashMap();
		scanner.startScan();
	}
	
	public void onTokenFound(String token) {
		if (tokenTable.get(token)!=null){
			System.out.println("["+tokenTable.get(token)+"]");
		}
		else{
			identifiers.add(token);
		}
	}
	
	public void populateHashMap(){
		for (int x = 0; x<RESERVED_WORDS.length;x++){
			tokenTable.put(RESERVED_WORDS[x], RESERVED_WORDS_TOKEN[x]);
		}
		
		for (int x= 0; x<OPERATORS.length; x++){
			tokenTable.put(OPERATORS[x], OPERATORS_TOKEN[x]);
		}
	}
	
	public boolean checkAvailable(String token){
		return tokenTable.get(token)==null?false:true;
	}
}
