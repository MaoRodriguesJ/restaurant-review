import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { take } from 'rxjs/operators'
import { environment } from 'src/environments/environment'
import { PasswordModel, RestaurantInputModel, ReviewInputModel, ReviewModel, UserModel } from './models'

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  baseUrl = environment.baseUrl

  constructor(private http: HttpClient) {}

  public searchUser(email: string) {
    return this.http.get<UserModel>(this.baseUrl + `/api/users/search/${email}`).pipe(take(1))
  }

  public getUser(id: number) {
    return this.http.get<UserModel>(this.baseUrl + `/api/users/${id}`).pipe(take(1))
  }

  public editUser(id: number, user: UserModel) {
    return this.http.put(this.baseUrl + `/api/users/${id}`, user).pipe(take(1))
  }

  public changePassword(id: number, password: PasswordModel) {
    return this.http.put(this.baseUrl + `/api/users/${id}/password`, password).pipe(take(1))
  }

  public deleteUser(id: number) {
    return this.http.delete(this.baseUrl + `/api/users/${id}`).pipe(take(1))
  }

  public editRestaurant(id: number, restaurant: RestaurantInputModel) {
    return this.http.put(this.baseUrl + `/api/restaurants/${id}`, restaurant).pipe(take(1))
  }

  public deleteRestaurant(id: number) {
    return this.http.delete(this.baseUrl + `/api/restaurants/${id}`).pipe(take(1))
  }

  public getReview(id: number) {
    return this.http.get<ReviewModel>(this.baseUrl + `/api/reviews/${id}`).pipe(take(1))
  }

  public editReview(id: number, review: ReviewInputModel) {
    return this.http.put(this.baseUrl + `/api/reviews/${id}`, review).pipe(take(1))
  }

  public deleteReview(id: number) {
    return this.http.delete(this.baseUrl + `/api/reviews/${id}`).pipe(take(1))
  }

  public editReply(id: number, reply: string) {
    return this.http.put(this.baseUrl + `/api/reviews/${id}/replies`, reply).pipe(take(1))
  }

  public deleteReply(id: number) {
    return this.http.delete(this.baseUrl + `/api/reviews/${id}/replies`).pipe(take(1))
  }
}
