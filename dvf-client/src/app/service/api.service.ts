import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private _apiUrl = "http://localhost:8080/"

  constructor(private _http: HttpClient) { }

  getInfo(lon: number, lat: number, ray: number): Observable<Blob> {
    return this._http.get(this._apiUrl + `pdf?longitude=${lon}&latitude=${lat}&rayon=${ray}`, {responseType: 'blob'});
  }

}
