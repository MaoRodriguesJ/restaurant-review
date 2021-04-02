import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'
import { AccountComponent } from './account.component'
import { CreateOwnerComponent } from './create-owner/create-owner.component'
import { CreateUserComponent } from './create-user/create-user.component'

const routes: Routes = [
  { path: '', component: AccountComponent },
  { path: 'user', component: CreateUserComponent },
  { path: 'owner', component: CreateOwnerComponent },
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AccountRoutingModule {}
