import { CommonModule } from '@angular/common'
import { NgModule } from '@angular/core'
import { ComponentsModule } from '@app/components/components.module'
import { AccountRoutingModule } from './account-routing.module'
import { AccountComponent } from './account.component'
import { CreateUserComponent } from './create-user/create-user.component'
import { CreateOwnerComponent } from './create-owner/create-owner.component'

@NgModule({
  declarations: [AccountComponent, CreateUserComponent, CreateOwnerComponent],
  imports: [CommonModule, AccountRoutingModule, ComponentsModule],
})
export class AccountModule {}
