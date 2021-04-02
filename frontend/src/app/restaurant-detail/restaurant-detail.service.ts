import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { RestaurantViewModel, ReviewListPayloadModel, ReviewModel } from '@app/models'
import { take } from 'rxjs/operators'
import { environment } from 'src/environments/environment'

@Injectable({
  providedIn: 'root',
})
export class RestaurantDetailService {
  baseUrl = environment.baseUrl

  constructor(private http: HttpClient) {}

  public getRestaurant(id: number) {
    return this.http.get<RestaurantViewModel>(this.baseUrl + `/api/restaurants/${id}`).pipe(take(1))
  }

  public getRestaurantHighestReview(id: number) {
    return this.http.get<ReviewModel>(this.baseUrl + `/api/restaurants/${id}/highestReview`).pipe(take(1))
  }

  public getRestaurantLowestReview(id: number) {
    return this.http.get<ReviewModel>(this.baseUrl + `/api/restaurants/${id}/lowestReview`).pipe(take(1))
  }

  public getRestaurantReviews(id: number, pageNumber: number, pageSize: number) {
    return this.http
      .get<ReviewListPayloadModel>(this.baseUrl + `/api/restaurants/${id}/reviews?size=${pageSize}&page=${pageNumber}`)
      .pipe(take(1))
  }
}
