import { Component, NgModule } from '@angular/core';
import { ApiService } from '../service/api.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-formulaire',
  templateUrl: './formulaire.component.html',
  styleUrl: './formulaire.component.scss',
})
export class FormulaireComponent {
  longitude!: number;
  latitude!: number;
  rayon!: number;

  messageError?: string;
  isLoad: boolean = false;

  constructor(private _apiService: ApiService) {}

  onSubmit(): void {
    this.messageError = undefined;
    this.isLoad = true;

    console.log(
      'longitude: ',
      this.longitude,
      '\nlatitude: ',
      this.latitude,
      '\nrayon: ',
      this.rayon
    );

    this._apiService
      .getInfo(this.longitude, this.latitude, this.rayon)
      .subscribe({
        next: (result: Blob | null): void => {
          this.isLoad = false;

          if (result !== null) {
            this.afficherPdf(result);
          } else {
            console.log('No content received (204)');
            this.messageError = 'Aucune données disponible';
          }
        },
        error: (error: HttpErrorResponse): void => {
          this.isLoad = false;

          console.log('ERROR !');
          this.messageError = error.message;
        },
      })
  }

  afficherPdf(pdfBlobObject: Blob): void {
    let newBlob: Blob = new Blob([pdfBlobObject], { type: 'application/pdf' });

    const data: string = window.URL.createObjectURL(newBlob);

    let link = document.createElement('a');
    link.href = data;
    link.download = this._genererTitreFichier();
    link.dispatchEvent(
      new MouseEvent('click', { bubbles: true, cancelable: true, view: window })
    );
  }

  private _genererTitreFichier(): string {
    const now: Date = new Date();

    const annee: number = now.getFullYear();
    const mois: string = String(now.getMonth() + 1).padStart(2, '0'); // Les mois sont indexés de 0 à 11
    const jour: string = String(now.getDate()).padStart(2, '0');
    const heures: string = String(now.getHours()).padStart(2, '0');
    const minutes: string = String(now.getMinutes()).padStart(2, '0');

    return `DonneesFoncieres_${annee}-${mois}-${jour}_${heures}${minutes}`;
  }
}
