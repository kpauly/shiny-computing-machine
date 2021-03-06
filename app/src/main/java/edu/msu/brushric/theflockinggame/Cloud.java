package edu.msu.brushric.theflockinggame;

import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Gamer on 3/18/2015.
 */
public class Cloud {
//    private static final String MAGIC = "NechAtHa6RuzeR8x";
//    private static final String LOGIN_URL = "http://webdev.cse.msu.edu/~sarteleb/cse476/toucan/toucan-login.php";
//    private static final String LOGOUT_URL = "http://webdev.cse.msu.edu/~sarteleb/cse476/toucan/toucan-logout.php";
//    private static final String NEW_USER_URL = "http://webdev.cse.msu.edu/~sarteleb/cse476/toucan/toucan-new-user.php";
//    private static final String LOAD_URL = "http://webdev.cse.msu.edu/~sarteleb/cse476/toucan/toucan-load-game-state.php";
//    private static final String SAVE_URL = "http://webdev.cse.msu.edu/~sarteleb/cse476/toucan/toucan-store-game-state.php";
//    private static final String POLLING_URL = "http://webdev.cse.msu.edu/~sarteleb/cse476/toucan/toucan-get-game-state.php";
//    private static final String GAME_OVER_URL = "http://webdev.cse.msu.edu/~sarteleb/cse476/toucan/toucan-game-over.php";
    private static final String MAGIC = "NechAtHa6RuzeR8x";
    private static final String LOGIN_URL = "http://webdev.cse.msu.edu/~brushric/cse476/toucan/toucan-login.php";
    private static final String LOGOUT_URL = "http://webdev.cse.msu.edu/~brushric/cse476/toucan/toucan-logout.php";
    private static final String NEW_USER_URL = "http://webdev.cse.msu.edu/~brushric/cse476/toucan/toucan-new-user.php";
    private static final String LOAD_URL = "http://webdev.cse.msu.edu/~brushric/cse476/toucan/toucan-load-game-state.php";
    private static final String SAVE_URL = "http://webdev.cse.msu.edu/~brushric/cse476/toucan/toucan-store-game-state.php";
    private static final String POLLING_URL = "http://webdev.cse.msu.edu/~brushric/cse476/toucan/toucan-get-game-state.php";
    private static final String GAME_OVER_URL = "http://webdev.cse.msu.edu/~brushric/cse476/toucan/toucan-game-over.php";
    private static final String UTF8 = "UTF-8";

    /**
     * Skip the XML parser to the end tag for whatever
     * tag we are currently within.
     * @param xml the parser
     * @throws java.io.IOException
     * @throws org.xmlpull.v1.XmlPullParserException
     */
    public static void skipToEndTag(XmlPullParser xml)
            throws IOException, XmlPullParserException {
        int tag;
        do
        {
            tag = xml.next();
            if(tag == XmlPullParser.START_TAG) {
                //Recurse over any start tag
                skipToEndTag(xml);
            }
        } while(tag != XmlPullParser.END_TAG &&
                tag != XmlPullParser.END_DOCUMENT);
    }

    /**
     * Login as a user in the cloud.
     * @return reference to an input stream or null if this fails
     */
    public boolean loginUser(String user, String password) {
        // Create a get query
        String query = LOGIN_URL + "?user=" + user + "&magic=" + MAGIC + "&password=" + password;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            InputStream stream = conn.getInputStream();

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);

                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "toucan");

                String status = xmlR.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return false;
                }

                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }
            stream.close();
            return true;

        } catch (MalformedURLException e) {
            // Should never happen
            return false;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Add a user to the database.
     * @return reference to an input stream or null if this fails
     */
    public boolean addNewUser(String user, String password) {
        // Create a get query
        String query = NEW_USER_URL + "?user=" + user + "&magic=" + MAGIC + "&password=" + password;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            InputStream stream = conn.getInputStream();

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);

                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "toucan");

                String status = xmlR.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return false;
                }

                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }
            stream.close();
            return loginUser(user, password);

        } catch (MalformedURLException e) {
            // Should never happen
            return false;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Login as a user in the cloud.
     * @return reference to an input stream or null if this fails
     */
    public boolean logoutUser(String user) {
        // Create a get query
        String query = LOGOUT_URL + "?user=" + user + "&magic=" + MAGIC;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            InputStream stream = conn.getInputStream();

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);

                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "toucan");

                String status = xmlR.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return false;
                }

                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }
            stream.close();
            return true;

        } catch (MalformedURLException e) {
            // Should never happen
            return false;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Grabs the current state of the game board
     * @return reference to an input stream or null if this fails
     */
    public InputStream loadBoard(){
        // Create a get query
        String query = LOAD_URL +  "?magic=" + MAGIC;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream stream = conn.getInputStream();
            return stream;

        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Saves the current state of the game bird
     * @return True if the save works, false otherwise
     */
    public boolean saveBoard(GameManager manager) {
        // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        try {
            xml.setOutput(writer);

            xml.startDocument("UTF-8", true);

            //xml.startTag(null, "toucan");
            //xml.attribute(null, "magic", MAGIC);
            manager.saveXml(xml);

            //xml.endTag(null, "toucan");

            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return false;
        }

        final String xmlStr = writer.toString();

        /*
         * Convert the XML into HTTP POST data
         */
        String postDataStr;
        try {
            postDataStr = "xml=" + URLEncoder.encode(xmlStr.substring(56), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return false;
        }

        /*
         * Send the data to the server
         */
        //byte[] postData = postDataStr.getBytes();

        InputStream stream = null;
        try {
            String win = manager.getWinner();
            if(win.equals("")){
                win = "0";
            }else{
                win = "1";
            }
            String user = manager.getUsername();
            String query = SAVE_URL + "?user=" + user + "&win=" + win + "&magic=" + MAGIC + "&"+postDataStr;
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setDoOutput(true);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
//            conn.setUseCaches(false);
//
//            OutputStream out = conn.getOutputStream();
//            out.write(postData);
//            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

//            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                Log.i("hatter", line);
//            }

            stream = conn.getInputStream();

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);

                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "toucan");

                String status = xmlR.getAttributeValue(null, "status");
                String msg = xmlR.getAttributeValue(null, "msg");
                if(status.equals("no")) {
                    return false;
                }

                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }
        return true;
    }

    /**
     * Polls the server waiting for the board to be updated
     * @return true if the board has been updated, false otherwise
     */
    public String serverPoll(String user) {
        // Create a get query
        String query = POLLING_URL + "?magic=" + MAGIC + "&user=" + user;
        String result = "";
        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                result = "no";
            }

            InputStream stream = conn.getInputStream();

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);

                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "toucan");

                String status = xmlR.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    result = "no";
                }else if(status.equals("yes")) {
                    result = "yes";
                }else if(status.equals("winner")) {
                    result = "winner";
                }else if(status.equals("update")) {
                    result = "update";
                }

                return result;
                // We are done
            } catch(XmlPullParserException ex) {
                result = "no";
            } catch(IOException ex) {
                result = "no";
            }
            stream.close();
            result = "yes";
        } catch (MalformedURLException e) {
            // Should never happen
            result = "no";
        } catch (IOException ex) {
            result = "no";
        }
        return result;
    }

    /**
     * Tells the server to clear the active users table and the game table
     * @return true if it worked, false otherwise
     */
    public boolean gameOver() {
        // Create a get query
        String query = GAME_OVER_URL + "?magic=" + MAGIC;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            InputStream stream = conn.getInputStream();

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);

                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "toucan");

                String status = xmlR.getAttributeValue(null, "status");
                if(status.equals("no")) {
                    return false;
                }

                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }
            stream.close();
            return true;

        } catch (MalformedURLException e) {
            // Should never happen
            return false;
        } catch (IOException ex) {
            return false;
        }
    }
}
