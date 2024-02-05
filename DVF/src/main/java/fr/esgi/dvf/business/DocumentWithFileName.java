package fr.esgi.dvf.business;

import com.itextpdf.layout.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class DocumentWithFileName {

  private Document document;
  
  private String fileName;
  
}
