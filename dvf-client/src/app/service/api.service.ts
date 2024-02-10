import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable, map} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private _apiUrl = "http://localhost:8080/"

  constructor(private _http: HttpClient) { }

  getInfo(lon: number, lat: number, ray: number): Observable<Blob | null> {
    return this._http.get(
      this._apiUrl + `pdf?longitude=${lon}&latitude=${lat}&rayon=${ray}`,
      { responseType: 'blob', observe: 'response' } // observe: 'response' pour complet HttpResponse
    ).pipe(
      map(response => {
        if (response.status === 204) {
          return null; // Si 294, donc null
        } else {
          return response.body as Blob;
        }
      })
    );
  }

}
