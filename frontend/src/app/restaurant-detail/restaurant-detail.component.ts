import { AfterViewInit, ChangeDetectorRef, Component, EventEmitter, OnInit, Output, ViewChild } from '@angular/core'
import { MatPaginator } from '@angular/material/paginator'
import { ActivatedRoute, Router } from '@angular/router'
import { AdminService } from '@app/admin.service'
import { RestaurantViewModel, ReviewListPayloadModel, ReviewModel } from '@app/models'
import { UserContextService } from '@core/service/user-context.service'
import { SnackbarService } from '@core/snackbar/snackbar.service'
import { merge, of } from 'rxjs'
import { catchError, map, startWith, switchMap } from 'rxjs/operators'
import { RestaurantDetailService } from './restaurant-detail.service'

@Component({
  templateUrl: './restaurant-detail.component.html',
})
export class RestaurantDetailComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator
  isLoadingResults = false
  resultsLength = 0
  reviews: ReviewModel[]
  restaurantId: number
  restaurant: RestaurantViewModel
  highestReview: ReviewModel
  lowestReview: ReviewModel
  zoom: number
  latitude: number
  longitude: number
  @Output() refreshReviewList: EventEmitter<boolean> = new EventEmitter()

  constructor(
    private service: RestaurantDetailService,
    private userContext: UserContextService,
    private router: Router,
    private activeRoute: ActivatedRoute,
    private cdRef: ChangeDetectorRef,
    private snackBar: SnackbarService,
    private adminService: AdminService
  ) {}

  ngOnInit(): void {
    this.zoom = 2
    this.latitude = 0
    this.longitude = 0
    this.activeRoute.params.subscribe((params) => {
      this.restaurantId = params.id
      this.service.getRestaurant(this.restaurantId).subscribe(
        (data) => {
          this.latitude = data.latitude
          this.longitude = data.longitude
          this.zoom = 14
          this.restaurant = data
        },
        (err) => {
          if (err?.status === 400) {
            this.snackBar.openSnackBar('Bad request', 'Dismiss')
            this.router.navigate(['/home'])
          }
        }
      )
      this.fetchReviews()
    })
  }

  ngAfterViewInit() {
    merge(this.paginator.page, this.refreshReviewList)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true
          this.cdRef.detectChanges()
          return this.service
            .getRestaurantReviews(this.restaurantId, this.paginator.pageIndex, this.paginator.pageSize)
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
      .subscribe((reviews) => {
        this.reviews = reviews
        this.cdRef.detectChanges()
      })
  }

  refresh() {
    this.fetchReviews()
    this.refreshReviewList.emit()
  }

  fetchReviews() {
    this.service.getRestaurantHighestReview(this.restaurantId).subscribe((data) => {
      this.highestReview = data
    })
    this.service.getRestaurantLowestReview(this.restaurantId).subscribe((data) => {
      this.lowestReview = data
    })
  }

  onEdit(id: number) {
    this.router.navigate([`/admin/restaurants/${id}/edit`])
  }

  onDelete(id: number) {
    this.adminService.deleteRestaurant(id).subscribe((_) => {
      this.snackBar.openSnackBar('Restaurant deleted', 'Dismiss')
      this.router.navigate(['/home'])
    })
  }

  isRegularUser() {
    return this.userContext.hasRole('ROLE_USER')
  }

  isAdmin() {
    return this.userContext.hasRole('ROLE_ADMIN')
  }

  private emptyIfError() {
    return of({
      content: [],
      totalElements: 0,
    } as ReviewListPayloadModel)
  }
}
