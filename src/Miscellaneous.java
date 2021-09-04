import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;


public class Miscellaneous 
{

	/**
	  * Fetch the entire contents of a text file, and return it in a String.
	  * This style of implementation does not throw Exceptions to the caller.
	  *
	  * @param aFile is a file which already exists and can be read.
	  */
	  public static String getContents(File aFile) 
	  {
	    //...checks on aFile are elided
	    StringBuilder contents = new StringBuilder();
	    
	    try 
	    {
	      //use buffering, reading one line at a time
	      //FileReader always assumes default encoding is OK!
	      BufferedReader input =  new BufferedReader(new FileReader(aFile));
	      try 
	      {
		        String line = null; //not declared within while loop
		        /*
		        * readLine is a bit quirky :
		        * it returns the content of a line MINUS the newline.
		        * it returns null only for the END of the stream.
		        * it returns an empty String if two newlines appear in a row.
		        */
		        while (( line = input.readLine()) != null)
		        {
		          contents.append(line);
		          contents.append(System.getProperty("line.separator"));
		        }
	      }
	      
	      finally 
	      {
	        input.close();
	      }
	    }
	    catch (IOException ex)
	    {
	      ex.printStackTrace();
	    }
	    
	    return contents.toString();
	  }

	public static String removeWhiteSpace(String string) 
	{
		char[] chars = string.toCharArray();
		String stringNoWhiteSpace = "";
		for(int j = 0; j < chars.length; j++)
		{	
			if(Character.isDigit(chars[j]))
			{
				stringNoWhiteSpace = stringNoWhiteSpace + chars[j];
			}
		}
		return stringNoWhiteSpace;
	}
}
