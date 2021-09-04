import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JFrame;

import org.apache.http.HttpResponse;


public class Main {
	private static String mainSkill = "1";
	private static String mainSkillMinimumValue = "0";
	private static String mainSkillMaximumValue = "0";
	private static String minimumAge = "17";
	private static String maximumAge = "17";
	private static String deadline = "-1";
	
	public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException 
	{
		Connection.login();
		GUI gui = new GUI();
	}
	
	public static String getMainSkill() 
	{
		return mainSkill;
	}
	
	public static void setMainSkill(String skill)
	{
		mainSkill = skill;
	}
	
	public static String getMainSkillMinimumValue() 
	{
		return mainSkillMinimumValue;
	}
	
	public static void setmainSkillMinimumValue(String value)
	{
		mainSkillMinimumValue = value;
	}
	
	public static String getMainSkillMaximumValue() 
	{
		return mainSkillMaximumValue;
	}
	
	public static void setmainSkillMaximumValue(String value)
	{
		mainSkillMaximumValue = value;
	}

	public static String getMinimumAge() 
	{
		return minimumAge;
	}
	
	public static void setMinimumAge(String age)
	{
		minimumAge = age;
	}
	
	public static String getMaximumAge() 
	{
		return maximumAge;
	}
	
	public static void setMaximumAge(String age)
	{
		maximumAge = age;
	}
	
	public static String getDeadline() 
	{
		return deadline;
	}
	
	public static void setDeadline(String dline)
	{
		deadline = dline;
	}


}
