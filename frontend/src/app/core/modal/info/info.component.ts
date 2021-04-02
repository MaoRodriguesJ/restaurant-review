import { Component, Inject } from '@angular/core'
import { MAT_DIALOG_DATA } from '@angular/material/dialog'

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
})
export class InfoComponent {
  title: string
  message: string
  constructor(@Inject(MAT_DIALOG_DATA) data) {
    this.title = data.title
    this.message = '<p>' + data.message + '</p>'
  }
}
