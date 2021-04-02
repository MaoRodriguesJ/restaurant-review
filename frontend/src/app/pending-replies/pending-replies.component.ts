import { AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core'
import { MatPaginator } from '@angular/material/paginator'
import { ActivatedRoute, Router } from '@angular/router'
import { ReviewListPayloadModel, ReviewModel } from '@app/models'
import { merge, of } from 'rxjs'
import { catchError, map, startWith, switchMap } from 'rxjs/operators'
import { PendingRepliesService } from './pending-replies.service'

@Component({
  templateUrl: './pending-replies.component.html',
})
export class PendingRepliesComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator
  isLoadingResults = false
  resultsLength = 0
  reviews: ReviewModel[]
  restaurantId: number

  constructor(
    private service: PendingRepliesService,
    private router: Router,
    private activeRoute: ActivatedRoute,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.activeRoute.params.subscribe((params) => {
      this.restaurantId = params.id
    })
  }

  ngAfterViewInit() {
    merge(this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true
          this.cdRef.detectChanges()
          return this.service
            .getReviewsPendingReply(this.restaurantId, this.paginator.pageIndex, this.paginator.pageSize)
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

  private emptyIfError() {
    return of({
      content: [],
      totalElements: 0,
    } as ReviewListPayloadModel)
  }
}
