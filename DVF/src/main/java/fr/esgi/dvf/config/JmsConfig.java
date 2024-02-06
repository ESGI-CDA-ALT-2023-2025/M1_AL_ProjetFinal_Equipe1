package fr.esgi.dvf.config;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.jms.Queue;

@Configuration
public class JmsConfig {
  
  private static final String PDF_DOWNLOAD_QUEUE = "pdf-generator-queue";

  @Bean
  public Queue pdfDownloadQueue() {
    return new ActiveMQQueue(JmsConfig.PDF_DOWNLOAD_QUEUE);
  }
}
