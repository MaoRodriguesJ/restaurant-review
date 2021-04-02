import { CommonModule } from '@angular/common'
import { NgModule } from '@angular/core'
import { ComponentsModule } from '@app/components/components.module'
import { AdminRoutingModule } from './admin-routing.module'
import { RestaurantEditComponent } from './restaurant-edit/restaurant-edit.component'
import { ReviewEditComponent } from './review-edit/review-edit.component'
import { SearchUserComponent } from './search-user/search-user.component'
import { UserEditComponent } from './user-edit/user-edit.component'

@NgModule({
  declarations: [UserEditComponent, SearchUserComponent, RestaurantEditComponent, ReviewEditComponent],
  imports: [CommonModule, AdminRoutingModule, ComponentsModule],
})
export class AdminModule {}
