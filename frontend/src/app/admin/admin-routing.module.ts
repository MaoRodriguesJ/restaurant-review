import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'
import { RestaurantEditComponent } from './restaurant-edit/restaurant-edit.component'
import { ReviewEditComponent } from './review-edit/review-edit.component'
import { SearchUserComponent } from './search-user/search-user.component'
import { UserEditComponent } from './user-edit/user-edit.component'

const routes: Routes = [
  { path: 'users/:id/edit', component: UserEditComponent },
  { path: 'restaurants/:id/edit', component: RestaurantEditComponent },
  { path: 'reviews/:id/edit', component: ReviewEditComponent },
  {
    path: 'search-user',
    component: SearchUserComponent,
  },
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminRoutingModule {}
