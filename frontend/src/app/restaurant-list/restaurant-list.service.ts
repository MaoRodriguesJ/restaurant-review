import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { RestaurantListPayloadModel } from '@app/models'
import { take } from 'rxjs/operators'
import { environment } from 'src/environments/environment'

@Injectable({
  providedIn: 'root',
})
export class RestaurantListService {
  baseUrl = environment.baseUrl

  constructor(private http: HttpClient) {}

  public getRestaurants(order: string, pageNumber: number, pageSize: number, rate: number) {
    return this.http
      .get<RestaurantListPayloadModel>(
        this.baseUrl + `/api/restaurants/?size=${pageSize}&page=${pageNumber}&sort=avgRate,${order}&rate=${rate}`
      )
      .pipe(take(1))
  }
}
