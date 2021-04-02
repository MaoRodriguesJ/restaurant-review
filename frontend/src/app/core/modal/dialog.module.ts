import { CommonModule } from '@angular/common'
import { NgModule } from '@angular/core'
import { MaterialModule } from '@app/material/material.module'
import { ConfirmComponent } from './confirm/confirm.component'
import { InfoComponent } from './info/info.component'

@NgModule({
  declarations: [InfoComponent, ConfirmComponent],
  imports: [CommonModule, MaterialModule],
})
export class DialogModule {}
