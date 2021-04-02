import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Router } from '@angular/router'
import { take, tap } from 'rxjs/operators'
import { environment } from 'src/environments/environment'

export interface RoleModel {
  id: number
  name: string
}

export interface UserModel {
  id: number
  username: string
  displayName: string
  roles: RoleModel[]
}

@Injectable({
  providedIn: 'root',
})
export class UserContextService {
  private user: UserModel
  private initialized: boolean
  baseUrl = environment.baseUrl

  constructor(private http: HttpClient, private router: Router) {}

  init() {
    return async () => {
      this.initialized = false
      try {
        await this.refresh().toPromise()
      } catch (err) {
        // no problem...probably not logged
      }
      this.initialized = true
    }
  }

  get isLogged() {
    return this.user?.id != null
  }

  get isInitialized() {
    return this.initialized
  }

  get userInfo() {
    return { ...this.user }
  }

  private userRoles() {
    return this.user?.roles
  }

  hasRole(role: string) {
    if (!this.isLogged) {
      return false
    }
    const roles = this.userRoles()
    for (let i = roles.length; i--; ) {
      if (roles[i].name === role) {
        return true
      }
    }
    return false
  }

  hasAnyRole(roles: string[]) {
    for (const role of roles) {
      if (this.hasRole(role)) {
        return true
      }
    }
    return false
  }

  destroy() {
    this.http
      .post(this.baseUrl + '/api/unauthorize', {})
      .pipe(take(1))
      .subscribe((_) => {
        this.user = null
        this.router.navigateByUrl('/login')
      })
  }

  refresh() {
    this.user = null
    return this.http.get<UserModel>(this.baseUrl + '/api/users/current').pipe(
      tap((user) => (this.user = user)),
      take(1)
    )
  }
}
