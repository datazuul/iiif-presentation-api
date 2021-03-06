package de.digitalcollections.iiif.presentation.config;

import de.digitalcollections.iiif.presentation.frontend.impl.client.rest.IIIFRepository;
import de.digitalcollections.iiif.presentation.frontend.impl.client.rest.exceptions.IiifErrorDecoder;
import de.digitalcollections.iiif.presentation.model.impl.jackson.v2.IiifPresentationApiObjectMapper;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource(value = {
    "classpath:de/digitalcollections/iiif/presentation/config/SpringConfigClientRest-${spring.profiles.active:PROD}.properties"
})
public class SpringConfigClientRest {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpringConfigClientRest.class);

  @Value("${presentation.iiifRepositoryURL}")
  private String iiifRepositoryURL;

  @Bean
  public IIIFRepository iiifRepository() {
    LOGGER.info("IIIF Rest Client using Endpoint {}" + iiifRepositoryURL);
    IIIFRepository iiif = Feign.builder()
        .decoder(new JacksonDecoder(new IiifPresentationApiObjectMapper()))
        .errorDecoder(new IiifErrorDecoder())
        .target(IIIFRepository.class, iiifRepositoryURL);
    return iiif;
  }

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

}
