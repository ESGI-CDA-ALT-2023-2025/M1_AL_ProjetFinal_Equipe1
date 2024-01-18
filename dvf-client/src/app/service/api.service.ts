import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private _apiUrl = "http://localhost:8080/"

  constructor(private _http: HttpClient) { }

  getInfo(lon: number, lat: number, ray: number){
    return this._http.get(this._apiUrl + `pdf?longitude=${lon}&latitude=${lat}&rayon=${ray}`);
  }
}
