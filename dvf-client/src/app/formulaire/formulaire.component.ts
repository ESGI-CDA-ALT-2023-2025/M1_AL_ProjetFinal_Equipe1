import { Component } from '@angular/core';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { ApiService } from '../service/api.service';
import { HttpErrorResponse } from '@angular/common/http';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-formulaire',
  standalone: true,
  imports: [MatInputModule, FormsModule, MatButtonModule],
  templateUrl: './formulaire.component.html',
  styleUrl: './formulaire.component.scss',
})
export class FormulaireComponent {
  longitude!: number;
  latitude!: number;
  rayon!: number;

  messageError?: string;

  constructor(private _apiService: ApiService) {}

  onSubmit(): void {
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
          if (result !== null) {
            this.afficherPdf(result);
          } else {
            this.messageError =
              'Pas de données pour longitude : ' +
              this.longitude +
              ' latitude : ' +
              this.latitude +
              ' rayon : ' +
              this.rayon;
            console.log('No content received (204)');
          }
        },
        error: (error: HttpErrorResponse): void => {
          console.log('Erreur : ', error);
          alert(error.message);
        },
      });
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
