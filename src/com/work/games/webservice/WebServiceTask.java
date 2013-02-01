package com.work.games.webservice;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.andengine.util.debug.Debug;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.work.games.hopeplus.HopeGenerator;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class WebServiceTask extends AsyncTask<Void, Void, String> {
	private static final String HOST_URL = "http://jakkugames.host22.com";
	private static final String MESSAGE_PATH = "/application/hopeplus/get_message.php";
	private Context mContext;
	private HopeGenerator mHopeG;
	
	public WebServiceTask(HopeGenerator hope_g) {
		mHopeG = hope_g;
		mContext = mHopeG.getContext();
	}
	
	public String getMessageFromURL() {
		String response = "";
        try {
            // Instantiate an HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(HOST_URL + MESSAGE_PATH);
       
            try {
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response = httpclient.execute(httppost, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Throwable t) {
            Toast.makeText(mContext, t.toString(), Toast.LENGTH_LONG).show();
        }
        return response ;
	}
	
	public String processResponse(String response) {
        if (response.length() < 1) return "";
        int scriptFound = response.indexOf("<!--") ;
        if (scriptFound != -1 ) response = response.substring(0, scriptFound) ;
       
        Document doc = XMLfromString(response);
        NodeList node_msg = doc.getElementsByTagName("message");
        NodeList node_author = doc.getElementsByTagName("author");
        if (node_msg.getLength() > 0) {
            Node node = node_msg.item(0) ;
            String message = node.getFirstChild().getNodeValue();
            mHopeG.setMessageString(message);
        }
        if(node_author.getLength() > 0) {
        	Node node = node_author.item(0);
        	String author = node.getFirstChild().getNodeValue();
        	mHopeG.setAuthorString(author);
        }
        return "";
    }

    public Document XMLfromString(String xml){
            Document doc = null;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false) ;
            try {
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    InputSource is = new InputSource();
                    is.setCharacterStream(new StringReader(xml));
                    doc = db.parse(is);
                    } catch (ParserConfigurationException e) {
                            Debug.w("XML parse error: " + e.getMessage());
                            return null;
                    } catch (SAXException e) {
                            Debug.w("Wrong XML file structure: " + e.getMessage());
                return null;
                    } catch (IOException e) {
                            Debug.w("I/O exeption: " + e.getMessage());
                            return null;
                    }
            return doc;
    }

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		processResponse(getMessageFromURL());
		if(isCancelled()) return null;
		
		return null;
	}
	
	protected void onPostExecute(String message) {
		mHopeG.outMessage();

	}

}
