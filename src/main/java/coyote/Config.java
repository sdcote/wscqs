package coyote;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;


@Configuration
public class Config
{

  /**
   * This is the marshaler of our SOAP requests & responses
   *  
   * @return Java XML Binding marshaler
   */
  @Bean
  public Jaxb2Marshaller marshaller()
  {
    // Create a standard marshaler
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

    // Tell it what package the WSDL generated classes into
    marshaller.setContextPath( "coyote.wx" );

    return marshaller;
  }




  /**
   * Create a client to the services using the JAXBv2 marshaler.
   *  
   * @param marshaller The JAXB marshaler with the context set to the java 
   * sources generated 
   * 
   * @return a client which can be used to invoke the services.
   */
  @Bean
  public Client weatherClient( Jaxb2Marshaller marshaller )
  {
    Client client = new Client();
    client.setDefaultUri( "http://wsf.cdyne.com/WeatherWS/Weather.asmx" );
    client.setMarshaller( marshaller );
    client.setUnmarshaller( marshaller );
    return client;
  }

}
