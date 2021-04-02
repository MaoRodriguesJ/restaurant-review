import { HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Router } from '@angular/router'
import { Observable, throwError } from 'rxjs'
import { catchError } from 'rxjs/operators'
import { UserContextService } from '../service/user-context.service'
import { SnackbarService } from '../snackbar/snackbar.service'

@Injectable({
  providedIn: 'root',
})
export class HttpInterceptorService implements HttpInterceptor {
  constructor(private router: Router, private userContext: UserContextService, private snackBar: SnackbarService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<any> {
    const newRequest = request.clone({
      withCredentials: true,
    })

    return next.handle(newRequest).pipe(
      catchError((err) => {
        if (this.userContext.isInitialized && !request.url.endsWith('api/authorize')) {
          if (401 === err.status) {
            this.userContext.destroy()
            this.snackBar.openSnackBar('Unauthorized', 'Dismiss')
            this.router.navigateByUrl('/login')
          } else if (403 === err.status) {
            this.snackBar.openSnackBar('Forbidden', 'Dismiss')
            this.router.navigateByUrl('/home')
          }
        }

        if (err.status?.toString().startsWith('5')) {
          this.snackBar.openSnackBar('Server error', 'Dismiss')
        }

        return throwError(err)
      })
    )
  }
}
