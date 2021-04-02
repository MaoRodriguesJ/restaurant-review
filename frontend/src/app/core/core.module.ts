import { CommonModule } from '@angular/common'
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http'
import { APP_INITIALIZER, NgModule } from '@angular/core'
import { MatMomentDateModule, MAT_MOMENT_DATE_FORMATS, MomentDateAdapter } from '@angular/material-moment-adapter'
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core'
import { RouterModule } from '@angular/router'
import { HttpInterceptorService } from './interceptor/http-interceptor.service'
import { UserContextService } from './service/user-context.service'

export const DATE_FORMAT = {
  parse: {
    dateInput: ['DD/MM/YYYY'],
  },
  display: {
    dateInput: 'DD/MM/YYYY',
    monthYearLabel: 'MMMM YYYY',
    dateA11yLabel: 'DD/MM/YYYY',
    monthYearA11yLabel: 'MMMM YYYY',
  },
}

@NgModule({
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorService, multi: true },
    {
      provide: APP_INITIALIZER,
      multi: true,
      deps: [UserContextService],
      useFactory: (userContext: UserContextService) => userContext.init(),
    },
    { provide: MAT_DATE_LOCALE, useValue: 'pt-BR' },
    { provide: DateAdapter, useClass: MomentDateAdapter },
    { provide: MAT_MOMENT_DATE_FORMATS, useValue: DATE_FORMAT },
    { provide: MAT_DATE_FORMATS, useValue: DATE_FORMAT },
  ],
  imports: [CommonModule, RouterModule, HttpClientModule, MatMomentDateModule],
})
export class CoreModule {}
