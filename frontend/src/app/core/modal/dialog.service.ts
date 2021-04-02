import { Injectable } from '@angular/core'
import { MatDialog, MatDialogConfig } from '@angular/material/dialog'
import { ConfirmComponent } from './confirm/confirm.component'
import { InfoComponent } from './info/info.component'

@Injectable({
  providedIn: 'root',
})
export class DialogService {
  constructor(public dialog: MatDialog) {}

  openInfoDialog(dialogConfig: MatDialogConfig) {
    return this.dialog.open(InfoComponent, dialogConfig)
  }

  openConfirmDialog(dialogConfig: MatDialogConfig) {
    return this.dialog.open(ConfirmComponent, dialogConfig)
  }
}
