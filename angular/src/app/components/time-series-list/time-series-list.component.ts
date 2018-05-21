import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';

import { TimeSeriesService } from '../../services/time-series.service';
import { TimeSeries } from '../../models/TimeSeries';
import { TimeSeriesFilter } from '../../models/TimeSeriesFilter';

@Component({
    selector: 'time-series-list',
    templateUrl: './time-series-list.component.html',
    styleUrls: ['./time-series-list.component.css']
})
export class TimeSeriesListComponent implements OnInit, OnDestroy {
    keys = Object.keys;
    timeSeries: {[key:string]: TimeSeries[]} = {};
    timeSeriesFilter: TimeSeriesFilter = new TimeSeriesFilter();
    saveSubscription: Subscription = null;
    filterSubscription: Subscription = null;

    constructor(private timeSeriesService: TimeSeriesService) {}

    ngOnInit() {
        this.filterSubscription =
            this.timeSeriesService.filter.asObservable().subscribe((timeSeriesFilter: TimeSeriesFilter) => {
                this.timeSeriesFilter = timeSeriesFilter;
                this.getTimeSeries();
            });

        this.saveSubscription =
            this.timeSeriesService.save.asObservable().subscribe(() => this.getTimeSeries());
    }

    ngOnDestroy() {
        this.filterSubscription.unsubscribe();
        this.saveSubscription.unsubscribe();
    }

    getTimeSeries() {
        this.timeSeriesService.findBySensorNameAndGroupBy(this.timeSeriesFilter)
                              .subscribe(timeSeries => this.timeSeries = timeSeries);
    }
}
