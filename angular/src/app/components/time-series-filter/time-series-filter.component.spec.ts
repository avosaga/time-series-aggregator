import { of } from 'rxjs';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TimeSeriesFilterComponent } from './time-series-filter.component';
import { TimeSeriesService } from '../../services/time-series.service';

class MockTimeSeriesService {
    filter = {next: () => {}};

    findGroupBys() {
        return of(['1', '2'])
    }
}

describe('TimeSeriesFilterComponent', () => {
    let component: TimeSeriesFilterComponent;
    let fixture: ComponentFixture<TimeSeriesFilterComponent>;
    let timeSeriesService: TimeSeriesService;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [
                TimeSeriesFilterComponent
            ],
            providers: [
                { provide: TimeSeriesService, useClass: MockTimeSeriesService }
            ]
        });

        fixture = TestBed.createComponent(TimeSeriesFilterComponent);
        component = fixture.componentInstance;
        timeSeriesService = TestBed.get(TimeSeriesService);
        fixture.detectChanges();
    }));

    it('should create component', () => {
        expect(component).toBeDefined();
    });

    it('should have groupBys after construction', () => {
        const expectedGroupBys = ['1', '2'];
        const element: HTMLElement = fixture.nativeElement;

        component.ngOnInit();

        console.log(element.querySelector('button.btn'));
        expect(component.groupBys).toEqual(expectedGroupBys);
        expect(element.querySelectorAll('button.btn').length).toBe(expectedGroupBys.length);
    });

    it('should set group-by', () => {
        spyOn(timeSeriesService.filter, 'next');

        component.setGroupBy('hour');

        expect(component.selectedGroupBy).toBe('hour');
        expect(timeSeriesService.filter.next).toHaveBeenCalled();
    });

    it('should set sensorName', () => {
        const expectedSensorName = 'SENSOR NAME';
        spyOn(timeSeriesService.filter, 'next');

        component.setSensorName(expectedSensorName);

        expect(component.sensorName).toBe(expectedSensorName);
        expect(timeSeriesService.filter.next).toHaveBeenCalled();
    });
});
