import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';

import { TimeSeriesService } from '../../services/time-series.service';

@Component({
    selector: 'time-series-form',
    templateUrl: './time-series-form.component.html',
    styleUrls: ['./time-series-form.component.css']
})
export class TimeSeriesFormComponent {

    @ViewChild('timeSeriesForm') form: NgForm;

    constructor(private timeSeriesService: TimeSeriesService) { }

    onSubmit() {
        const timeSeries = this.form.value;
        this.timeSeriesService.saveTimeSeries(timeSeries).subscribe(id => {
            timeSeries.id = id;
            this.timeSeriesService.save.next(timeSeries);
        });
    }
}
