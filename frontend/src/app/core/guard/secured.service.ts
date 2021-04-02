import { Injectable } from '@angular/core'
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router'
import { UserContextService } from '../service/user-context.service'
import { SnackbarService } from '../snackbar/snackbar.service'

@Injectable({
  providedIn: 'root',
})
export class SecuredService implements CanActivate {
  constructor(private router: Router, private userContext: UserContextService, private snackBar: SnackbarService) {}
  canActivate(route: ActivatedRouteSnapshot) {
    if (!route?.data?.roles || !Array.isArray(route.data.roles)) {
      throw new Error('Expecting data roles[]: ' + route.routeConfig.path)
    }
    if (!this.userContext.hasAnyRole(route.data.roles)) {
      this.snackBar.openSnackBar('Forbidden', 'Dismiss')
      this.router.navigateByUrl('/home')
      return false
    }
    return true
  }
}
