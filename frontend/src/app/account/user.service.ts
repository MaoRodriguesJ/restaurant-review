import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { ResetPasswordModel, RestaurantOwnerInputModel, UserInputModel } from '@app/models'
import { UserModel } from '@core/service/user-context.service'
import { take } from 'rxjs/operators'
import { environment } from 'src/environments/environment'

@Injectable({
  providedIn: 'root',
})
export class UserService {
  baseUrl = environment.baseUrl

  constructor(private http: HttpClient) {}

  public createUser(form: UserInputModel) {
    return this.http.post<UserModel>(this.baseUrl + '/api/public/users', form).pipe(take(1))
  }

  public createRestaurantOwner(form: RestaurantOwnerInputModel) {
    return this.http.post<UserModel>(this.baseUrl + '/api/public/users/restaurant', form).pipe(take(1))
  }

  public forgotPassword(email: string) {
    return this.http.post(this.baseUrl + '/api/public/users/resetToken', email).pipe(take(1))
  }

  public validResetToken(token: string, email: string) {
    return this.http.get<boolean>(this.baseUrl + `/api/public/users/password/${token}/${email}/valid`).pipe(take(1))
  }

  public resetPassword(form: ResetPasswordModel, token: string, email: string) {
    return this.http.put(this.baseUrl + `/api/public/users/password/${token}/${email}`, form).pipe(take(1))
  }
}
