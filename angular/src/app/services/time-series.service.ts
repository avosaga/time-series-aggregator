import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subject } from 'rxjs';

import { TimeSeries } from '../models/TimeSeries';
import { TimeSeriesFilter } from '../models/TimeSeriesFilter';

@Injectable()
export class TimeSeriesService {
    filter = new Subject<TimeSeriesFilter>();
    save = new Subject<TimeSeries>();

    constructor(private http: HttpClient) {}

    findBySensorNameAndGroupBy(timeSeriesFilter: TimeSeriesFilter) {
        return this.http.get<{[key:string]: TimeSeries[]}>(
          `/api/timeseries?sensorName=${timeSeriesFilter.sensorName}&groupBy=${timeSeriesFilter.groupBy}`
        )
    }

    saveTimeSeries(timeSeries: TimeSeries) {
        return this.http.post<TimeSeries>('/api/timeseries', timeSeries);
    }

    findGroupBys() {
        return this.http.get<string[]>('/api/groupBys');
    }
}
