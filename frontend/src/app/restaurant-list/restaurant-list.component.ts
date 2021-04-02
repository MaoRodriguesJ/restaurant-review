import { AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Output, ViewChild } from '@angular/core'
import { MatPaginator } from '@angular/material/paginator'
import { MatSort } from '@angular/material/sort'
import { Router } from '@angular/router'
import { RestaurantListPayloadModel, RestaurantViewModel } from '@app/models'
import { UserContextService } from '@core/service/user-context.service'
import { merge, of } from 'rxjs'
import { catchError, map, startWith, switchMap } from 'rxjs/operators'
import { RestaurantListService } from './restaurant-list.service'

@Component({
  selector: 'app-restaurant-list',
  templateUrl: './restaurant-list.component.html',
})
export class RestaurantListComponent implements AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator
  @ViewChild(MatSort) sort: MatSort
  @Output() filterChange: EventEmitter<string> = new EventEmitter()
  isLoadingResults = false
  rating = 0
  resultsLength = 0
  restaurants: RestaurantViewModel[]

  constructor(
    private service: RestaurantListService,
    private userContext: UserContextService,
    private router: Router,
    private cdRef: ChangeDetectorRef
  ) {}

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0))

    merge(this.sort.sortChange, this.paginator.page, this.filterChange)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true
          this.cdRef.detectChanges()
          return this.service
            .getRestaurants(this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize, this.rating)
            .pipe(
              catchError((e) => {
                return this.emptyIfError()
              })
            )
        }),
        map((data) => {
          this.isLoadingResults = false
          this.resultsLength = data.totalElements
          return data.content
        })
      )
      .subscribe((restaurants) => {
        this.restaurants = restaurants
        this.cdRef.detectChanges()
      })
  }

  isRestaurantOwner() {
    return this.userContext.hasRole('ROLE_OWNER')
  }

  isRegularUser() {
    return this.userContext.hasRole('ROLE_USER')
  }

  onViewClick(id: number) {
    this.router.navigate(['restaurants/' + id.toString()])
  }

  onRateClick(id: number) {
    this.router.navigate(['restaurants/' + id.toString() + '/rate'])
  }

  onPendingRepliesClick(id: number) {
    this.router.navigate(['restaurants/' + id.toString() + '/pending-replies'])
  }

  onRatingClick(event) {
    if (this.rating === event.rating) {
      this.rating = 0
    } else {
      this.rating = event.rating
    }
    this.filterChange.emit()
  }

  private emptyIfError() {
    return of({
      content: [],
      totalElements: 0,
    } as RestaurantListPayloadModel)
  }
}
