import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private _apiUrl = "http://localhost:8201/a.php"

  constructor(private _http: HttpClient) { }

  getInfo(lon: number, lat: number, ray: number){
    return this._http.get(this._apiUrl + `?longitude=${lon}&latitude=${lat}&rayon=${ray}`);
  }
}
