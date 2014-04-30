package coyote;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Utility class to assist with loading system properties for the application.
 */
public class CfgUtil
{

  /** System property which specifies the user name for the proxy server */
  public static final String PROXY_USER = "http.proxyUser";

  /** System property which specifies the user password for the proxy server */
  public static final String PROXY_PASSWORD = "http.proxyPassword";

  /** System property which specifies the proxy server host name */
  public static final String PROXY_HOST = "http.proxyHost";

  /** Name of the property file to load */
  public static final String PROPERTY_FILENAME = "application.properties";

  /** Our logger */
  private static final Log LOG = LogFactory.getLog( CfgUtil.class );




  /**
   * Load the application properties file from the class path into the system 
   * properties and load a network authenticator if the system properties 
   * contain HTTP proxy settings.
   */
  public static void loadProperties()
  {
    loadPropertiesIntoSystem();
    installProxyAuthenticatorIfNeeded();
  }




  /**
   * Look in the classpath for our property file and load it in
   */
  private static void loadPropertiesIntoSystem()
  {
    Properties props = new Properties();

    try
    {
      props.load( CfgUtil.class.getClassLoader().getResourceAsStream( PROPERTY_FILENAME ) );
      System.getProperties().putAll( props );
    }
    catch( IOException e )
    {
      String msg = "Failed to read from " + PROPERTY_FILENAME;
      LOG.error( msg + ": " + e.getMessage() );
    }

  }




  /**
   * Load the Java proxy authenticator if the are system properties specifying 
   * a proxy host and user name. A network proxy can prevent this JVM from 
   * connecting to external services.
   */
  private static void installProxyAuthenticatorIfNeeded()
  {
    final String user = System.getProperty( PROXY_USER );
    final String password = System.getProperty( PROXY_PASSWORD );
    final String host = System.getProperty( PROXY_HOST );
    if( !isBlank( user ) && !isBlank( password ) && !isBlank( host ) )
    {
      LOG.info( String.format( "Detected HTTP proxy settings (%s@%s), creating network authenticator", user, host ) );
      Authenticator.setDefault( new Authenticator()
      {
        @Override
        protected PasswordAuthentication getPasswordAuthentication()
        {
          return new PasswordAuthentication( user, password.toCharArray() );
        }
      } );
    }
  }




  /**
   * Simple utility method to check for blank, null or empty strings.
   *  
   * @param str The string to check
   * 
   * @return True if the string is null,blank, empty or whitespace.
   */
  public static boolean isBlank( String str )
  {
    int strLen;
    if( str == null || ( strLen = str.length() ) == 0 )
    {
      return true;
    }
    for( int i = 0; i < strLen; i++ )
    {
      if( ( Character.isWhitespace( str.charAt( i ) ) == false ) )
      {
        return false;
      }
    }
    return true;
  }
}
