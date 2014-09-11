import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

/**
 * This class is used to create, store, delete and retrieve text keyed in by user
 * User is assumed to type in a valid text file when launching the programme
 * if text file does not exist, a new txt file of the specified name will be created
 * User is assumed to use only "add", "delete", "display", "clear" and "exit" commands
 * User is assumed to enter one command in one line
 * it is assumed that the user does not need to edit existing text
 * It is assumed that only one text file is used with each time the programme is run
 * The command format is given by the example interaction below:

c:> TextBuddy.exe mytextfile.txt  (OR c:>java  TextBuddy mytextfile.txt)
Welcome to TextBuddy. mytextfile.txt is ready for use
command: add little brown fox
added to mytextfile.txt: “little brown fox”
command: display
1. little brown fox
command: add jumped over the moon
added to mytextfile.txt: “jumped over the moon”
command: display
1. little brown fox
2. jumped over the moon
command: delete 2
deleted from mytextfile.txt: “jumped over the moon”
command: display
1. little brown fox
command: clear
all content deleted from mytextfile.txt
command: display
mytextfile.txt is empty
command: exit
c:>

 * @author Tean Zheng Yang
 */

public class TextBuddy {

	// Output Messages
	private static final String MESSAGE_ADDED = "added to %1$s: \"%2$s\"";
	private static final String MESSAGE_DELETED = "deleted from %1$s: \"%2$s\"";
	private static final String MESSAGE_CLEARED = "all content deleted from %1$s";
	private static final String MESSAGE_EMPTY = "%1$s is empty";
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use ";
	private static final String MESSAGE_INVALID_FORMAT = "Invalid Format";
	
	// These are the possible command types
	enum CommandType {
		ADD_LINE, DELETE_LINE, DISPLAY,CLEAR, EXIT, INVALID
	};
	
	// This vector will be use to store the text lines
	private static Vector<String> buffer = new Vector<String>();

	/*
	 * This variable is declared for the whole class (instead of declaring it
	 * inside the readUserCommand() method to facilitate automated testing using
	 * the I/O redirection technique. If not, only the first line of the input
	 * text file will be processed.
	 */
	private static Scanner scanner = new Scanner(System.in);
	
	// To keep track of the text file used
	private static String filePath;

	public static void main(String[] args) {
		filePath="test.txt";
		showToUser(MESSAGE_WELCOME,filePath,"");
		openFile(filePath);
		while (true) {
			System.out.print("command: ");
			String command = scanner.nextLine();
			executeCommand(command);
			saveFile(filePath);
		}
	}
	
	private static void showToUser(String text, String file, String linetext) {
		System.out.println(String.format(text,file,linetext));
	}

	private static void openFile(String filePath) {
		try { // check if file exist if not create a new file with that name
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				buffer.add(line);
			}
			fileReader.close();
		} catch (IOException e) {
				e.printStackTrace();
			}
	}
	

	private static void executeCommand(String userCommand) {
		if (userCommand.trim().equals("")){
			System.out.println(String.format(MESSAGE_INVALID_FORMAT, userCommand));
		}
		else{
			String commandTypeString = getFirstWord(userCommand);
			CommandType commandType = determineCommandType(commandTypeString);
			switch (commandType) {
			case ADD_LINE:
				addLine(userCommand);
				break;
			case DELETE_LINE:
				deleteLine(userCommand);
				break;
			case DISPLAY:
				display();
				break;
			case CLEAR:
				clearAll();
				break;
			case INVALID:
				System.out.println(String.format(MESSAGE_INVALID_FORMAT, userCommand));
				break;
			case EXIT:
				System.exit(0);
				break;
			default:
				//throw an error if the command is not recognized
				throw new Error("Unrecognized command type");
			}
		}
	}

	/**
	 * This operation determines which of the supported command types the user
	 * wants to perform
	 * 
	 * @param commandTypeString is the first word of the user command
	 */
	private static CommandType determineCommandType(String commandTypeString) {
		if (commandTypeString == null){
			throw new Error("command type string cannot be null!");
		}
		if (commandTypeString.equalsIgnoreCase("add")) {
			return CommandType.ADD_LINE;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return CommandType.DELETE_LINE;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return CommandType.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("clear")) {
			return CommandType.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
			return CommandType.EXIT;
		} else {
			return CommandType.INVALID;
		}		
	}
	
	/**
	 * This operation is extract the command
	 */

	private static String getFirstWord(String userCommand) {
		return userCommand.split(" ")[0];
	}

	/**
	 * This operation is used to add new line
	 */
	private static void addLine(String line) {
		buffer.add(line.substring(line.indexOf(' ')+1));
		showToUser(MESSAGE_ADDED,filePath,line.substring(line.indexOf(' ')+1));
	}

	/**
	 * This operation is used to delete line
	 */
	private static void deleteLine(String userCommand) {
		String temp="";
		int lineNumber=Integer.parseInt(userCommand.split(" ")[1])-1;
		if (lineNumber<0||lineNumber>buffer.size()-1){
			System.out.println(String.format(MESSAGE_INVALID_FORMAT, userCommand));
		}
		else{
			temp=buffer.get(lineNumber);
			buffer.remove(lineNumber);
			showToUser(MESSAGE_DELETED,filePath,temp);
		}
	}
	
	/**
	 * This operation is used to clear all lines
	 */
	private static void clearAll(){
		buffer.clear();
		showToUser(MESSAGE_CLEARED,filePath,"");
	}
	
	/**
	 * This operation is used to print out all lines
	 */
	private static void display(){
		for(int i=0; i< buffer.size(); i++){
			String lineToAdd="";
			lineToAdd+=String.valueOf(i+1);
			lineToAdd+=". ";
			lineToAdd+=buffer.get(i);
			System.out.println(lineToAdd);
		}
		if (buffer.size()==0){
			showToUser(MESSAGE_EMPTY,filePath,"");
		}
	}

	/**
	 * This operation is used to save the changes made
	 */
	private static void saveFile(String filePath) {
		File file = new File(filePath);
		file.delete();
		try {
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for(int i=0; i< buffer.size(); i++){
				bw.write(buffer.get(i));
				if (i<buffer.size()-1){
					bw.newLine(); 
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
