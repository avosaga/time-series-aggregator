import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { TimeSeriesListComponent } from './components/time-series-list/time-series-list.component';
import { TimeSeriesService } from './services/time-series.service';
import { TimeSeriesFilterComponent } from './components/time-series-filter/time-series-filter.component';
import { TimeSeriesFormComponent } from './components/time-series-form/time-series-form.component';

@NgModule({
    declarations: [
        AppComponent,
        TimeSeriesListComponent,
        TimeSeriesFilterComponent,
        TimeSeriesFormComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        FormsModule
    ],
    providers: [
        TimeSeriesService
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
