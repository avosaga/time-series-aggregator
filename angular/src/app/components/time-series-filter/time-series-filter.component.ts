import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';

import { Subscription, fromEvent } from 'rxjs';
import { map, debounceTime } from 'rxjs/operators';

import { TimeSeriesService } from '../../services/time-series.service';
import { TimeSeriesFilter } from '../../models/TimeSeriesFilter';

@Component({
    selector: 'time-series-filter',
    templateUrl: './time-series-filter.component.html',
    styleUrls: ['./time-series-filter.component.css']
})
export class TimeSeriesFilterComponent implements OnInit, OnDestroy, AfterViewInit {
    @ViewChild('sensorName') sensorNameRef: ElementRef;
    groupBys: string[] = [];
    selectedGroupBy: string = null;
    sensorName: string = '';
    sensorNameSubscription: Subscription = null;

    constructor(private timeSeriesService: TimeSeriesService) {}

    ngOnInit() {
        this.timeSeriesService.findGroupBys().subscribe(groupBys => {
            this.groupBys = groupBys;
            this.setGroupBy(this.groupBys[0]);
        });
    }

    ngAfterViewInit() {
        this.sensorNameSubscription = fromEvent(this.sensorNameRef.nativeElement, 'keyup').pipe(
            debounceTime(1000),
            map((event: KeyboardEvent) => event.target['value'])
        ).subscribe(value => this.setSensorName(value));
    }

    ngOnDestroy() {
        this.sensorNameSubscription.unsubscribe();
    }

    setGroupBy(groupBy: string) {
        this.selectedGroupBy = groupBy;
        this.notify();
    }

    setSensorName(sensorName: string) {
        this.sensorName = sensorName;
        this.notify();
    }

    private notify() {
        this.timeSeriesService.filter.next(new TimeSeriesFilter(this.sensorName, this.selectedGroupBy));
    }
}
