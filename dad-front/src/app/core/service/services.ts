import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {resources} from '../resources/resources';


@Injectable({providedIn: 'root'})
export class Services {
  constructor(private http: HttpClient) {
  }

  getCategories(): Observable<any[]> {
    return this.http.get<any[]>(resources.catalogue.category); // Se agregará la baseUrl por el interceptor
  }

  getProduct(): Observable<any[]> {
    return this.http.get<any[]>("producto"); // Se agregará la baseUrl por el interceptor
  }

}
