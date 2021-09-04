import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;


public class Player 
{
	// Wage subtracted with 20% bonus for foreign players
	private int wage;
	private String ID;
	private String link;
	private int price = 0;
	private String buyerTeamID = "";
	
	public Player(int wage, String ID, String link) throws ClientProtocolException, IOException
	{
		this.wage = wage;
		this.ID = ID;
		this.link = link;
		// Set price and buyerTeamID
		update();
	}
	
	/**
	 * Pas hier maar goed mee op...
	 */
	public void bid(int amount) throws ClientProtocolException, IOException
	{
		String postAddress = "http://www" + Connection.serverNummer + ".hattrick.org/Club/Players/Player.aspx?playerId=" + getID();
		if(amount < 1000000)
		{
			ArrayList<NameValuePair> inputs = Connection.getBasicInputs(postAddress);
			String amountString = Integer.toString(amount);
			String name1 = "ctl00$ctl00$CPContent$CPMain$btnBid";
			String value1 = "Bieden";
			String name2 = "ctl00$ctl00$CPContent$CPMain$txtBid";
			String value2 = amountString;
			inputs.add(new BasicNameValuePair(name1,value1));
			inputs.add(new BasicNameValuePair(name2,value2));
			
			HttpResponse response = Connection.postRequest(postAddress,inputs,true);
			String stringResponse = Connection.getStringResponse(response);
		}
		else
		{
			System.out.println("Meer dan een miljoen, ge zijt zot zeker?!");
		}
	}
	
	/**
	 * Buy a player
	 */
	public void buyPlayer(int maxPrice) throws ClientProtocolException, IOException, InterruptedException
	{
		// Sleep for 2 minutes (in milliseconds)
		boolean deadLinePassed = update();
		while(price <= maxPrice && !deadLinePassed)
		{
			deadLinePassed = update();
			int price = getPrice();
			// Als het hoogste bod niet van FC Vaalbeek is en de prijs onder de maximumprijs ligt en de deadLine niet gepasseerd is
			if(!getBuyerTeamID().equalsIgnoreCase("105181") && price <= maxPrice && !deadLinePassed)
			{
				bid(price);
			}
			Thread.sleep(120000);
		}

	}
	
	/**
	 * Get buyerTeamID and update price and return whether deadline has passed
	 */
	public boolean update() throws ClientProtocolException, IOException
	{
		// GetRequest op playerlink
		String getURL = "http://www" + Connection.serverNummer + ".hattrick.org/Club/Players/Player.aspx?playerId=" + getID();
		HttpResponse response = Connection.getRequest(getURL,false);
		String stringResponse = Connection.getStringResponse(response);
		
		// BuyerTeamID
		int hoogsteBodIndex = stringResponse.indexOf("Hoogste bod: ");
		int hoogsteBodIndex2 = stringResponse.indexOf("&nbsp;€ door ", hoogsteBodIndex);
		if(hoogsteBodIndex != -1)
		{	
			int buyerTeamIDindex = stringResponse.indexOf("<a href=\"/Club/?TeamID=",hoogsteBodIndex2) + 23;
			int buyerTeamIDindex2 = stringResponse.indexOf("\" title=\"",buyerTeamIDindex);
			setBuyerTeamID(stringResponse.substring(buyerTeamIDindex, buyerTeamIDindex2));
		}

		// Plaats een bod (welk bedrag?)
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
		// SetPrice
		setPrice(Integer.parseInt(priceNoWhiteSpace));
		return deadLinePassed;
	}
	
	private void setPrice(int number) 
	{
		price = number;
	}

	private void setBuyerTeamID(String ID)
	{
		buyerTeamID = ID;
	}

	public int getWage()
	{
		return wage;
	}
	
	public String getID()
	{
		return ID;
	}
	
	public String getLink()
	{
		return link;
	}
	
	public int getPrice()
	{
		return price;
	}
	
	public String getBuyerTeamID()
	{
		return buyerTeamID;
	}
}
