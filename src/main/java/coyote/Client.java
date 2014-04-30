package coyote;

import java.text.SimpleDateFormat;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

// These imports are for those classes generated from the wsdl
import coyote.wx.Forecast;
import coyote.wx.ForecastReturn;
import coyote.wx.GetCityForecastByZIP;
import coyote.wx.GetCityForecastByZIPResponse;
import coyote.wx.Temp;


public class Client extends WebServiceGatewaySupport
{

  /**
   * Get the forecast for a zipcode
   * 
   * @param zipCode
   * 
   * @return
   */
  public ForecastReturn getCityForecastByZip( String zipCode )
  {
    GetCityForecastByZIP request = new GetCityForecastByZIP();
    request.setZIP( zipCode );

    GetCityForecastByZIPResponse response = (GetCityForecastByZIPResponse)getWebServiceTemplate().marshalSendAndReceive( request, new SoapActionCallback( "http://ws.cdyne.com/WeatherWS/GetCityForecastByZIP" ) );
    ForecastReturn forecast = response.getGetCityForecastByZIPResult();

    return forecast;
  }




  /**
   * Format the response object to a string.
   * 
   * @param response the forecast response to format.
   */
  public String formatResponse( ForecastReturn forecastReturn )
  {
    if( forecastReturn != null && forecastReturn.isSuccess() )
    {
      StringBuilder buff = new StringBuilder( "Forecast for " );
      buff.append( forecastReturn.getCity() );
      buff.append( ", " );
      buff.append( forecastReturn.getState() );
      buff.append( System.getProperty( "line.separator" ) );

      SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
      for( Forecast forecast : forecastReturn.getForecastResult().getForecast() )
      {
        buff.append( format.format( forecast.getDate().toGregorianCalendar().getTime() ) );
        buff.append( " " );
        buff.append( forecast.getDesciption() );
        buff.append( " " );
        Temp temperature = forecast.getTemperatures();
        buff.append( temperature.getMorningLow() + "\u00b0-" + temperature.getDaytimeHigh() + "\u00b0 " );
        buff.append( System.getProperty( "line.separator" ) );
      }
      return buff.toString();
    }
    else
    {
      return "No forecast received";
    }
  }

}
