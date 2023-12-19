import { Component } from '@angular/core';
import {MatInputModule} from "@angular/material/input";
import {FormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {ApiService} from "../service/api.service";

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

    this._apiService.getInfo(this.longitude, this.latitude, this.rayon).subscribe();
  }

}
