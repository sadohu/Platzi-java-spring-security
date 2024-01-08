import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  pizzas: any;

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    const requestOptions = {

      headers: new HttpHeaders({
        'Authorization': 'Basic dXNlcjpkNWExNGQ0NC0wN2VjLTQ4NDItOTI1MC02MmI2ODE0Njk1NWY='
      }),
    };

    this.http.get('http://localhost:8080/api/pizzas/available', requestOptions).subscribe(response => {
      this.pizzas = response;
    });
  }
}
