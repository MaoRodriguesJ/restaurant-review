import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'
import { AccountEditComponent } from './account-edit/account-edit.component'
import { SecuredService } from './core/guard/secured.service'
import { CreateRestaurantComponent } from './create-restaurant/create-restaurant.component'
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component'
import { HomeComponent } from './home/home.component'
import { HeaderComponent } from './layout/header/header.component'
import { PendingRepliesComponent } from './pending-replies/pending-replies.component'
import { RatingComponent } from './rating/rating.component'
import { ResetPasswordComponent } from './reset-password/reset-password.component'
import { RestaurantDetailComponent } from './restaurant-detail/restaurant-detail.component'

const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'reset-password/:token/:email', component: ResetPasswordComponent },
  { path: 'login', loadChildren: () => import('./login/login.module').then((m) => m.LoginModule) },
  { path: 'create', loadChildren: () => import('./account/account.module').then((m) => m.AccountModule) },
  {
    path: '',
    component: HeaderComponent,
    children: [
      {
        path: 'home',
        component: HomeComponent,
        canActivate: [SecuredService],
        data: { roles: ['ROLE_ADMIN', 'ROLE_USER', 'ROLE_OWNER'] },
      },
      {
        path: 'create-restaurant',
        component: CreateRestaurantComponent,
        canActivate: [SecuredService],
        data: { roles: ['ROLE_OWNER'] },
      },
      {
        path: 'restaurants/:id/rate',
        component: RatingComponent,
        canActivate: [SecuredService],
        data: { roles: ['ROLE_USER'] },
      },
      {
        path: 'restaurants/:id/pending-replies',
        component: PendingRepliesComponent,
        canActivate: [SecuredService],
        data: { roles: ['ROLE_OWNER'] },
      },
      {
        path: 'restaurants/:id',
        component: RestaurantDetailComponent,
        canActivate: [SecuredService],
        data: { roles: ['ROLE_ADMIN', 'ROLE_USER', 'ROLE_OWNER'] },
      },
      {
        path: 'account',
        component: AccountEditComponent,
        canActivate: [SecuredService],
        data: { roles: ['ROLE_ADMIN', 'ROLE_USER', 'ROLE_OWNER'] },
      },
    ],
  },
  {
    path: 'admin',
    component: HeaderComponent,
    canActivate: [SecuredService],
    data: { roles: ['ROLE_ADMIN'] },
    loadChildren: () => import('./admin/admin.module').then((m) => m.AdminModule),
  },
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
