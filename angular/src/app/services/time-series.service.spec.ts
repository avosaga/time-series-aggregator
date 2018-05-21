import { defer } from 'rxjs';

import { TimeSeriesService } from './time-series.service';
import { TimeSeries } from '../models/TimeSeries';
import { TimeSeriesFilter } from '../models/TimeSeriesFilter';

describe('TimeSeriesService', () => {
    let httpClientSpy: { get: jasmine.Spy, post: jasmine.Spy };
    let timeSeriesService: TimeSeriesService;

    beforeEach(() => {
        httpClientSpy = jasmine.createSpyObj('HttpClient', ['get', 'post']);
        timeSeriesService = new TimeSeriesService(<any> httpClientSpy);
    });

    it('should return expected Time Series', () => {
        const expectedTimeSeries = {
            '2018': [new TimeSeries(1, 'SENSOR 1', 1, '2018-10-10 10:10')]
        };
        httpClientSpy.get.and.returnValue(asyncData(expectedTimeSeries));

        timeSeriesService.findBySensorNameAndGroupBy(new TimeSeriesFilter()).subscribe(timeSeries => {
            expect(timeSeries).toEqual(expectedTimeSeries),
            fail
        });

        expect(httpClientSpy.get.calls.count()).toBe(1);
    });

    it('should return group-bys for filtering', () => {
        const expectedGroupBys = ['hour', 'month', 'day', 'year'];
        httpClientSpy.get.and.returnValue(asyncData(expectedGroupBys));

        timeSeriesService.findGroupBys().subscribe(groupBys =>
            expect(groupBys).toEqual(expectedGroupBys),
            fail
        );

        expect(httpClientSpy.get.calls.count()).toBe(1);
    });

    it('should save new Time Series', () => {
        const expectedTimeSeries = new TimeSeries(1, '1', 1, '1');
        httpClientSpy.post.and.returnValue(asyncData(expectedTimeSeries));

        timeSeriesService.saveTimeSeries(new TimeSeries(1, '1', 1, '1')).subscribe(timeSeries =>
            expect(timeSeries).toEqual(expectedTimeSeries),
            fail
        );

        expect(httpClientSpy.post.calls.count()).toBe(1);
    });
});

export function asyncData<T>(data: T) {
  return defer(() => Promise.resolve(data));
}

