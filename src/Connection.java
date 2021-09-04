import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;


public class Connection 
{
	static HttpClient httpclient = new DefaultHttpClient();
	static HttpContext localContext = new BasicHttpContext();
	static CookieStore cookieStore = new BasicCookieStore();
	static String serverNummer = "";
	
	public static void login() throws MalformedURLException, IOException
	{
		// INSTELLINGEN (HANDLE REDIRECTS OP FALSE ZETTEN IS KUTTEBELANGRIJK!!!)
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);
		httpclient.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS,false);
		
		// Get request op hattrick.org
		HttpResponse getResponse = getRequest("http://www.hattrick.org/",true);
		String getStringResponse = getStringResponse(getResponse);
		
		
		String loginURL = "http://www.hattrick.org/Default.aspx";
		
		// Get basic inputs
		ArrayList<NameValuePair> inputs = getBasicInputs(loginURL);
		
		// Get other inputs
		String name1 = "ctl00$ctl00$CPContent$ucSubMenu$ucLogin$txtUserName";
		String username = "";  // Removed for security purposes.
		String name2 = "ctl00$ctl00$CPContent$ucSubMenu$ucLogin$txtPassword";
		String password = "";  // Removed for security purposes.
		String name3 = "ctl00$ctl00$CPContent$ucSubMenu$ucLogin$butLogin";
		String value3 = "Login";
		inputs.add(new BasicNameValuePair(name1, username));
		inputs.add(new BasicNameValuePair(name2, password));
		inputs.add(new BasicNameValuePair(name3, value3));
		
		// Post request
		HttpResponse loginResponse = postRequest(loginURL, inputs,true);
		String loginStringResponse = getStringResponse(loginResponse);
		serverNummer = loginResponse.getHeaders("Location")[0].getValue().substring(10,12);
		// Follow redirection
		HttpResponse redirectionResponse = followRedirection(loginResponse);
		String redirectionStringResponse = getStringResponse(redirectionResponse);
		
		// Follow redirection
		HttpResponse redirectionResponse2 = followRedirection(redirectionResponse);
		String redirectionStringResponse2 = getStringResponse(redirectionResponse2);
	
	}
	
	/**
	 * PAS OP DAT JE GELDIGE WAARDEN INGEEFT, ANDERS HATTRICK ERROR!
	 * 
	 * Geeft source code terug van de transferlijst
	 */
	public static String searchTransferList(String mainSkill, String mainSkillMinimumValue, String mainSkillMaximumValue, String minimumAge, String maximumAge, String deadline) throws ClientProtocolException, IOException
	{
		String transferURL = "http://www" + serverNummer + ".hattrick.org" + "/World/Transfers/default.aspx";
		ArrayList<NameValuePair> inputs = getBasicInputs(transferURL);		
		
		String name1 = "ctl00$ctl00$CPContent$CPMain$butSearch";
		String value1 = "Zoeken";
		String name2 = "ctl00$ctl00$CPContent$CPMain$chkShowAdvanced";
		String value2 = "on";
		String name3 = "ctl00$ctl00$CPContent$CPMain$chkUseGlobalMax";
		String value3 = "on";
		String name4 = "ctl00$ctl00$CPContent$CPMain$ddlAgeMin";
		// Age
		String value4 = minimumAge;
		String name5 = "ctl00$ctl00$CPContent$CPMain$ddlAgeMax";
		String value5 = maximumAge;
		// Geboren in
		String name6 = "ctl00$ctl00$CPContent$CPMain$ddlBornIn";
		String value6 = "-1";
		// Deadline
		String name7 = "ctl00$ctl00$CPContent$CPMain$ddlDeadline";
		String value7 = deadline;
		/// Snap niet goed wat dit doet
		String name8 = "ctl00$ctl00$CPContent$CPMain$ddlGlobalSkillMax";
		String value8 = "-1";
		// Skill 1
		String name9 = "ctl00$ctl00$CPContent$CPMain$ddlSkill1";
		String value9 = mainSkill;
		String name10 = "ctl00$ctl00$CPContent$CPMain$ddlSkill1Min";
		String value10 = mainSkillMinimumValue;
		String name11 = "ctl00$ctl00$CPContent$CPMain$ddlSkill1Max";
		String value11 = mainSkillMaximumValue;
		
		// Skill 2 // PASSEN MAXIMUM MATIG (tenzij mainskill = passen ---> if/else)
		String name12 = ""; String value12 = ""; String name13 = ""; String value13 = ""; String name14 = ""; String value14 = "";
		if(!mainSkill.equalsIgnoreCase("8"))
		{
			name12 = "ctl00$ctl00$CPContent$CPMain$ddlSkill2";
			value12 = "8";
			name13 = "ctl00$ctl00$CPContent$CPMain$ddlSkill2Min";
			value13 = "-1";
			name14 = "ctl00$ctl00$CPContent$CPMain$ddlSkill2Max";
			value14 = "5";
		}
		else
		{
			name12 = "ctl00$ctl00$CPContent$CPMain$ddlSkill2";
			value12 = "-1";
			name13 = "ctl00$ctl00$CPContent$CPMain$ddlSkill2Min";
			value13 = "-1";
			name14 = "ctl00$ctl00$CPContent$CPMain$ddlSkill2Max";
			value14 = "-1";
		}

		// Skill 3 // SPELMAKEN MAXIMUM MATIG (tenzij mainskill = spelmaken ---> if/else)
		String name15 = ""; String value15 = ""; String name16 = ""; String value16 = ""; String name17 = ""; String value17 = "";
		if(!mainSkill.equalsIgnoreCase("4"))
		{
			name15 = "ctl00$ctl00$CPContent$CPMain$ddlSkill3";
			value15 = "4";
			name16 = "ctl00$ctl00$CPContent$CPMain$ddlSkill3Min";
			value16 = "-1";
			name17 = "ctl00$ctl00$CPContent$CPMain$ddlSkill3Max";
			value17 = "5";
		}
		else
		{
			name15 = "ctl00$ctl00$CPContent$CPMain$ddlSkill3";
			value15 = "-1";
			name16 = "ctl00$ctl00$CPContent$CPMain$ddlSkill3Min";
			value16 = "-1";
			name17 = "ctl00$ctl00$CPContent$CPMain$ddlSkill3Max";
			value17 = "-1";
		}
		// Skill 4 // VLEUGELSPEL MAXIMUM MATIG (tenzij mainskill = vleugelspel ---> if/else)
		String name18 = ""; String value18 = ""; String name19 = ""; String value19 = ""; String name20 = ""; String value20 = "";
		if(!mainSkill.equalsIgnoreCase("5"))
		{
			name18 = "ctl00$ctl00$CPContent$CPMain$ddlSkill4";
			value18 = "5";
			name19 = "ctl00$ctl00$CPContent$CPMain$ddlSkill4Min";
			value19 = "-1";
			name20 = "ctl00$ctl00$CPContent$CPMain$ddlSkill4Max";
			value20 = "5";
		}
		else
		{
			name18 = "ctl00$ctl00$CPContent$CPMain$ddlSkill4";
			value18 = "-1";
			name19 = "ctl00$ctl00$CPContent$CPMain$ddlSkill4Min";
			value19 = "-1";
			name20 = "ctl00$ctl00$CPContent$CPMain$ddlSkill4Max";
			value20 = "-1";
		}
		// Specialiteit
		String name22 = "ctl00$ctl00$CPContent$CPMain$ddlSpecialty";
		String value22 = "-1";
		// Zone
		String name23 = "ctl00$ctl00$CPContent$CPMain$ddlZone";
		String value23 = "0";
		
		String name24 = "ctl00$ctl00$CPContent$CPMain$rdSort";
		String value24 = "ASC";
		String name25 = "ctl00$ctl00$CPContent$CPMain$txtBidMin";
		String value25 = "0";
		String name26 = "ctl00$ctl00$CPContent$CPMain$txtBidMax";
		String value26 = "0";
		String name27 = "ctl00$ctl00$CPContent$CPMain$txtTSIMin";
		String value27 = "0";
		String name28 = "ctl00$ctl00$CPContent$CPMain$txtTSIMax";
		String value28 = "0";
		// NIET GEDAAN : "ctl00_ctl00_CPContent_CPMain_txtBidMax_ClientState"
		String name29 = "ctl00_ctl00_CPContent_CPMain_txtBidMax_text";
		String value29 = "0";
		String name30 = "ctl00_ctl00_CPContent_CPMain_txtBidMin_text";
		String value30 = "0";
		String name31 = "ctl00_ctl00_CPContent_CPMain_txtTSIMin_text";
		String value31 = "0";
		String name32 = "ctl00_ctl00_CPContent_CPMain_txtTSIMax_text";
		String value32 = "0";
		inputs.add(new BasicNameValuePair(name1,value1));
		inputs.add(new BasicNameValuePair(name2,value2));
		inputs.add(new BasicNameValuePair(name3,value3));
		inputs.add(new BasicNameValuePair(name4,value4));
		inputs.add(new BasicNameValuePair(name5,value5));
		inputs.add(new BasicNameValuePair(name6,value6));
		inputs.add(new BasicNameValuePair(name7,value7));
		inputs.add(new BasicNameValuePair(name8,value8));
		inputs.add(new BasicNameValuePair(name9,value9));
		inputs.add(new BasicNameValuePair(name10,value10));
		inputs.add(new BasicNameValuePair(name11,value11));
		inputs.add(new BasicNameValuePair(name12,value12));
		inputs.add(new BasicNameValuePair(name13,value13));
		inputs.add(new BasicNameValuePair(name14,value14));
		inputs.add(new BasicNameValuePair(name15,value15));
		inputs.add(new BasicNameValuePair(name16,value16));
		inputs.add(new BasicNameValuePair(name17,value17));
		inputs.add(new BasicNameValuePair(name18,value18));
		inputs.add(new BasicNameValuePair(name19,value19));
		inputs.add(new BasicNameValuePair(name20,value20));
		inputs.add(new BasicNameValuePair(name22,value22));
		inputs.add(new BasicNameValuePair(name23,value23));
		inputs.add(new BasicNameValuePair(name24,value24));
		inputs.add(new BasicNameValuePair(name25,value25));
		inputs.add(new BasicNameValuePair(name26,value26));
		inputs.add(new BasicNameValuePair(name27,value27));
		inputs.add(new BasicNameValuePair(name28,value28));
		inputs.add(new BasicNameValuePair(name29,value29));
		inputs.add(new BasicNameValuePair(name30,value30));
		inputs.add(new BasicNameValuePair(name31,value31));
		inputs.add(new BasicNameValuePair(name32,value32));
		
		HttpResponse transferResponse = postRequest(transferURL,inputs,true);
		String transferStringResponse = getStringResponse(transferResponse);
		// Follow redirection
		HttpResponse redirectionResponse = followRedirection(transferResponse);
		String redirectionStringResponse = getStringResponse(redirectionResponse);
		
		return redirectionStringResponse;
	}
	
	
	public static void mail() throws ClientProtocolException, IOException
	{
		String mailURL = "http://www" + serverNummer + ".hattrick.org/MyHattrick/Inbox/Default.aspx?actionType=newMail";
		ArrayList<NameValuePair> inputs = getBasicInputs(mailURL);
		String name1 = "ctl00$ctl00$CPContent$CPMain$tbTo";
		String username = "";  // Removed for security purposes.
		String name2 = "ctl00$ctl00$CPContent$CPMain$tbSubject";
		String value2 = "Onderwerp";
		String name3 = "ctl00$ctl00$CPContent$CPMain$ucEditorMain$txtBody";
		String value3 = "Tekst";
		String name4 = "ctl00$ctl00$CPContent$CPMain$btnSendNew";
		String value4 = "Versturen";
		inputs.add(new BasicNameValuePair(name1,username));
		inputs.add(new BasicNameValuePair(name2,value2));
		inputs.add(new BasicNameValuePair(name3,value3));
		inputs.add(new BasicNameValuePair(name4,value4));
		
		HttpResponse response = postRequest(mailURL, inputs,true);
		String stringResponse = getStringResponse(response);
	}
	
	/**
	 * Geeft de inputs "__VIEWSTATE", "__EVENTVALIDATION" en "ctl00_ctl00_sm_HiddenField" terug.
	 */
	public static ArrayList<NameValuePair> getBasicInputs(String URL) throws ClientProtocolException, IOException
	{
		ArrayList<NameValuePair> inputs = new ArrayList<NameValuePair>();
		
		//// Get request
		HttpResponse response = getRequest(URL,false);
		String source = getStringResponse(response);
		
		//// Get "ctl00_ctl00_sm_HiddenField"
		// Get Telerik-link
		int telerikBeginIndex = source.indexOf("src=\"/Telerik") + 5;
		int telerikEndIndex = source.indexOf(" ",telerikBeginIndex) - 1;
		String telerikLink = source.substring(telerikBeginIndex, telerikEndIndex);
		// Decode Telerik-link
		String decoded = URLDecoder.decode(telerikLink);
		// Extract hiddenfieldValue from Telerik)link
		try
		{
			int hiddenfieldBeginIndex = decoded.indexOf(";;System.Web.Extensions");
			String hiddenfieldValue = decoded.substring(hiddenfieldBeginIndex);
			inputs.add(new BasicNameValuePair("ctl00_ctl00_sm_HiddenField",hiddenfieldValue));
		}
		catch(StringIndexOutOfBoundsException s)
		{}
		
		//// Get "__VIEWSTATE" en "__EVENTVALIDATION" 
		int viewstateIndex = source.indexOf("__VIEWSTATE");
		int viewstateBeginIndex = source.indexOf("value=", viewstateIndex) + 7;
		int viewstateEndIndex =  source.indexOf(" ", viewstateBeginIndex) - 1;
		String viewstateValue = source.substring(viewstateBeginIndex, viewstateEndIndex);
		int eventvalidationIndex = source.indexOf("__EVENTVALIDATION");
		int eventvalidationBeginIndex = source.indexOf("value=", eventvalidationIndex) + 7;
		int eventvalidationEndIndex =  source.indexOf(" ", eventvalidationBeginIndex) - 1;
		String eventvalidationValue = source.substring(eventvalidationBeginIndex, eventvalidationEndIndex);
		
		inputs.add(new BasicNameValuePair("__VIEWSTATE",viewstateValue));
		inputs.add(new BasicNameValuePair("__EVENTVALIDATION",eventvalidationValue));
		
		return inputs;
	}

	
	/**
	 * Has always to be followed by getStringResponse(HttpResponse response);
	 * Otherwise you get an IllegalStateException
	 */
	public static HttpResponse getRequest(String URL,boolean printResponse) throws ClientProtocolException, IOException
	{
		HttpGet httpget = new HttpGet(URL);
		//setHeaders(httpget);
		HttpResponse response = httpclient.execute(httpget,localContext);
		//setCookies(response);
		
		HttpEntity entity = response.getEntity();
		if(printResponse)
		{
			System.out.println(response.toString());
		}
		return response;
	}
	
	/**
	 * Has always to be followed by getStringResponse(HttpResponse response);
	 * Otherwise you get an IllegalStateException
	 */
	public static HttpResponse postRequest(String URL,ArrayList<NameValuePair> inputs,boolean printResponse) throws MalformedURLException, IOException
	{
		HttpPost httppost = new HttpPost(URL);
		//setHeaders(httppost);
		httppost.setEntity(new UrlEncodedFormEntity(inputs, "UTF-8"));
		HttpResponse response = httpclient.execute(httppost,localContext);
		//setCookies(response);
		if(printResponse)
		{
			System.out.println(response.toString());
		}
		return response;		
	}
	
	/**
	 * Has always to be followed by getStringResponse(HttpResponse response);
	 * Otherwise you get an IllegalStateException
	 */
	public static HttpResponse followRedirection(HttpResponse response) throws ClientProtocolException, IOException
	{
		// Parse Response to get location of redirection
		Header locationHeader = response.getHeaders("Location")[0];
		String redirection = "";
		if(locationHeader.getValue().startsWith("http")) // Absolute location header
		{
			redirection = locationHeader.getValue();
		}
		else // Relatieve location header
		{
			redirection = "http://www" + serverNummer + ".hattrick.org" + locationHeader.getValue();
		}
		// Get request op redirection
		HttpResponse redirectionResponse = getRequest(redirection,true);
		
		return redirectionResponse;
	}
	
	public static String getStringResponse(HttpResponse response) throws UnsupportedEncodingException, IllegalStateException, IOException
	{
		HttpEntity entity = response.getEntity();
		StringBuilder sb = new StringBuilder(); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8")); 
	    int byteR;
        while((byteR = reader.read()) != -1) 
        {
            char ch = (char) byteR;
            sb.append(ch);
        }
        
        if (entity != null) 
		{
			entity.consumeContent();
		}
        
        String stringResponse = sb.toString();
        return stringResponse;
	}
	
	public static void setHeaders(HttpPost httppost)
	{
		httppost.addHeader("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.22) Gecko/20110905 Ubuntu/10.10 (maverick) Firefox/3.6.22");
		httppost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httppost.addHeader("Accept-Language", "en-us,en;q=0.5");
		//httppost.addHeader("Accept-Encoding","gzip,deflate");
		httppost.addHeader("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		httppost.addHeader("Keep-Alive","115");
		httppost.addHeader("Connection","keep-alive");
		httppost.addHeader("Host","www" + serverNummer + ".hattrick.org");
	}
	
	public static void setHeaders(HttpGet httpget)
	{
		httpget.addHeader("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.22) Gecko/20110905 Ubuntu/10.10 (maverick) Firefox/3.6.22");
		httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpget.addHeader("Accept-Language", "en-us,en;q=0.5");
		//httpget.addHeader("Accept-Encoding","gzip,deflate");
		httpget.addHeader("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		httpget.addHeader("Keep-Alive","115");
		httpget.addHeader("Connection","keep-alive");
		httpget.addHeader("Host","www" + serverNummer + ".hattrick.org");
	}

}
