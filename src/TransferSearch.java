import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class TransferSearch {
	// SourceCode of the first page
	String sourceCodeFirstPage;
	ArrayList<Player> players;
	static String minWage;
	static String maxPrice;

	public TransferSearch(String sourceCode, String minWage, String maxPrice)
			throws ClientProtocolException, IOException {
		sourceCodeFirstPage = sourceCode;
		this.minWage = minWage;
		this.maxPrice = maxPrice;
		players = new ArrayList<Player>();
		extractAllPlayers();
	}

	public void extractAllPlayers() throws ClientProtocolException, IOException {
		// Extract players of the first page
		extractPlayers(sourceCodeFirstPage);
		
		// Extract players form subsequetn pages
		int index = sourceCodeFirstPage.indexOf("Pagina 1 van ");
		int index2 = sourceCodeFirstPage.indexOf(", resultaat 1", index);
		int numberOfPages = Integer.valueOf(
				sourceCodeFirstPage.substring(index + 13, index2)).intValue();
		System.out.println("numberOfPages: " + numberOfPages);
		for (int i = 1; i < numberOfPages; i++) 
		{
			String pageSource = getNextTransferPage(i);
			extractPlayers(pageSource);
		}
	}

	public void extractPlayers(String sourceCode) throws ClientProtocolException, IOException 
	{
		String[] playerLinks = extractPlayerLinks(sourceCode);
		
		for (int i = 0; i < playerLinks.length; i++) 
		{
			// Get playerID uit playerLink
			int beginIndex = playerLinks[i].indexOf("PlayerID=") + 9;
			int endIndex = playerLinks[i].indexOf("&amp;");
			String ID = playerLinks[i].substring(beginIndex, endIndex);

			// GetRequest op playerlink
			String getURL = "http://www" + Connection.serverNummer
					+ ".hattrick.org" + playerLinks[i];
			HttpResponse response = Connection.getRequest(getURL, false);
			String stringResponse = Connection.getStringResponse(response);

			// 20% bonus
			boolean bonus = true;
			if (stringResponse.indexOf("inclusief 20% bonus") == -1) {
				bonus = false;
			}

			// Get Wage
			int index = stringResponse.indexOf("&nbsp;€/week");
			String wageString = stringResponse.substring(index - 10, index);
			// Remove whitespace
			String wageNoWhiteSpace = Miscellaneous
					.removeWhiteSpace(wageString);
			int wage = Integer.parseInt(wageNoWhiteSpace);

			// Edit wage voor bonus
			if (bonus == true) {
				wage = (int) ((int) wage / 1.2);
			}
			
			// Get Price
			int index1 = stringResponse.indexOf("<input name=\"ctl00$ctl00$CPContent$CPMain$txtBid\"");
			boolean deadLinePassed = false;
			String priceNoWhiteSpace = "";
			if(index1 == -1)
			{
				deadLinePassed = true;
				// Prijs zeer hoog zetten zodat speler niet wordt toegevoegd(deadline passed)
				priceNoWhiteSpace = "100000000";
			}
			else
			{
				int index2 = stringResponse.indexOf("value=\"",index1);
				int index3 = stringResponse.indexOf("\" ",index2);
				String priceString = stringResponse.substring(index2 + 7,index3);
				priceNoWhiteSpace = Miscellaneous.removeWhiteSpace(priceString);
			}
			int priceInt = Integer.parseInt(priceNoWhiteSpace);
			if(wage >= Integer.parseInt(minWage) && priceInt <= Integer.parseInt(maxPrice))
			{
				Player player = new Player(wage, ID, playerLinks[i]);
				players.add(player);
			}
		}
	}

	public String[] extractPlayerLinks(String sourceCode) throws ClientProtocolException, IOException 
	{
		// Array[1] - Array[25] == 25 playerStrings
		String regex = "<div class=\"transferPlayerInfo\">";
		String test = sourceCode.substring(sourceCode.indexOf(regex));
		String[] array = test.split(regex);
		String[] playerLinks = new String[array.length -1];
		for (int i = 1; i < array.length; i++) 
		{
			int beginIndex = array[i]
					.indexOf("/Club/Players/Player.aspx?PlayerID=");
			int endIndex = array[i].indexOf("\" title", beginIndex);
			String playerLink = array[i].substring(beginIndex, endIndex);
			playerLinks[i - 1] = playerLink;
		}
		return playerLinks;
	}

	public String getNextTransferPage(int pageNumber) throws ClientProtocolException,IOException 
	{
		String postURL = "http://www" + Connection.serverNummer + ".hattrick.org" + "/World/Transfers/TransfersSearchResult.aspx";
		ArrayList<NameValuePair> inputs = Connection.getBasicInputs(postURL);	
		
		String pageNumberString = Integer.toString(pageNumber);
		String string2 = "";
		if(pageNumber >= 1 && pageNumber <= 10)
		{
			string2 = "0" + pageNumberString;
			if(pageNumber == 10)
			{
				string2 = pageNumberString;
			}
		}
		else
		{
			int temp = Integer.valueOf(pageNumberString.substring(1,2)) + 1;
			string2 = "0" + Integer.toString(temp);
			if(temp == 10)
			{
				string2 = "11";
			}
		}
		String name1 = "__EVENTTARGET";
		String value1 = "ctl00$ctl00$CPContent$CPMain$ucPager$repPages$ctl" + string2 + "$p" + pageNumberString;
		System.out.println(value1);
		inputs.add(new BasicNameValuePair(name1, value1));

		HttpResponse response = Connection.postRequest(postURL, inputs, false);
		String stringResponse = Connection.getStringResponse(response);
		return stringResponse;

	}

	public String getSourceCode() {
		return sourceCodeFirstPage;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public static void setMinWage(String wage) {
		minWage = wage;
	}

	public static void setMaxPrice(String price) {
		maxPrice = price;
	}
}
