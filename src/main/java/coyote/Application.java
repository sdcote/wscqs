package coyote;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import coyote.wx.ForecastReturn;


// This needs to import the

public class Application
{
  private static final Log LOG = LogFactory.getLog( Application.class );



/**
 * This is the main entry point into our application.
 * 
 * @param args a single ZIP code or null to use the default of 43210
 */
  public static void main( String[] args )
  {
    // Load system properties into the runtime and install proxy authenticator 
    // if there are system properties relating to a proxy
    CfgUtil.loadProperties();

    // Use Spring to set everything up through our configuration class
    SpringApplication app = new SpringApplication( Config.class );
    app.setShowBanner( false ); // keep the banner from printing
    ApplicationContext ctx = app.run( args );

    // Get a weather service client wired and ready to use 
    Client client = ctx.getBean( Client.class );

    // determine the arguments for the call
    String zipCode = "43210";
    if( args.length > 0 )
    {
      zipCode = args[0];
    }

    // Make the call
    ForecastReturn response = client.getCityForecastByZip( zipCode );

    // print it out
    LOG.info(client.formatResponse( response ));

    // all done
    LOG.info( "Processing completed normally." );
  }

}
