package fr.esgi.dvf.config;

import jakarta.jms.Queue;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JmsConfig {
  
  private final static String PDF_DOWNLOAD_QUEUE = "pdf-download-queue";

  @Bean
  public Queue pdfDownloadQueue() {
    return new ActiveMQQueue(JmsConfig.PDF_DOWNLOAD_QUEUE);
  }
}
