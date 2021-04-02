import { Component, Inject } from '@angular/core'
import { MAT_DIALOG_DATA } from '@angular/material/dialog'

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
})
export class ConfirmComponent {
  title: string
  message: string
  primaryOption: string
  secondaryOption: string
  constructor(@Inject(MAT_DIALOG_DATA) data) {
    this.title = data.title
    this.message = '<p>' + data.message + '</p>'
    this.primaryOption = data.primaryOption
    this.secondaryOption = data.secondaryOption
  }
}
