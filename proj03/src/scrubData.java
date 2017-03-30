import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class scrubData{
    public static void main(String[] args) throws IOException{
        File inputFile = new File("/Users/kanoutsuyu/Desktop/cs460_proj3/csv/test.csv");
        File tempFile = new File("/Users/kanoutsuyu/Desktop/cs460_proj3/csv/gettest.csv");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String currentLine;
      
        int counter = 0;
        ArrayList<String> records = new ArrayList<String>(); // store
        while((currentLine = reader.readLine()) != null) {
        	String[] field = currentLine.split(",",29);
        	for(int i = 0; i < 28; i++){
		      	if(field[i].matches("\\*")){
//		      		System.out.println("detect asterisk");
		      		field[i] = field[i].replaceAll("\\*", "NULL");
//		      		System.out.println(field[i]);
		      	
		      	}
		      	if(field[i].isEmpty()){//remove that line;
		      		counter++;
		      	}
		      	records.add(field[i]);
		    }
        	String formattedR = records.toString()
        		    .replace("[", "")  //remove the right bracket
        		    .replace("]", "")  //remove the left bracket
        		    .trim(); 
        	if(null!=currentLine && counter==0){
        		writer.write(formattedR);
        		writer.write("\n");
        	}
        	records = new ArrayList<String>();
        	counter=0;
        }
        writer.close(); 
        reader.close(); 
    }
}