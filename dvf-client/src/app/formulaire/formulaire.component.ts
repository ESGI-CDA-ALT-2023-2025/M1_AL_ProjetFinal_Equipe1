import { Component } from '@angular/core';
import {MatInputModule} from "@angular/material/input";
import {FormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {ApiService} from "../service/api.service";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";

@Component({
  selector: 'app-formulaire',
  standalone: true,
  imports: [
    MatInputModule,
    FormsModule,
    MatButtonModule
  ],
  templateUrl: './formulaire.component.html',
  styleUrl: './formulaire.component.scss'
})
export class FormulaireComponent {

  longitude!: number;
  latitude!: number;
  rayon!: number;

  constructor(private _apiService: ApiService) {
  }

  onSubmit() {
    console.log(
      "longitude: ", this.longitude,
      "\nlatitude: ", this.latitude,
      "\nrayon: ", this.rayon
    )

    this._apiService.getInfo(this.longitude, this.latitude, this.rayon).subscribe({
      next: (result: Blob) => {
        this.afficherPdf(result);
      },
      error: (error: HttpErrorResponse) => {
        alert(error.message);
      }
    });
  }

  afficherPdf(pdfBlobObject: Blob) {

    let newBlob = new Blob([pdfBlobObject], { type: "application/pdf" });

    const data = window.URL.createObjectURL(newBlob);

    let link = document.createElement('a');
    link.href = data;
    link.download = 'pdfFileName'; // TODO: Essayer de récupérer le nom du fichier PDF renvoyé dans l'en-tête par l'API. (Content-Disposition)
    link.dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true, view: window}));

    // setTimeout(function () {
    //   window.URL.revokeObjectURL(data);
    //   link.remove();
    // }, 100);
  }

}
