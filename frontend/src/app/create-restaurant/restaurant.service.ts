import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { RestaurantInputModel } from '@app/models'
import { take } from 'rxjs/operators'
import { environment } from 'src/environments/environment'

@Injectable({
  providedIn: 'root',
})
export class RestaurantService {
  baseUrl = environment.baseUrl

  constructor(private http: HttpClient) {}

  public createRestaurant(form: RestaurantInputModel) {
    return this.http.post<RestaurantInputModel>(this.baseUrl + '/api/restaurants', form).pipe(take(1))
  }
}
