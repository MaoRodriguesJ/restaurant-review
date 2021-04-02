import { CommonModule } from '@angular/common'
import { NgModule } from '@angular/core'
import { FormsModule, ReactiveFormsModule } from '@angular/forms'
import { MaterialModule } from '../material/material.module'
import { LoginRoutingModule } from './login-routing.module'
import { LoginComponent } from './login/login.component'

@NgModule({
  declarations: [LoginComponent],
  imports: [CommonModule, LoginRoutingModule, MaterialModule, ReactiveFormsModule, FormsModule],
})
export class LoginModule {}
