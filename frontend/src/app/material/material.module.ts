// CDK Commmon Behaviors
import { A11yModule } from '@angular/cdk/a11y'
import { BidiModule } from '@angular/cdk/bidi'
import { DragDropModule } from '@angular/cdk/drag-drop'
import { LayoutModule } from '@angular/cdk/layout'
import { ObserversModule } from '@angular/cdk/observers'
import { OverlayModule } from '@angular/cdk/overlay'
import { PlatformModule } from '@angular/cdk/platform'
import { PortalModule } from '@angular/cdk/portal'
import { ScrollingModule } from '@angular/cdk/scrolling'
// CDK Components
import { CdkStepperModule } from '@angular/cdk/stepper'
import { CdkTableModule } from '@angular/cdk/table'
import { TextFieldModule } from '@angular/cdk/text-field'
import { CdkTreeModule } from '@angular/cdk/tree'
import { NgModule } from '@angular/core'
// Form Control
import { MatAutocompleteModule } from '@angular/material/autocomplete'
import { MatBadgeModule } from '@angular/material/badge'
// Popups & Modals
import { MatBottomSheetModule } from '@angular/material/bottom-sheet'
// Buttons & Indicators
import { MatButtonModule } from '@angular/material/button'
import { MatButtonToggleModule } from '@angular/material/button-toggle'
// Layout
import { MatCardModule } from '@angular/material/card'
import { MatCheckboxModule } from '@angular/material/checkbox'
import { MatChipsModule } from '@angular/material/chips'
import { MatRippleModule } from '@angular/material/core'
import { MatDatepickerModule } from '@angular/material/datepicker'
import { MatDialogModule } from '@angular/material/dialog'
import { MatDividerModule } from '@angular/material/divider'
import { MatExpansionModule } from '@angular/material/expansion'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatGridListModule } from '@angular/material/grid-list'
import { MatIconModule } from '@angular/material/icon'
import { MatInputModule } from '@angular/material/input'
import { MatListModule } from '@angular/material/list'
// Navigation
import { MatMenuModule } from '@angular/material/menu'
// Data table
import { MatPaginatorModule } from '@angular/material/paginator'
import { MatProgressBarModule } from '@angular/material/progress-bar'
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { MatRadioModule } from '@angular/material/radio'
import { MatSelectModule } from '@angular/material/select'
import { MatSidenavModule } from '@angular/material/sidenav'
import { MatSlideToggleModule } from '@angular/material/slide-toggle'
import { MatSliderModule } from '@angular/material/slider'
import { MatSnackBarModule } from '@angular/material/snack-bar'
import { MatSortModule } from '@angular/material/sort'
import { MatStepperModule } from '@angular/material/stepper'
import { MatTableModule } from '@angular/material/table'
import { MatTabsModule } from '@angular/material/tabs'
import { MatToolbarModule } from '@angular/material/toolbar'
import { MatTooltipModule } from '@angular/material/tooltip'
import { MatTreeModule } from '@angular/material/tree'

const modules = [
  MatAutocompleteModule,
  MatCheckboxModule,
  MatDatepickerModule,
  MatFormFieldModule,
  MatInputModule,
  MatRadioModule,
  MatSelectModule,
  MatSliderModule,
  MatSlideToggleModule,
  MatMenuModule,
  MatSidenavModule,
  MatToolbarModule,
  MatCardModule,
  MatDividerModule,
  MatExpansionModule,
  MatGridListModule,
  MatListModule,
  MatStepperModule,
  MatTabsModule,
  MatTreeModule,
  MatButtonModule,
  MatButtonToggleModule,
  MatBadgeModule,
  MatChipsModule,
  MatIconModule,
  MatProgressSpinnerModule,
  MatProgressBarModule,
  MatRippleModule,
  MatBottomSheetModule,
  MatDialogModule,
  MatSnackBarModule,
  MatTooltipModule,
  MatPaginatorModule,
  MatSortModule,
  MatTableModule,
  A11yModule,
  BidiModule,
  DragDropModule,
  LayoutModule,
  ObserversModule,
  OverlayModule,
  PlatformModule,
  PortalModule,
  ScrollingModule,
  TextFieldModule,
  CdkStepperModule,
  CdkTableModule,
  CdkTreeModule,
]

@NgModule({
  imports: [...modules],
  exports: [...modules],
})
export class MaterialModule {}
