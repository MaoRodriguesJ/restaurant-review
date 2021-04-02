import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Router } from '@angular/router'
import { LoginModel } from '@app/models'
import { mergeMap } from 'rxjs/operators'
import { environment } from 'src/environments/environment'
import { UserContextService } from '../core/service/user-context.service'

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  baseUrl = environment.baseUrl

  constructor(private http: HttpClient, private userContext: UserContextService, private router: Router) {}

  createSession(loginModel: LoginModel) {
    const formData = new FormData()
    formData.append('username', loginModel.email)
    formData.append('password', loginModel.password)
    return this.http.post(this.baseUrl + '/api/authorize', formData).pipe(mergeMap((_) => this.userContext.refresh()))
  }

  ifLogged() {
    return this.userContext.isLogged
  }

  navigateToHome() {
    this.router.navigate(['/home'])
  }
}
